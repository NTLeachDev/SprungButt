package com.nleachdev.derivativedi.framework.domain.metadata;

import com.nleachdev.derivativedi.framework.domain.BeanType;
import com.nleachdev.derivativedi.framework.domain.Dependency;
import com.nleachdev.derivativedi.framework.exception.BeanInstantiationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public class ConfiguredBeanMetadata<T> extends Metadata<T> {
    protected final Method method;
    protected Object parentInstance;
    protected Dependency parent;

    public ConfiguredBeanMetadata(final Class<T> type, final String beanName, final Dependency[] dependencies,
                                  final Set<Class<?>> interfaces, final Method method, final Dependency parent) {
        super(type, BeanType.CONFIGURED_METHOD_BEAN, beanName, dependencies, interfaces);
        this.method = method;
        this.parent = parent;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T getInstance(final Object... args) {
        try {
            return (T) (args == null || args.length == 0
                    ? this.method.invoke(this.parentInstance)
                    : this.method.invoke(this.parentInstance, args));
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new BeanInstantiationException(e);
        }
    }

    public Method getMethod() {
        return this.method;
    }

    public Object getParentInstance() {
        return this.parentInstance;
    }

    public void setParentInstance(final Object parentInstance) {
        this.parentInstance = parentInstance;
    }

    public Dependency getParent() {
        return this.parent;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final ConfiguredBeanMetadata<?> that = (ConfiguredBeanMetadata<?>) o;
        return Objects.equals(this.method, that.method) && Objects.equals(this.parentInstance, that.parentInstance) && Objects.equals(this.parent, that.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.method, this.parentInstance, this.parent);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConfiguredBeanMetadata.class.getSimpleName() + "[", "]")
                .add("method=" + this.method)
                .add("parentInstance=" + this.parentInstance)
                .add("parent=" + this.parent)
                .toString();
    }
}
