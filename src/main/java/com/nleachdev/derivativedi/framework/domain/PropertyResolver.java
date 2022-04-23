package com.nleachdev.derivativedi.framework.domain;

public interface PropertyResolver {

    Object getValueForProperty(final Class<?> valueType, final String property);
}
