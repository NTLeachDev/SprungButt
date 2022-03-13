package com.nleachdev.sprungbutt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class ThingScanner {
    private static final Logger logger = LoggerFactory.getLogger(ThingScanner.class);

    public static List<Class<?>> getClasses(final String packageName) throws IOException {
        final Enumeration<URL> resources = getPackageResources(packageName);

        final List<File> directories = new ArrayList<>();
        while (resources.hasMoreElements()) {
            final URL resource = resources.nextElement();
            directories.add(new File(resource.getFile()));
        }

        return directories.stream()
                .map(directory -> getClasses(directory, packageName))
                .flatMap(List::stream)
                .collect(toList());
    }

    private static List<Class<?>> getClasses(final File directory, final String packageName) {
        if (!directory.exists()) {
            return new ArrayList<>();
        }

        return Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                .map(file -> {
                    if (file.isDirectory()) {
                        return getClassesFromSubDirectory(file, packageName);
                    }

                    if (file.getName().endsWith(".class")) {
                        return getClassFromFile(file, packageName);
                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(toList());
    }

    private static List<Class<?>> getClassFromFile(final File file, final String packageName) {
        final String className = packageName + "." + file.getName().substring(0, file.getName().length() - ".class".length());
        try {
            return Collections.singletonList(Class.forName(className));
        } catch (final ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private static List<Class<?>> getClassesFromSubDirectory(final File file, final String packageName) {
        if (file.getName().contains(".")) {
            return new ArrayList<>();
        }

        return getClasses(file, packageName + "." + file.getName());
    }

    private static Enumeration<URL> getPackageResources(final String packageName) throws IOException {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final String path = packageName.replace('.', '/');
        return loader.getResources(path);
    }
}
