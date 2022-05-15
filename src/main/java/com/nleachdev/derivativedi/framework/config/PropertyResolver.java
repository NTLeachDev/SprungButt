package com.nleachdev.derivativedi.framework.config;

public interface PropertyResolver {

    Object getValueForProperty(final Class<?> valueType, final String property);
}
