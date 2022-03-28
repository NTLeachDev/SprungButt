package com.nleachdev.noveildi.framework.model;

import com.nleachdev.noveildi.framework.exception.BeanInstantiationException;

import java.util.*;

public abstract class Metadata implements Comparable<Metadata> {
    protected final Class<?> type;
    protected final String beanName;
    protected final BeanType beanType;
    protected int dependencyCost;
    protected Object instance;
    protected final Set<Class<?>> interfaces;
    protected Set<Metadata> dependencyMetadata;
    protected boolean isProxyTarget;

    public Metadata(final Class<?> type, final String beanName, final BeanType beanType) {
        this.type = type;
        this.beanName = beanName;
        this.beanType = beanType;
        interfaces = getInterfaces(type);
    }

    protected abstract Object createInstance(final Object instance, final Object... args) throws BeanInstantiationException;

    public abstract Dependency[] getDependencies();

    private static Set<Class<?>> getInterfaces(final Class<?> clazz) {
        return new HashSet<>(Arrays.asList(clazz.getInterfaces()));
    }

    @Override
    public int compareTo(final Metadata obj) {
        return Integer.compare(dependencyCost, obj.dependencyCost);
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

    public int getDependencyCost() {
        return dependencyCost;
    }

    public Object getInstance() {
        return instance;
    }

    public Set<Class<?>> getInterfaces() {
        return interfaces;
    }

    public Set<Metadata> getDependencyMetadata() {
        return dependencyMetadata;
    }

    public void setDependencyCost(final int dependencyCost) {
        this.dependencyCost = dependencyCost;
    }

    public void setInstance(final Object instance) {
        this.instance = instance;
    }

    public void setDependencyMetadata(final Set<Metadata> dependencyMetadata) {
        this.dependencyMetadata = dependencyMetadata;
    }

    public boolean isProxyTarget() {
        return isProxyTarget;
    }

    public void setProxyTarget(final boolean proxyTarget) {
        isProxyTarget = proxyTarget;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Metadata)) {
            return false;
        }
        final Metadata metadata = (Metadata) o;
        return dependencyCost == metadata.dependencyCost &&
                Objects.equals(type, metadata.type) &&
                Objects.equals(beanName, metadata.beanName) &&
                beanType == metadata.beanType &&
                Objects.equals(instance, metadata.instance) &&
                Objects.equals(interfaces, metadata.interfaces) &&
                Objects.equals(dependencyMetadata, metadata.dependencyMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, beanName, beanType, dependencyCost, instance, interfaces, dependencyMetadata);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Metadata.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("beanName='" + beanName + "'")
                .add("beanType=" + beanType)
                .add("dependencyCost=" + dependencyCost)
                .add("instance=" + instance)
                .add("interfaces=" + interfaces)
                .add("dependencyMetadata=" + dependencyMetadata)
                .add("isProxyTarget=" + isProxyTarget)
                .toString();
    }
}
