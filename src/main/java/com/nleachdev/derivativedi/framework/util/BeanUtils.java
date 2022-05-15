package com.nleachdev.derivativedi.framework.util;

import com.nleachdev.derivativedi.framework.annotation.*;
import com.nleachdev.derivativedi.framework.core.Containerr;
import com.nleachdev.derivativedi.framework.domain.BeanType;
import com.nleachdev.derivativedi.framework.exception.AmbiguousConstructorException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BeanUtils {
    private static final String AMBIGUOUS_EXCEPTION_MSG = "Multiple constructors targeted for injection point in class: %s";

    private BeanUtils() {
    }

    public static boolean classIsConcreteComponentType(final Class<?> clazz) {
        final Set<Class<?>> annotationTypesForClass = Stream.of(clazz.getAnnotations())
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());

        final boolean containsComponentAnnotation = Containerr.getInstance().getConfig().getComponentAnnotations().stream()
                .anyMatch(annotationTypesForClass::contains);
        return containsComponentAnnotation && isConcreteType(clazz);
    }

    private static boolean isConcreteType(final Class<?> clazz) {
        return !clazz.isInterface()
                && !clazz.isAnnotation()
                && !Modifier.isAbstract(clazz.getModifiers());
    }

    public static boolean isConfigurationBean(final Class<?> clazz) {
        return clazz.isAnnotationPresent(Config.class);
    }

    public static boolean isComponentBean(final Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class) && !clazz.isAnnotationPresent(Config.class);
    }

    public static String getBeanName(final Class<?> clazz) {
        return isComponentBean(clazz)
                ? getComponentBeanName(clazz)
                : getConfigBeanName(clazz);
    }

    public static String getConfigBeanName(final Class<?> clazz) {
        final Config config = clazz.getAnnotation(Config.class);
        final String qualifiedName = config.name();
        return qualifiedName.isEmpty()
                ? clazz.getSimpleName()
                : qualifiedName;
    }

    public static String getComponentBeanName(final Class<?> clazz) {
        final Component component = clazz.getAnnotation(Component.class);
        final String qualifiedName = component.name();
        return qualifiedName.isEmpty()
                ? clazz.getSimpleName()
                : qualifiedName;
    }

    public static String getMethodBeanName(final Method method) {
        final Bean annotation = method.getAnnotation(Bean.class);
        if (annotation == null || annotation.name().isEmpty()) {
            return method.getName();
        }

        return annotation.name();
    }

    public static String getParameterName(final Parameter parameter) {
        if (!parameter.isAnnotationPresent(Named.class)) {
            return parameter.getType().getSimpleName();
        }

        final Named annotation = parameter.getAnnotation(Named.class);
        final String qualifiedName = annotation.value();
        return qualifiedName.isEmpty()
                ? parameter.getName()
                : qualifiedName;
    }

    public static <T> Constructor<T> getBeanConstructor(final Class<T> clazz) {
        final Constructor<T>[] constructors = Stream.of(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                .toArray(Constructor[]::new);

        if (constructors.length > 1) {
            throw new AmbiguousConstructorException(String.format(AMBIGUOUS_EXCEPTION_MSG, clazz.getSimpleName()));
        }

        return constructors.length == 1
                ? constructors[0]
                : null;
    }

    public static boolean isPartOfActiveProfiles(final Class<?> type, final Set<String> activeProfiles) {
        final Profile profile = type.getAnnotation(Profile.class);

        return activeProfiles.isEmpty() || profile == null || activeProfiles.contains(profile.profileName());
    }

    public static BeanType getBeanType(final Class<?> clazz) {
        return isComponentBean(clazz)
                ? BeanType.COMPONENT
                : BeanType.CONFIG_COMPONENT;
    }
}
