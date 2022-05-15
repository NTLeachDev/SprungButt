package com.nleachdev.derivativedi.framework.domain.metadata;

import com.nleachdev.derivativedi.framework.domain.BeanType;
import com.nleachdev.derivativedi.framework.domain.Dependency;
import com.nleachdev.derivativedi.framework.domain.InjectionPoint;
import com.nleachdev.derivativedi.framework.exception.BeanInstantiationException;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.StringJoiner;

public class BeanMetadataa<T> extends Metadataa<T> {
    private static final String BEAN_INSTANTIATION_EXCEPTION_MSG = "Unable to instantiate bean with name %s";
    private final InjectionPoint<T> injectionPoint;

    public BeanMetadataa(final Class<T> type, final String beanName, final InjectionPoint<T> injectionPoint) {
        super(type, beanName, BeanType.COMPONENT);
        this.injectionPoint = injectionPoint;
    }

    protected BeanMetadataa(final Class<T> type, final String beanName, final InjectionPoint<T> injectionPoint,
                            final BeanType beanType) {
        super(type, beanName, beanType);
        this.injectionPoint = injectionPoint;
    }

    @Override
    public void createAndSetInstance(final Object[] args) throws BeanInstantiationException {
        try {
            instance = args == null || args.length == 0
                    ? type.newInstance()
                    : injectionPoint.getConstructor().newInstance(args);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new BeanInstantiationException(
                    String.format(BEAN_INSTANTIATION_EXCEPTION_MSG, beanName),
                    e
            );
        }
    }

    @Override
    public Dependency[] getDependencies() {
        return injectionPoint.getDependencies();
    }

    public InjectionPoint<T> getInjectionPoint() {
        return injectionPoint;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeanMetadataa)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final BeanMetadataa<?> metadata = (BeanMetadataa<?>) o;
        return Objects.equals(injectionPoint, metadata.injectionPoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), injectionPoint);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BeanMetadataa.class.getSimpleName() + "[", "]")
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