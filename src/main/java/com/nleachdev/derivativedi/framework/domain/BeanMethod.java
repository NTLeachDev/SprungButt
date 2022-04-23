package com.nleachdev.derivativedi.framework.domain;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class BeanMethod<T> {
    private final String methodName;
    private final Method method;
    private final Class<T> returnType;
    private final Dependency[] dependencies;

    public BeanMethod(final String methodName, final Method method, final Class<T> returnType, final Dependency[] dependencies) {
        this.methodName = methodName;
        this.method = method;
        this.returnType = returnType;
        this.dependencies = dependencies;
    }

    public String getMethodName() {
        return methodName;
    }

    public Method getMethod() {
        return method;
    }

    public Class<T> getReturnType() {
        return returnType;
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
        final BeanMethod<?> that = (BeanMethod<?>) o;
        return Objects.equals(methodName, that.methodName) &&
                Objects.equals(method, that.method) &&
                Objects.equals(returnType, that.returnType) &&
                Arrays.equals(dependencies, that.dependencies);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(methodName, method, returnType);
        result = 31 * result + Arrays.hashCode(dependencies);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BeanMethod.class.getSimpleName() + "[", "]")
                .add("methodName='" + methodName + "'")
                .add("instance=" + method)
                .add("returnType=" + returnType)
                .add("dependencies=" + Arrays.toString(dependencies))
                .toString();
    }
}
