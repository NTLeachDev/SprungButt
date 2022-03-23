package com.nleachdev.noveildi.framework.model;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class BeanMethod {
    private final String methodName;
    private final Method instance;
    private final Dependency[] dependencies;

    public BeanMethod(final String methodName, final Method instance, final Dependency[] dependencies) {
        this.methodName = methodName;
        this.instance = instance;
        this.dependencies = dependencies;
    }

    public String getMethodName() {
        return methodName;
    }

    public Method getInstance() {
        return instance;
    }

    public Dependency[] getDependencies() {
        return dependencies;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BeanMethod that = (BeanMethod) o;
        return Objects.equals(methodName, that.methodName) &&
                Objects.equals(instance, that.instance) &&
                Arrays.equals(dependencies, that.dependencies);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(methodName, instance);
        result = 31 * result + Arrays.hashCode(dependencies);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BeanMethod.class.getSimpleName() + "[", "]")
                .add("methodName='" + methodName + "'")
                .add("instance=" + instance)
                .add("dependencies=" + Arrays.toString(dependencies))
                .toString();
    }
}
