package com.nleachdev.sprungbutt.framework;

import com.nleachdev.sprungbutt.annotation.GetProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.stream.Stream;

public class PropertyInjection {
    private static final Logger logger = LoggerFactory.getLogger(PropertyInjection.class);

    public static void handleFieldPropertyInjection(final Class<?> clazz, final Object clazzInstance) {
        Stream.of(clazz.getDeclaredFields()).forEach(field ->
                PropertyInjection.injectPropertyIfApplicable(clazzInstance, field)
        );
    }

    private static void injectPropertyIfApplicable(final Object instance, final Field field) {
        if (!availableForPropertyInjection(field)) {
            return;
        }

        final Object valueForField = getPropertyValueForField(field);

        field.setAccessible(true);
        try {
            field.set(instance, valueForField);
        } catch (final IllegalAccessException e) {
            logger.error("Error injecting field value", e);
        }
    }

    private static Object getPropertyValueForField(final Field field) {
        final String[] str = field.getAnnotation(GetProperty.class).value().split(":");
        final String propKey = str[0];

        final String valForField;
        String defaultValue = null;

        if (str.length == 2) {
            defaultValue = str[1];
        }

        try (final InputStream input = SprungButt.class.getClassLoader().getResourceAsStream("application.properties")) {
            final Properties properties = new Properties();
            if (input == null) {
                logger.warn("Unable to find application.properties");
                return null;
            }

            properties.load(input);
            valForField = properties.getProperty(propKey);
            final String strValue = valForField == null
                    ? defaultValue
                    : valForField;

            return castToFieldType(field.getType(), strValue);
        } catch (final IOException e) {
            logger.error("Error reading from properties file", e);
        }

        return null;
    }

    private static boolean availableForPropertyInjection(final Field field) {
        return field.getAnnotation(GetProperty.class) != null;
    }

    private static Object castToFieldType(final Class<?> fieldType, final String value) {
        if (fieldType == int.class || fieldType == Integer.class) {
            return Integer.parseInt(value);
        }

        if (fieldType == long.class || fieldType == Long.class) {
            return Long.parseLong(value);
        }

        if (fieldType == boolean.class || fieldType == Boolean.class) {
            return Boolean.parseBoolean(value);
        }

        if (fieldType == String.class) {
            return value;
        }

        return null;
    }
}
