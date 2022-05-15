package com.nleachdev.derivativedi.framework.domain.metadata;

import com.nleachdev.derivativedi.framework.exception.BeanInstantiationException;
import com.nleachdev.derivativedi.framework.domain.BeanType;
import com.nleachdev.derivativedi.framework.domain.Dependency;

import java.util.Objects;
import java.util.StringJoiner;

public class PropertyMetadataa<T> extends Metadataa<T> {
    private final String propertyKey;

    public PropertyMetadataa(final Class<T> type, final String beanName, final String propertyKey, final T propertyValue) {
        super(type, beanName, BeanType.CONFIGURED_PROPERTY);
        this.propertyKey = propertyKey;
        dependencyCost = 0;
        instance = propertyValue;
        dependencyMetadata = new Metadataa[0];
    }

    @Override
    public void createAndSetInstance(final Object... args) throws BeanInstantiationException {
        throw new BeanInstantiationException("Should not be trying to call createInstance on PropertyMetadata type");
    }

    @Override
    public Dependency[] getDependencies() {
        return new Dependency[0];
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PropertyMetadataa)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final PropertyMetadataa<?> that = (PropertyMetadataa<?>) o;
        return Objects.equals(propertyKey, that.propertyKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), propertyKey);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PropertyMetadataa.class.getSimpleName() + "[", "]")
                .add("propertyKey='" + propertyKey + "'")
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
