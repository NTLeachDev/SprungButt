package com.nleachdev.noveildi.framework.model;


import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class ConfigBeanMetadata {
    private final BeanMetadata beanMetadata;
    private final BeanMethod[] beanMethods;

    public ConfigBeanMetadata(final BeanMetadata beanMetadata, final BeanMethod[] beanMethods) {
        this.beanMetadata = beanMetadata;
        this.beanMethods = beanMethods;
    }

    public BeanMetadata getBeanMetadata() {
        return beanMetadata;
    }

    public BeanMethod[] getBeanMethods() {
        return beanMethods;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ConfigBeanMetadata that = (ConfigBeanMetadata) o;
        return Objects.equals(beanMetadata, that.beanMetadata) &&
                Arrays.equals(beanMethods, that.beanMethods);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(beanMetadata);
        result = 31 * result + Arrays.hashCode(beanMethods);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConfigBeanMetadata.class.getSimpleName() + "[", "]")
                .add("beanMetadata=" + beanMetadata)
                .add("beanMethods=" + Arrays.toString(beanMethods))
                .toString();
    }
}
