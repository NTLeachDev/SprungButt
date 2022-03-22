package com.nleachdev.noveildi.framework.core;

import com.nleachdev.noveildi.framework.exception.ClassScanningException;
import com.nleachdev.noveildi.framework.model.ContainerConfiguration;
import com.nleachdev.noveildi.framework.model.ScannablePackage;
import com.nleachdev.noveildi.framework.model.ScanningTarget;
import com.nleachdev.noveildi.framework.service.ClassScanner;
import com.nleachdev.noveildi.framework.service.DirectoryClassScanner;
import com.nleachdev.noveildi.framework.service.JarFileClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public enum Container {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(Container.class);
    private static ContainerConfiguration config;

    public static Container getInstance() {
        return INSTANCE;
    }

    public void startContainer(final ContainerConfiguration providedConfig) {
        config = providedConfig;
        final Set<Class<?>> relevantClasses = getRelevantClasses();
        relevantClasses.forEach(clazz -> logger.info("Class: {}", clazz));
    }

    private Set<Class<?>> getRelevantClasses() {
        final ScannablePackage scannablePackage = config.getScannablePackage();
        final ClassScanner scanner = getClassScanner(scannablePackage.getPackageType());
        if (scanner == null) {
            throw new ClassScanningException(String.format("Unable to setup ClassScanner instance for scannable package: %s", scannablePackage));
        }
        return scanner.getClasses(
                scannablePackage.getPackageName(),
                config.getAvailablePackages()
        );
    }

    private ClassScanner getClassScanner(final ScanningTarget scanningTarget) {
        switch (scanningTarget) {
            case DIRECTORY:
                return new DirectoryClassScanner();
            case JAR:
                return new JarFileClassScanner();
            case UNKNOWN:
                return null;
        }

        return null;
    }
}
