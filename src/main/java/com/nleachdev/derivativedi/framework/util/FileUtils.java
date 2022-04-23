package com.nleachdev.derivativedi.framework.util;

public final class FileUtils {
    public static final String JAVA_CLASS_EXTENSION = ".class";
    public static final String JAR_FILE_EXTENSION = ".jar";

    private FileUtils() {

    }

    public static String cleanToPackageFormat(final String str) {
        return str.replaceAll(JAVA_CLASS_EXTENSION, "")
                .replace("/", ".")
                .replaceAll("\\\\", ".");
    }

    public static String cleanToDirectoryFormat(final String str) {
        return str.replaceAll(JAVA_CLASS_EXTENSION, "")
                .replace(".", "/");
    }
}
