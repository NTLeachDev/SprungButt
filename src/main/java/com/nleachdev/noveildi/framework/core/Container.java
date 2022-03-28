package com.nleachdev.noveildi.framework.core;

import com.nleachdev.noveildi.framework.exception.ClassScanningException;
import com.nleachdev.noveildi.framework.model.*;
import com.nleachdev.noveildi.framework.model.ClassScanner;
import com.nleachdev.noveildi.framework.model.DirectoryClassScanner;
import com.nleachdev.noveildi.framework.model.JarFileClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum Container {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(Container.class);
    private static final String CLASS_SCANNING_EXCEPTION_MSG = "Unable to setup ClassScanner instance for scannable package: %s";
    private static ContainerConfiguration config;
    private static final Map<BeanType, Set<Metadata>> metadataPerBeanType = new HashMap<>();
    private static final Map<String, Metadata> metadataPerBeanName = new HashMap<>();
    private static final Map<Class<?>, Set<String>> beanNamesPerType = new HashMap<>();

    public static Container getInstance() {
        return INSTANCE;
    }

    public void startContainer(final ContainerConfiguration providedConfig) {
        config = providedConfig;
        clear();
        setupBeanMetadata();
        verifyDependencies();

        metadataPerBeanName.forEach((beanName, metadata) -> {
            logger.info("Metadata for bean with name: {}\n{}\n", beanName, metadata);
        });
    }

    public Object getBeanForType(final Class<?> clazz) {
        final Set<String> namesForType = beanNamesPerType.get(clazz);
        if (namesForType == null || namesForType.size() != 1) {
            // throw exception?
            return null;
        }

        return getBeanForName(((String[]) namesForType.toArray())[0]);
    }

    public Object getBeanForName(final String beanName) {
        final Metadata metadata = metadataPerBeanName.get(beanName);
        if (metadata == null) {
            // throw exception?
            return null;
        }

        return metadata.getInstance();
    }

    private static void setupBeanMetadata() {
        final Set<Class<?>> relevantClasses = getRelevantClasses();
        new ContainerSetup(
                metadataPerBeanType,
                metadataPerBeanName,
                beanNamesPerType
        ).setupBeanMetadata(relevantClasses);
    }

    private static void verifyDependencies() {
        new DependencyVerifier(
                metadataPerBeanName,
                beanNamesPerType
        ).verifyDependencies();
    }

    private static Set<Class<?>> getRelevantClasses() {
        final ScannablePackage scannablePackage = config.getScannablePackage();
        final ClassScanner scanner = getClassScanner(scannablePackage.getPackageType());
        if (scanner == null) {
            throw new ClassScanningException(String.format(CLASS_SCANNING_EXCEPTION_MSG, scannablePackage));
        }
        return scanner.getClasses(
                scannablePackage.getPackageName(),
                config.getAvailablePackages()
        );
    }

    private static ClassScanner getClassScanner(final ScanningTarget scanningTarget) {
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

    private static void clear() {
        metadataPerBeanName.clear();
        metadataPerBeanType.clear();
        beanNamesPerType.clear();
    }

    public ContainerConfiguration getConfig() {
        return config;
    }
}