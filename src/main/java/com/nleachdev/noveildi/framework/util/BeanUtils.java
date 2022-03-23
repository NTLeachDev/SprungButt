package com.nleachdev.noveildi.framework.util;

import com.nleachdev.noveildi.framework.annotation.*;
import com.nleachdev.noveildi.framework.exception.AmbiguousConstructorException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

public final class BeanUtils {
    private static final String AMBIGUOUS_EXCEPTION_MSG = "Multiple constructors targeted for injection point in class: %s";

    private BeanUtils() {
    }

    public static boolean isConfigurationBean(final Class<?> clazz) {
        return clazz.isAnnotationPresent(Config.class);
    }

    public static boolean isComponentBean(final Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class) && !clazz.isAnnotationPresent(Config.class);
    }

    public static String getBeanName(final Class<?> clazz) {
        final Component component = clazz.getAnnotation(Component.class);
        final String qualifiedName = component.name();
        return qualifiedName.isEmpty()
                ? clazz.getSimpleName()
                : qualifiedName;
    }

    public static String getBeanName(final Method method) {
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

    public static Constructor<?> getBeanConstructor(final Class<?> clazz) {
        final Constructor<?>[] constructors = Stream.of(clazz.getConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                .toArray(Constructor[]::new);

        if (constructors.length > 1) {
            throw new AmbiguousConstructorException(String.format(AMBIGUOUS_EXCEPTION_MSG, clazz.getSimpleName()));
        }

        return constructors.length == 1
                ? constructors[0]
                : null;
    }
}
