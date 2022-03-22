package com.nleachdev.noveildi.framework.service;

import com.nleachdev.noveildi.framework.model.ScannablePackage;
import com.nleachdev.noveildi.framework.model.ScanningTarget;
import com.nleachdev.noveildi.framework.util.FileUtils;

import java.io.File;

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
