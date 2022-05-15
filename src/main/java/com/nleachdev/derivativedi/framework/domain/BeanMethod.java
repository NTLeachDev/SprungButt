package com.nleachdev.derivativedi.framework.domain;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class BeanMethod<T> {
    private final String beanName;
    private final Method method;
    private final Class<T> returnType;
    private final Dependency[] dependencies;

    public BeanMethod(final String beanName, final Method method, final Class<T> returnType, final Dependency[] dependencies) {
        this.beanName = beanName;
        this.method = method;
        this.returnType = returnType;
        this.dependencies = dependencies;
    }

    public String getBeanName() {
        return this.beanName;
    }

    public Method getMethod() {
        return this.method;
    }

    public Class<T> getReturnType() {
        return this.returnType;
    }

    public Dependency[] getDependencies() {
        return this.dependencies;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BeanMethod<?> that = (BeanMethod<?>) o;
        return Objects.equals(this.beanName, that.beanName) &&
                Objects.equals(this.method, that.method) &&
                Objects.equals(this.returnType, that.returnType) &&
                Arrays.equals(this.dependencies, that.dependencies);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(this.beanName, this.method, this.returnType);
        result = 31 * result + Arrays.hashCode(this.dependencies);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BeanMethod.class.getSimpleName() + "[", "]")
                .add("methodName='" + this.beanName + "'")
                .add("instance=" + this.method)
                .add("returnType=" + this.returnType)
                .add("dependencies=" + Arrays.toString(this.dependencies))
                .toString();
    }
}
