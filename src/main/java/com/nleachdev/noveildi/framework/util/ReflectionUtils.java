package com.nleachdev.noveildi.framework.util;

import com.nleachdev.noveildi.framework.annotation.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ReflectionUtils {

    private ReflectionUtils() {

    }

    public static boolean classIsConcreteComponentType(final Class<?> clazz) {
        final Set<Class<?>> annotationTypesForClass = Stream.of(clazz.getAnnotations())
                .map(Annotation::annotationType)
                .collect(Collectors.toSet());
        return annotationTypesForClass.contains(Component.class) && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers());
    }
}
