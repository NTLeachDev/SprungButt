package com.nleachdev.derivativedi.framework.scanner;

import com.nleachdev.derivativedi.framework.config.PackageResolver;
import com.nleachdev.derivativedi.framework.exception.ClassScanningException;

import java.util.Set;

public class BaseClassResolver implements ClassResolver {
    private static final String CLASS_SCANNING_EXCEPTION_MSG = "Unable to setup ClassScanner instance for scannable package: %s";
    private final Class<?> mainClazz;
    private final Set<String> availablePackages;
    private final PackageResolver packageResolver;

    public BaseClassResolver(final Class<?> mainClazz, final Set<String> availablePackages, final PackageResolver packageResolver) {
        this.mainClazz = mainClazz;
        this.availablePackages = availablePackages;
        this.packageResolver = packageResolver;
    }

    @Override
    public Set<Class<?>> getRelevantClasses() {
        final ScannablePackage scannablePackage = this.packageResolver.getPackage(this.mainClazz);
        final ClassScanner scanner = this.getClassScanner(scannablePackage.getPackageType());
        if (scanner == null) {
            throw new ClassScanningException(String.format(CLASS_SCANNING_EXCEPTION_MSG, scannablePackage));
        }
        return scanner.getClasses(
                scannablePackage.getPackageName(),
                this.availablePackages
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
