package com.nleachdev.noveildi.framework.model;

import java.util.Comparator;
import java.util.Objects;
import java.util.StringJoiner;

public class BeanMetadata implements Comparator<BeanMetadata> {
    private final Class<?> type;
    private final String beanName;
    private final BeanType beanType;
    private final InjectionPoint injectionPoint;
    private int dependencyCost;
    private Object instance;

    public BeanMetadata(final Class<?> type, final String beanName, final BeanType beanType, final InjectionPoint injectionPoint) {
        this.type = type;
        this.beanName = beanName;
        this.beanType = beanType;
        this.injectionPoint = injectionPoint;
    }

    @Override
    public int compare(final BeanMetadata o1, final BeanMetadata o2) {
        return Integer.compare(o1.dependencyCost, o2.dependencyCost);
    }

    public Class<?> getType() {
        return type;
    }

    public String getBeanName() {
        return beanName;
    }

    public BeanType getBeanType() {
        return beanType;
    }

    public InjectionPoint getInjectionPoint() {
        return injectionPoint;
    }

    public int getDependencyCost() {
        return dependencyCost;
    }

    public Object getInstance() {
        return instance;
    }

    public void setDependencyCost(final int dependencyCost) {
        this.dependencyCost = dependencyCost;
    }

    public void setInstance(final Object instance) {
        this.instance = instance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BeanMetadata that = (BeanMetadata) o;
        return dependencyCost == that.dependencyCost &&
                Objects.equals(type, that.type) &&
                Objects.equals(beanName, that.beanName) &&
                beanType == that.beanType &&
                Objects.equals(injectionPoint, that.injectionPoint) &&
                Objects.equals(instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, beanName, beanType, injectionPoint, dependencyCost, instance);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BeanMetadata.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("beanName='" + beanName + "'")
                .add("beanType=" + beanType)
                .add("injectionPoint=" + injectionPoint)
                .add("dependencyCost=" + dependencyCost)
                .add("instance=" + instance)
                .toString();
    }
}
