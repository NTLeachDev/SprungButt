package com.nleachdev.derivativedi.framework.scanner;

import com.nleachdev.derivativedi.framework.exception.ClassScanningException;

import java.util.Set;

public interface ClassScanner {
    Set<Class<?>> getClasses(final String rootDirectory, final Set<String> availablePackages) throws ClassScanningException;
}
