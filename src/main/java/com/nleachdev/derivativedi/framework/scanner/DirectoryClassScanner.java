package com.nleachdev.derivativedi.framework.scanner;

import com.nleachdev.derivativedi.framework.exception.ClassScanningException;
import com.nleachdev.derivativedi.framework.util.BeanUtils;
import com.nleachdev.derivativedi.framework.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DirectoryClassScanner implements ClassScanner {
    private static final Logger logger = LoggerFactory.getLogger(DirectoryClassScanner.class);

    @Override
    public Set<Class<?>> getClasses(final String rootDirectory, final Set<String> availablePackages) throws ClassScanningException {
        final Set<Class<?>> scannedClasses = new HashSet<>();
        for (final File innerDirectory : Objects.requireNonNull(new File(rootDirectory).listFiles())) {
            scannedClasses.addAll(getClassesFromDirectory(innerDirectory, ""));
        }
        return scannedClasses;
    }

    private Set<Class<?>> getClassesFromDirectory(final File directory, String resolvedPath) {
        final Set<Class<?>> classes = new HashSet<>();
        resolvedPath += directory.getName() + ".";
        for (final File innerFile : Objects.requireNonNull(directory.listFiles())) {
            if (innerFile.isDirectory()) {
                classes.addAll(getClassesFromDirectory(innerFile, resolvedPath));
                continue;
            }

            final Class<?> clazz = getClassFromFile(innerFile, resolvedPath);
            if (clazz != null) {
                classes.add(clazz);
            }
        }

        return classes;
    }

    private Class<?> getClassFromFile(final File file, final String resolvedPath) {
        try {
            if (file.getName().endsWith(FileUtils.JAVA_CLASS_EXTENSION)) {
                final String className = FileUtils.cleanToPackageFormat(resolvedPath + file.getName());
                final Class<?> clazz = Class.forName(className);
                if (BeanUtils.classIsConcreteComponentType(clazz)) {
                    return clazz;
                }
            }
            return null;
        } catch (final ClassNotFoundException e) {
            logger.warn("Could not get class from file", e);
            throw new ClassScanningException(e.getMessage(), e);
        }
    }
}
