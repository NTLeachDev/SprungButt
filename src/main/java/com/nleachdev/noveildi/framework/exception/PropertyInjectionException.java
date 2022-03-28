package com.nleachdev.noveildi.framework.exception;

public class PropertyInjectionException extends RuntimeException {
    private static final long serialVersionUID = -5137730335096710631L;

    public PropertyInjectionException(final String message) {
        super(message);
    }

    public PropertyInjectionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
