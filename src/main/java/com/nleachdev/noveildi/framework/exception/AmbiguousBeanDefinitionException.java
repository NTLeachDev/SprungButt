package com.nleachdev.noveildi.framework.exception;

public class AmbiguousBeanDefinitionException extends RuntimeException {
    private static final long serialVersionUID = 5106437220942974044L;

    public AmbiguousBeanDefinitionException(final String message) {
        super(message);
    }

    public AmbiguousBeanDefinitionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
