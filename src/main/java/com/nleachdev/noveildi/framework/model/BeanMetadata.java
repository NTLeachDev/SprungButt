package com.nleachdev.noveildi.framework.model;

import com.nleachdev.noveildi.framework.exception.BeanInstantiationException;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.StringJoiner;

public class BeanMetadata extends Metadata {
    private final InjectionPoint injectionPoint;

    public BeanMetadata(final Class<?> type, final String beanName, final InjectionPoint injectionPoint) {
        super(type, beanName, BeanType.COMPONENT);
        this.injectionPoint = injectionPoint;
    }

    protected BeanMetadata(final Class<?> type, final String beanName, final InjectionPoint injectionPoint,
                           final BeanType beanType) {
        super(type, beanName, beanType);
        this.injectionPoint = injectionPoint;
    }

    @Override
    protected Object createInstance(final Object instance, final Object... args) throws BeanInstantiationException {
        try {
            return injectionPoint.getConstructor().newInstance(args);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new BeanInstantiationException(e.getMessage(), e);
        }
    }

    @Override
    public Dependency[] getDependencies() {
        return injectionPoint.getDependencies();
    }

    public InjectionPoint getInjectionPoint() {
        return injectionPoint;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeanMetadata)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final BeanMetadata metadata = (BeanMetadata) o;
        return Objects.equals(injectionPoint, metadata.injectionPoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), injectionPoint);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BeanMetadata.class.getSimpleName() + "[", "]")
                .add("injectionPoint=" + injectionPoint)
                .add("type=" + type)
                .add("beanName='" + beanName + "'")
                .add("beanType=" + beanType)
                .add("dependencyCost=" + dependencyCost)
                .add("instance=" + instance)
                .add("interfaces=" + interfaces)
                .add("dependencyMetadata=" + dependencyMetadata)
                .toString();
    }
}