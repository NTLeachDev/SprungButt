package com.nleachdev.derivativedi.framework.domain.metadata;

import com.nleachdev.derivativedi.framework.exception.BeanInstantiationException;
import com.nleachdev.derivativedi.framework.domain.BeanType;
import com.nleachdev.derivativedi.framework.domain.Dependency;

import java.util.*;

public abstract class Metadataa<T> implements Comparator<Metadataa<?>> {
    public static final Comparator<Metadataa<?>> COMPARATOR = Comparator.comparing(Metadataa::getInstantiationPriority);
    protected final Class<T> type;
    protected final String beanName;
    protected final BeanType beanType;
    protected int dependencyCost;
    protected T instance;
    protected final Set<Class<?>> interfaces;
    protected Metadataa<?>[] dependencyMetadata;

    public Metadataa(final Class<T> type, final String beanName, final BeanType beanType) {
        this.type = type;
        this.beanName = beanName;
        this.beanType = beanType;
        interfaces = getInterfaces(type);
    }

    public abstract void createAndSetInstance(final Object... args) throws BeanInstantiationException;

    public abstract Dependency[] getDependencies();

    private static Set<Class<?>> getInterfaces(final Class<?> clazz) {
        return new HashSet<>(Arrays.asList(clazz.getInterfaces()));
    }

    @Override
    public int compare(final Metadataa o1, final Metadataa o2) {
        return COMPARATOR.compare(o1, o2);
    }

    public Class<T> getType() {
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

    public T getInstance() {
        return instance;
    }

    public Set<Class<?>> getInterfaces() {
        return interfaces;
    }

    public Metadataa[] getDependencyMetadata() {
        return dependencyMetadata;
    }

    public void setDependencyCost(final int dependencyCost) {
        this.dependencyCost = dependencyCost;
    }

    public void setInstance(final T instance) {
        this.instance = instance;
    }

    public void setDependencyMetadata(final Metadataa<?>[] dependencyMetadata) {
        this.dependencyMetadata = dependencyMetadata;
    }

    private int getInstantiationPriority() {
        return dependencyCost + beanType.getTypePriority();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Metadataa)) {
            return false;
        }
        final Metadataa<T> metadataa = (Metadataa<T>) o;
        return dependencyCost == metadataa.dependencyCost &&
                Objects.equals(type, metadataa.type) &&
                Objects.equals(beanName, metadataa.beanName) &&
                beanType == metadataa.beanType &&
                Objects.equals(instance, metadataa.instance) &&
                Objects.equals(interfaces, metadataa.interfaces) &&
                Arrays.equals(dependencyMetadata, metadataa.dependencyMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, beanName, beanType, dependencyCost, instance, interfaces, dependencyMetadata);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Metadataa.class.getSimpleName() + "[", "]")
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
