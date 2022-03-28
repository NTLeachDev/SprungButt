package com.nleachdev.noveildi.framework.model;

import java.util.Set;

public interface PropertyResolver {

    Object getValueForProperty(final Class<?> valueType, final String property);
}
