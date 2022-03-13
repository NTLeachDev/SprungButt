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
                final Constructor<?> injectionPoint = Stream.of(clazz.getConstructors())
                        .filter(constructor -> constructor.getAnnotation(InjectThings.class) != null)
                        .findFirst().orElse(null);
                if (injectionPoint == null) {
                    return;
                }

                depsPerThing.put(clazz, Arrays.asList(injectionPoint.getParameterTypes()));
            }
        });

        logger.info("Deps per thing: {}", depsPerThing);
        logger.info("Method per method based thing: {}", methodPerMethodBasedThing);

        final Map<Class<?>, Integer> depCostPerThing = new HashMap<>();
        depsPerThing.forEach((thing, depsForThing) -> {
            setupDepCostForThing(thing, depsForThing, depCostPerThing, depsPerThing);
        });

        logger.info("depCostPerThing: {}", depCostPerThing);

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

        logger.info("thingsPerDepCost: {}", thingsPerDepCost);

        thingsPerDepCost.keySet().stream().sorted().forEach(depCost -> {
            final List<Class<?>> thingsToSetup = thingsPerDepCost.get(depCost);

            thingsToSetup.forEach(thing -> {
                final Method method = methodPerMethodBasedThing.get(thing);
                if (method != null) {
                    final Object[] depInstances = Stream.of(method.getParameterTypes())
                            .map(instancePerThing::get)
                            .toArray();

                    final Object setupClassInstance = setupInstancePerThing.get(thing);
                    final Object thingInstance = getThingFromMethod(setupClassInstance, method, depInstances);
                    instancePerThing.put(thing, thingInstance);
                }

                final Object[] depInstances = depsPerThing.get(thing).stream()
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
            });
        });

        logger.info("instance per thing: {}", instancePerThing);
    }

    private static void setupDepCostForThing(final Class<?> thing, final List<Class<?>> dependencies, final Map<Class<?>, Integer> depCostPerThing, final Map<Class<?>, List<Class<?>>> depsPerThing) {
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
}