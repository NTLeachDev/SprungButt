package com.nleachdev.noveildi.framework.model;

import java.util.Objects;
import java.util.StringJoiner;

public class Dependency {
    private final Class<?> type;
    private String name;
    private final String propertyValue;
    private final boolean isInterfaceType;

    public Dependency(final Class<?> type, final String name, final String propertyValue, final boolean isInterfaceType) {
        this.type = type;
        this.name = name;
        this.propertyValue = propertyValue;
        this.isInterfaceType = isInterfaceType;
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public boolean isInterfaceType() {
        return isInterfaceType;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Dependency that = (Dependency) o;
        return isInterfaceType == that.isInterfaceType &&
                Objects.equals(type, that.type) &&
                Objects.equals(name, that.name) &&
                Objects.equals(propertyValue, that.propertyValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, propertyValue, isInterfaceType);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Dependency.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("name='" + name + "'")
                .add("propertyValue='" + propertyValue + "'")
                .add("isInterfaceType=" + isInterfaceType)
                .toString();
    }
}
