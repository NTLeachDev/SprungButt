package com.nleachdev.noveildi.framework.exception;

public class MissingBeanDefinitionException extends RuntimeException {
    private static final long serialVersionUID = -7983663341380032259L;

    public MissingBeanDefinitionException(final String message) {
        super(message);
    }

    public MissingBeanDefinitionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
