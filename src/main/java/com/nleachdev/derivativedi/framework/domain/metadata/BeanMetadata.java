package com.nleachdev.derivativedi.framework.domain.metadata;

import com.nleachdev.derivativedi.framework.domain.BeanType;
import com.nleachdev.derivativedi.framework.exception.BeanInstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public class BeanMetadata<T> extends Metadata<T> {
    protected final Constructor<T> constructor;

    public BeanMetadata(final Class<T> type, final BeanType beanType, final String beanName,
                        final String[] dependencyNames, final Set<Class<?>> interfaces,
                        final Constructor<T> constructor) {
        super(type, beanType, beanName, dependencyNames, interfaces);
        this.constructor = constructor;
    }

    @Override
    protected T getInstance(final Object... args) {
        try {
            return args == null || args.length == 0
                    ? this.type.newInstance()
                    : this.constructor.newInstance(args);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new BeanInstantiationException(e);
        }
    }

    public Constructor<T> getConstructor() {
        return this.constructor;
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
        final BeanMetadata<?> that = (BeanMetadata<?>) o;
        return Objects.equals(this.constructor, that.constructor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.constructor);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BeanMetadata.class.getSimpleName() + "[", "]")
                .add("constructor=" + this.constructor)
                .toString();
    }
}
