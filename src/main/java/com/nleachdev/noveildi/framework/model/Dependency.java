package com.nleachdev.noveildi.framework.model;

import java.util.Objects;
import java.util.StringJoiner;

public class Dependency {
    private final Class<?> type;
    private final String name;
    private final String propertyValue;

    public Dependency(Class<?> type, String name, String propertyValue) {
        this.type = type;
        this.name = name;
        this.propertyValue = propertyValue;
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency that = (Dependency) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(name, that.name) &&
                Objects.equals(propertyValue, that.propertyValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, propertyValue);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Dependency.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("name='" + name + "'")
                .add("propertyValue='" + propertyValue + "'")
                .toString();
    }
}
