package com.nleachdev.derivativedi.framework.exception;

public class AmbiguousConstructorException extends RuntimeException {
    private static final long serialVersionUID = -4538127071933368819L;

    public AmbiguousConstructorException(final String message) {
        super(message);
    }

    public AmbiguousConstructorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AmbiguousConstructorException(final Throwable cause) {
        super(cause);
    }
}
