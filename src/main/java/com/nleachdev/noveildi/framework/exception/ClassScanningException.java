package com.nleachdev.noveildi.framework.exception;

public class ClassScanningException extends RuntimeException {
    private static final long serialVersionUID = -1106904348514383306L;

    public ClassScanningException() {
    }

    public ClassScanningException(final String message) {
        super(message);
    }

    public ClassScanningException(final Throwable cause) {
        super(cause);
    }

    public ClassScanningException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
