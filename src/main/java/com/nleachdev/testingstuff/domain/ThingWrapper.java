package com.nleachdev.testingstuff.domain;

import java.util.Objects;
import java.util.StringJoiner;

public class ThingWrapper {
    private final Class<?> type;
    private final ThingDependencies dependencies;
    private final Object obj;

    public ThingWrapper(final Class<?> type, final ThingDependencies dependencies, final Object obj) {
        this.type = type;
        this.dependencies = dependencies;
        this.obj = obj;
    }

    public Class<?> getType() {
        return type;
    }

    public ThingDependencies getDependencies() {
        return dependencies;
    }

    public Object getObj() {
        return obj;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ThingWrapper that = (ThingWrapper) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(dependencies, that.dependencies) &&
                Objects.equals(obj, that.obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, dependencies, obj);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ThingWrapper.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("dependencies=" + dependencies)
                .add("obj=" + obj)
                .toString();
    }
}
