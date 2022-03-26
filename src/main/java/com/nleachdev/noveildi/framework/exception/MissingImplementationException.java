package com.nleachdev.noveildi.framework.exception;

public class MissingImplementationException extends RuntimeException {
    private static final long serialVersionUID = 7964288814109531012L;

    public MissingImplementationException(final String message) {
        super(message);
    }

    public MissingImplementationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
