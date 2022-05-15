package com.nleachdev.derivativedi.framework.config;

import com.nleachdev.derivativedi.framework.scanner.ScannablePackage;
import com.nleachdev.derivativedi.framework.scanner.ScanningTarget;
import com.nleachdev.derivativedi.framework.util.FileUtils;

import java.io.File;
import java.util.Set;

public class BasePackageResolver implements PackageResolver {

    @Override
    public ScannablePackage getPackage(final Class<?> clazz) {
        final String rootDirectory = getRootDirectory(clazz);
        return new ScannablePackage(rootDirectory, getPackageType(rootDirectory));
    }

    private ScanningTarget getPackageType(final String rootDirectory) {
        final File file = new File(rootDirectory);

        if (file.isDirectory()) {
            return ScanningTarget.DIRECTORY;
        } else if (file.getName().endsWith(FileUtils.JAR_FILE_EXTENSION)) {
            return ScanningTarget.JAR;
        }

        return ScanningTarget.UNKNOWN;
    }

    private static String getRootDirectory(final Class<?> clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
    }
}
