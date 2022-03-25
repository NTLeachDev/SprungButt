package com.nleachdev.noveildi.framework.model;

import java.util.*;

public abstract class Metadata implements Comparator<Metadata> {
    protected final Class<?> type;
    protected final String beanName;
    protected final BeanType beanType;
    protected int dependencyCost;
    protected Object instance;
    protected final Set<Class<?>> interfaces;

    public Metadata(final Class<?> type, final String beanName, final BeanType beanType) {
        this.type = type;
        this.beanName = beanName;
        this.beanType = beanType;
        interfaces = getInterfaces(type);
    }

    protected abstract Object createInstance(final Object... args);

    private static Set<Class<?>> getInterfaces(final Class<?> clazz) {
        return new HashSet<>(Arrays.asList(clazz.getInterfaces()));
    }

    @Override
    public int compare(final Metadata o1, final Metadata o2) {
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

    public int getDependencyCost() {
        return dependencyCost;
    }

    public Object getInstance() {
        return instance;
    }

    public Set<Class<?>> getInterfaces() {
        return interfaces;
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
        final Metadata metadata = (Metadata) o;
        return dependencyCost == metadata.dependencyCost &&
                Objects.equals(type, metadata.type) &&
                Objects.equals(beanName, metadata.beanName) &&
                beanType == metadata.beanType &&
                Objects.equals(instance, metadata.instance) &&
                Objects.equals(interfaces, metadata.interfaces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, beanName, beanType, dependencyCost, instance, interfaces);
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
                .toString();
    }
}
