package com.nleachdev.derivativedi.framework.config;

import com.nleachdev.derivativedi.framework.exception.PropertyInjectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

public class BasePropertyResolver implements PropertyResolver {
    private static final Logger logger = LoggerFactory.getLogger(BasePropertyResolver.class);
    private static final String NO_PROPERTY_FOUND_EXCEPTION_MSG = "No property (or default) found for key: %s";
    private static final String FILE_READ_EXCEPTION = "Unable to parse property file with name: %s";
    private final Set<String> propertyFiles;

    public BasePropertyResolver(final Set<String> propertyFiles) {
        this.propertyFiles = propertyFiles;
    }

    @Override
    public Object getValueForProperty(final Class<?> fieldType, final String fullProperty) {
        final String[] str = fullProperty.split(":");
        final String propKey = str[0];
        final String defaultValue = str.length == 2
                ? str[1]
                : null;

        return propertyFiles.stream()
                .map(fileName -> getPropertyValue(fileName, propKey))
                .map(value -> buildFromPropertyValue(fieldType, value, defaultValue))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new PropertyInjectionException(
                        String.format(NO_PROPERTY_FOUND_EXCEPTION_MSG, fullProperty)
                ));
    }

    private String getPropertyValue(final String propertyFile, final String propertyKey) {
        try (final InputStream in = getClass().getClassLoader().getResourceAsStream(propertyFile)) {
            final Properties properties = new Properties();
            if (in == null) {
                logger.debug("Unable to find property file with name {}", propertyFile);
                return null;
            }

            properties.load(in);
            return properties.getProperty(propertyKey);

        } catch (final IOException e) {
            throw new PropertyInjectionException(
                    String.format(FILE_READ_EXCEPTION, propertyFile), e
            );
        }
    }

    private static Object buildFromPropertyValue(final Class<?> type, final String propertyValue, final String defaultValue) {
        final String value = propertyValue == null
                ? defaultValue
                : propertyValue;

        return castToFieldType(type, value);
    }

    private static Object castToFieldType(final Class<?> fieldType, final String value) {
        if (value == null) {
            return null;
        }
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
