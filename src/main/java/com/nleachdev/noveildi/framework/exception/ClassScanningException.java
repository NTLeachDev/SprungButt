package com.nleachdev.noveildi.framework.exception;

public class ClassScanningException extends RuntimeException {


    public ClassScanningException() {
    }

    public ClassScanningException(String message) {
        super(message);
    }

    public ClassScanningException(Throwable cause) {
        super(cause);
    }

    public ClassScanningException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
