package com.nleachdev.noveildi.framework.model;

import com.nleachdev.noveildi.framework.exception.ClassScanningException;
import com.nleachdev.noveildi.framework.util.BeanUtils;
import com.nleachdev.noveildi.framework.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarFileClassScanner implements ClassScanner {
    private static final Logger logger = LoggerFactory.getLogger(JarFileClassScanner.class);
    private static final String JAR_CONVERSION_ERROR_MSG = "Error attempting to convert JarEntry to class";
    private static final String JAR_INSTANTIATION_ERROR_MSG = "Error constructing jar instance";

    @Override
    public Set<Class<?>> getClasses(final String rootDirectory, final Set<String> availablePackages) throws ClassScanningException {
        final Set<Class<?>> scannedClasses = new HashSet<>();

        try {
            final JarFile jar = new JarFile(rootDirectory);
            jar.stream().forEach(jarEntry -> {
                if (jarEntryInAvailablePackages(jarEntry, availablePackages) && entryIsClass(jarEntry)) {
                    final Class<?> clazz = getClassFromEntry(jarEntry);
                    if (clazz != null) {
                        scannedClasses.add(clazz);
                    }
                }
            });
        } catch (final IOException e) {
            logger.warn(JAR_INSTANTIATION_ERROR_MSG, e);
        }

        return scannedClasses;
    }

    private Class<?> getClassFromEntry(final JarEntry jarEntry) {
        final String className = FileUtils.cleanToPackageFormat(jarEntry.getName());
        try {
            final Class<?> clazz = Class.forName(className);
            return BeanUtils.classIsConcreteComponentType(clazz)
                    ? clazz
                    : null;
        } catch (final ClassNotFoundException e) {
            throw new ClassScanningException(JAR_CONVERSION_ERROR_MSG, e);
        }
    }

    private boolean entryIsClass(final JarEntry entry) {
        return entry.getName().endsWith(FileUtils.JAVA_CLASS_EXTENSION);
    }

    private boolean jarEntryInAvailablePackages(final JarEntry jarEntry, final Set<String> availablePackages) {
        return availablePackages.stream()
                .anyMatch(availablePackage ->
                        jarEntry.getName().startsWith(FileUtils.cleanToDirectoryFormat(availablePackage))
                );
    }
}
