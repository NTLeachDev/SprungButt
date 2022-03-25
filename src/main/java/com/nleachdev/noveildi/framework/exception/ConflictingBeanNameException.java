package com.nleachdev.noveildi.framework.exception;

public class ConflictingBeanNameException extends RuntimeException {
    private static final long serialVersionUID = 8362328838180308159L;

    public ConflictingBeanNameException(final String message) {
        super(message);
    }

    public ConflictingBeanNameException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
