package com.nleachdev.derivativedi.framework.domain.metadata;

import com.nleachdev.derivativedi.framework.domain.BeanType;
import com.nleachdev.derivativedi.framework.domain.Dependency;
import com.nleachdev.derivativedi.framework.exception.BeanInstantiationException;
import com.nleachdev.derivativedi.framework.domain.BeanMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class ConfiguredBeanMetadata<T> extends Metadata<T> {
    private final Method method;
    private final Dependency[] dependencies;
    private final ConfigBeanMetadata<?> parentConfigMetadata;

    public ConfiguredBeanMetadata(final Class<T> type, final String beanName, final Method method, final Dependency[] dependencies, final ConfigBeanMetadata<?> parentConfigMetadata) {
        super(type, beanName, BeanType.CONFIGURED_METHOD_BEAN);
        this.method = method;
        this.dependencies = dependencies;
        this.parentConfigMetadata = parentConfigMetadata;
    }

    public ConfiguredBeanMetadata(final BeanMethod<T> beanMethod, final ConfigBeanMetadata<?> parentConfigMetadata) {
        this(beanMethod.getReturnType(), beanMethod.getMethodName(), beanMethod.getMethod(), beanMethod.getDependencies(), parentConfigMetadata);
    }

    @Override
    public void createAndSetInstance(final Object... args) throws BeanInstantiationException {
        try {
            instance = (T) (args == null || args.length == 0
                    ? method.invoke(parentConfigMetadata.getInstance())
                    : method.invoke(parentConfigMetadata.getInstance(), args));
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new BeanInstantiationException(e.getMessage(), e);
        }
    }

    @Override
    public Dependency[] getDependencies() {
        return dependencies;
    }

    public Method getMethod() {
        return method;
    }

    public ConfigBeanMetadata<?> getParentConfigMetadata() {
        return parentConfigMetadata;
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
        final ConfiguredBeanMetadata<?> that = (ConfiguredBeanMetadata<?>) o;
        return Objects.equals(method, that.method) &&
                Arrays.equals(dependencies, that.dependencies) &&
                Objects.equals(parentConfigMetadata, that.parentConfigMetadata);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), method, parentConfigMetadata);
        result = 31 * result + Arrays.hashCode(dependencies);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConfiguredBeanMetadata.class.getSimpleName() + "[", "]")
                .add("method=" + method)
                .add("dependencies=" + Arrays.toString(dependencies))
                .add("parentConfigMetadata=" + parentConfigMetadata)
                .add("type=" + type)
                .add("beanName='" + beanName + "'")
                .add("beanType=" + beanType)
                .add("dependencyCost=" + dependencyCost)
                .add("instance=" + instance)
                .add("interfaces=" + interfaces)
                .add("dependencyMetadata=" + Arrays.toString(dependencyMetadata))
                .toString();
    }
}
