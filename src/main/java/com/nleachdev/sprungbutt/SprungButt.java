package com.nleachdev.sprungbutt;

import com.nleachdev.sprungbutt.annotation.InjectThings;
import com.nleachdev.sprungbutt.annotation.Thing;
import com.nleachdev.sprungbutt.annotation.ThingSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static com.nleachdev.sprungbutt.ThingScanner.getClasses;

public class SprungButt {
    private static final Logger logger = LoggerFactory.getLogger(SprungButt.class);
    private static final Map<Class<?>, Object> instancePerThing = new HashMap<>();

    public static Object getThing(final Class<?> thingType) {
        return instancePerThing.get(thingType);
    }

    public static void startEnvironment(final String packageName) throws IOException {
        logger.info("Starting SprungButt Environment");
        final LocalDateTime start = LocalDateTime.now();

        final Map<Class<?>, List<Class<?>>> depsPerThing = new HashMap<>();
        final Map<Class<?>, Method> methodPerMethodBasedThing = new HashMap<>();
        final Map<Class<?>, Object> setupInstancePerThing = new HashMap<>();

        getClasses(packageName).forEach(clazz -> {
            if (clazz.getAnnotation(ThingSetup.class) != null) {
                final Object classInstance = getInstanceOfClass(clazz);
                Stream.of(clazz.getMethods())
                        .filter(method -> method.getAnnotation(Thing.class) != null)
                        .forEach(method -> {
                            final List<Class<?>> methodDeps = Arrays.asList(method.getParameterTypes());
                            final Class<?> thingType = method.getReturnType();
                            depsPerThing.put(thingType, methodDeps);
                            methodPerMethodBasedThing.put(thingType, method);
                            setupInstancePerThing.put(thingType, classInstance);
                        });

            } else if (clazz.getAnnotation(Thing.class) != null) {
                depsPerThing.put(clazz, getDepsForClassBasedThing(clazz));
            }
        });

        logger.debug("Deps per thing: {}", depsPerThing);
        logger.debug("Method per method based thing: {}", methodPerMethodBasedThing);

        final Map<Integer, List<Class<?>>> thingsPerDepCost = getThingsPerDepCost(depsPerThing);
        logger.debug("thingsPerDepCost: {}", thingsPerDepCost);

        thingsPerDepCost.keySet().stream().sorted().forEach(depCost -> {
            final List<Class<?>> thingsToSetup = thingsPerDepCost.get(depCost);

            thingsToSetup.forEach(thing -> {
                final Method method = methodPerMethodBasedThing.get(thing);
                if (method != null) {
                    setupInstanceForMethodBasedThing(thing, method, setupInstancePerThing.get(thing));
                }

                setupInstanceForClassBasedThing(thing, depsPerThing.get(thing));
            });
        });

        final LocalDateTime finish = LocalDateTime.now();

        logger.debug("instance per thing: {}", instancePerThing);
        logger.info("SprungButt Environment Created in {} milliseconds", Duration.between(start, finish).toMillis());
    }

    private static void setupInstanceForClassBasedThing(final Class<?> thing, final List<Class<?>> depsForThing) {
        final Object[] depInstances = depsForThing.stream()
                .map(instancePerThing::get)
                .toArray();
        final Constructor<?> thingConstructor = Stream.of(thing.getConstructors())
                .filter(constructor -> constructor.getAnnotation(InjectThings.class) != null)
                .findFirst().orElse(null);

        if (thingConstructor == null) {
            return;
        }

        final Object thingInstance = getThingFromConstructor(thingConstructor, depInstances);
        if (thingInstance == null) {
            return;
        }

        instancePerThing.put(thing, thingInstance);
    }

    private static void setupInstanceForMethodBasedThing(final Class<?> thing, final Method method, final Object setupClassInstance) {
        final Object[] depInstances = Stream.of(method.getParameterTypes())
                .map(instancePerThing::get)
                .toArray();

        final Object thingInstance = getThingFromMethod(setupClassInstance, method, depInstances);
        instancePerThing.put(thing, thingInstance);
    }

    private static void setupDepCostForThing(final Class<?> thing, final List<Class<?>> dependencies,
                                             final Map<Class<?>, Integer> depCostPerThing,
                                             final Map<Class<?>, List<Class<?>>> depsPerThing) {
        if (dependencies == null || dependencies.isEmpty()) {
            depCostPerThing.put(thing, 0);
            return;
        }

        int depCost = 1;

        for (final Class<?> dependency : dependencies) {
            if (!depCostPerThing.containsKey(dependency)) {
                setupDepCostForThing(dependency, depsPerThing.get(dependency), depCostPerThing, depsPerThing);
            }
            depCost += depCostPerThing.get(dependency);
        }

        depCostPerThing.put(thing, depCost);
    }

    private static Object getThingFromConstructor(final Constructor<?> constructor, final Object[] deps) {
        try {
            return constructor.newInstance(deps);
        } catch (final IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logger.error("Error getting Thing from constructor", e);
            return null;
        }
    }

    private static <T> T getInstanceOfClass(final Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (final InstantiationException | IllegalAccessException e) {
            logger.warn("Error instantiating new instance of class", e);
        }

        return null;
    }

    private static Object getThingFromMethod(final Object instance, final Method method, final Object[] deps) {
        try {
            if (deps == null || deps.length == 0) {
                return method.invoke(instance);
            }
            return method.invoke(instance, deps);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            logger.warn("Error thrown instantiating Thing from ThingSetup class method", e);
            return null;
        }
    }

    private static List<Class<?>> getDepsForClassBasedThing(final Class<?> clazz) {
        final Constructor<?> injectionPoint = Stream.of(clazz.getConstructors())
                .filter(constructor -> constructor.getAnnotation(InjectThings.class) != null)
                .findFirst().orElse(null);
        if (injectionPoint == null) {
            return new ArrayList<>();
        }

        return Arrays.asList(injectionPoint.getParameterTypes());
    }

    private static Map<Integer, List<Class<?>>> getThingsPerDepCost(final Map<Class<?>, List<Class<?>>> depsPerThing) {
        final Map<Class<?>, Integer> depCostPerThing = new HashMap<>();
        depsPerThing.forEach((thing, depsForThing) -> {
            setupDepCostForThing(thing, depsForThing, depCostPerThing, depsPerThing);
        });

        logger.debug("depCostPerThing: {}", depCostPerThing);

        return groupThingsPerDepCost(depCostPerThing);
    }

    private static Map<Integer, List<Class<?>>> groupThingsPerDepCost(final Map<Class<?>, Integer> depCostPerThing) {
        final Map<Integer, List<Class<?>>> thingsPerDepCost = new HashMap<>();
        depCostPerThing.forEach((thing, depCost) -> {
            if (thingsPerDepCost.get(depCost) == null) {
                final List<Class<?>> things = new ArrayList<>();
                things.add(thing);
                thingsPerDepCost.put(depCost, things);
            } else {
                thingsPerDepCost.get(depCost).add(thing);
            }
        });

        return thingsPerDepCost;
    }
}