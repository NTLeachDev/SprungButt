package com.nleachdev.derivativedi.framework.exception;

public class MultipleBeanDefinitionException extends RuntimeException {
    private static final long serialVersionUID = -9059965170857725616L;

    public MultipleBeanDefinitionException(final String message) {
        super(message);
    }

    public MultipleBeanDefinitionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
