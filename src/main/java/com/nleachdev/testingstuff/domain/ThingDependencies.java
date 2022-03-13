package com.nleachdev.testingstuff.domain;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class ThingDependencies {
    private final List<Class<?>> dependencies;
    private final int dependencyCost;

    public ThingDependencies(final List<Class<?>> dependencies, final int dependencyCost) {
        this.dependencies = dependencies;
        this.dependencyCost = dependencyCost;
    }

    public List<Class<?>> getDependencies() {
        return dependencies;
    }

    public int getDependencyCost() {
        return dependencyCost;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ThingDependencies that = (ThingDependencies) o;
        return dependencyCost == that.dependencyCost &&
                Objects.equals(dependencies, that.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dependencies, dependencyCost);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ThingDependencies.class.getSimpleName() + "[", "]")
                .add("dependencies=" + dependencies)
                .add("dependencyCost=" + dependencyCost)
                .toString();
    }
}
