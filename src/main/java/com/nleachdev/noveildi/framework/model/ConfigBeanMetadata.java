package com.nleachdev.noveildi.framework.model;


import java.util.Arrays;
import java.util.StringJoiner;

public class ConfigBeanMetadata<T> extends BeanMetadata<T> {
    private final BeanMethod<?>[] beanMethods;

    public ConfigBeanMetadata(final Class<T> type, final String beanName, final InjectionPoint<T> injectionPoint,
                              final BeanMethod<?>[] beanMethods) {
        super(type, beanName, injectionPoint, BeanType.CONFIG_COMPONENT);
        this.beanMethods = beanMethods;
    }

    public BeanMethod<?>[] getBeanMethods() {
        return beanMethods;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigBeanMetadata)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final ConfigBeanMetadata<?> that = (ConfigBeanMetadata<?>) o;
        return Arrays.equals(beanMethods, that.beanMethods);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(beanMethods);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConfigBeanMetadata.class.getSimpleName() + "[", "]")
                .add("beanMethods=" + Arrays.toString(beanMethods))
                .add("type=" + type)
                .add("beanName='" + beanName + "'")
                .add("beanType=" + beanType)
                .add("dependencyCost=" + dependencyCost)
                .add("instance=" + instance)
                .add("interfaces=" + interfaces)
                .toString();
    }
}
