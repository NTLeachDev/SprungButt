package com.nleachdev.derivativedi.framework.domain;

public enum BeanType {
    COMPONENT(3),
    CONFIG_COMPONENT(1),
    CONFIGURED_METHOD_BEAN(2),
    CONFIGURED_PROPERTY(0);

    private final int typePriority;

    BeanType(final int typePriority) {
        this.typePriority = typePriority;
    }

    public int getTypePriority() {
        return typePriority;
    }
}
