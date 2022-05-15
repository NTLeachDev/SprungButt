package com.nleachdev.derivativedi.framework.domain;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.StringJoiner;

public class ConfiguredBean {
    private final Method method;
    private final String parentBeanName;

    public ConfiguredBean(final Method method, final String parentBeanName) {
        this.method = method;
        this.parentBeanName = parentBeanName;
    }

    public Method getMethod() {
        return this.method;
    }

    public String getParentBeanName() {
        return this.parentBeanName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ConfiguredBean that = (ConfiguredBean) o;
        return Objects.equals(this.method, that.method) && Objects.equals(this.parentBeanName, that.parentBeanName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.method, this.parentBeanName);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConfiguredBean.class.getSimpleName() + "[", "]")
                .add("method=" + this.method)
                .add("parentBeanName='" + this.parentBeanName + "'")
                .toString();
    }
}
