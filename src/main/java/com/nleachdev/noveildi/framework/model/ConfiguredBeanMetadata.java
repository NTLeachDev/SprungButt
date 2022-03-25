package com.nleachdev.noveildi.framework.model;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class ConfiguredBeanMetadata extends Metadata {
    private final Method method;
    private final Dependency[] dependencies;

    public ConfiguredBeanMetadata(final Class<?> type, final String beanName, final Method method, final Dependency[] dependencies) {
        super(type, beanName, BeanType.CONFIGURED_METHOD_BEAN);
        this.method = method;
        this.dependencies = dependencies;
    }

    public ConfiguredBeanMetadata(final BeanMethod beanMethod) {
        this(beanMethod.getReturnType(), beanMethod.getMethodName(), beanMethod.getMethod(), beanMethod.getDependencies());
    }

    @Override
    protected Object createInstance(final Object... args) {
        return null;
    }

    public Method getMethod() {
        return method;
    }

    public Dependency[] getDependencies() {
        return dependencies;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfiguredBeanMetadata)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final ConfiguredBeanMetadata that = (ConfiguredBeanMetadata) o;
        return Objects.equals(method, that.method) &&
                Arrays.equals(dependencies, that.dependencies);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), method);
        result = 31 * result + Arrays.hashCode(dependencies);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConfiguredBeanMetadata.class.getSimpleName() + "[", "]")
                .add("method=" + method)
                .add("dependencies=" + Arrays.toString(dependencies))
                .add("type=" + type)
                .add("beanName='" + beanName + "'")
                .add("beanType=" + beanType)
                .add("dependencyCost=" + dependencyCost)
                .add("instance=" + instance)
                .add("interfaces=" + interfaces)
                .toString();
    }
}
