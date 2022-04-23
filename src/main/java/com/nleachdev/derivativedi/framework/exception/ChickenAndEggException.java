package com.nleachdev.derivativedi.framework.exception;

public class ChickenAndEggException extends RuntimeException {
    private static final long serialVersionUID = 1691163374765820319L;

    public ChickenAndEggException(final String message) {
        super(message);
    }

    public ChickenAndEggException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
