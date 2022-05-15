package com.nleachdev.derivativedi.framework.exception;

public class BeanInstantiationException extends RuntimeException {
    private static final long serialVersionUID = -1589484006560047311L;

    public BeanInstantiationException(final String message) {
        super(message);
    }

    public BeanInstantiationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BeanInstantiationException(final Throwable cause) {
        super(cause);
    }

}
