package com.nleachdev.derivativedi.framework.domain.metadata;

import com.nleachdev.derivativedi.framework.domain.BeanType;
import com.nleachdev.derivativedi.framework.domain.Dependency;

import java.util.*;

public class Metadata<T> {
    public static final Comparator<Metadata<?>> COMPARATOR = Comparator.comparing(Metadata::getInstantiationPriority);
    protected final Class<T> type;
    protected final BeanType beanType;
    protected final String beanName;
    protected final Dependency[] dependencies;
    protected final Set<Class<?>> interfaces;
    protected int dependencyCost;

    protected T getInstance(final Object... args) {
        return null;
    }

    public Metadata(final Class<T> type, final BeanType beanType, final String beanName,
                    final Dependency[] dependencies, final Set<Class<?>> interfaces) {
        this.type = type;
        this.beanType = beanType;
        this.beanName = beanName;
        this.dependencies = dependencies;
        this.interfaces = interfaces;
    }

    public Class<T> getType() {
        return this.type;
    }

    public BeanType getBeanType() {
        return this.beanType;
    }

    public String getBeanName() {
        return this.beanName;
    }

    public Dependency[] getDependencyNames() {
        return this.dependencies;
    }

    public Set<Class<?>> getInterfaces() {
        return this.interfaces;
    }

    public int getDependencyCost() {
        return this.dependencyCost;
    }

    public void setDependencyCost(final int dependencyCost) {
        this.dependencyCost = dependencyCost;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Metadata<?> metadata = (Metadata<?>) o;
        return this.dependencyCost == metadata.dependencyCost && Objects.equals(this.type, metadata.type)
                && this.beanType == metadata.beanType && Objects.equals(this.beanName, metadata.beanName)
                && Arrays.equals(this.dependencies, metadata.dependencies) && Objects.equals(this.interfaces, metadata.interfaces);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(this.type, this.beanType, this.beanName, this.interfaces, this.dependencyCost);
        result = 31 * result + Arrays.hashCode(this.dependencies);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Metadata.class.getSimpleName() + "[", "]")
                .add("type=" + this.type)
                .add("beanType=" + this.beanType)
                .add("beanName='" + this.beanName + "'")
                .add("dependencies=" + Arrays.toString(this.dependencies))
                .add("interfaces=" + this.interfaces)
                .add("dependencyCost=" + this.dependencyCost)
                .toString();
    }

    private int getInstantiationPriority() {
        return this.dependencyCost + this.beanType.getTypePriority();
    }
}
