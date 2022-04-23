package com.nleachdev.derivativedi.framework.core;

import com.nleachdev.derivativedi.framework.domain.*;
import com.nleachdev.derivativedi.framework.domain.metadata.Metadata;
import com.nleachdev.derivativedi.framework.exception.ClassScanningException;
import com.nleachdev.derivativedi.framework.exception.MissingBeanDefinitionException;
import com.nleachdev.derivativedi.framework.exception.MultipleBeanDefinitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.stream.Collectors.toSet;

public enum Container {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(Container.class);
    private static final String CLASS_SCANNING_EXCEPTION_MSG = "Unable to setup ClassScanner instance for scannable package: %s";
    private static final String MISSING_BEAN_DEF_WITH_NAME_EXCEPTION_MSG = "No bean found with specified name %s";
    private static final String MISSING_BEAN_DEF_WITH_TYPE_NAME_EXCEPTION_MSG = "No bean found with specified type %s and name %s";
    private static final String MISSING_BEAN_DEF_WITH_TYPE_EXCEPTION_MSG = "No bean found with specified type %s";
    private static final String MULTIPLE_BEAN_DEF_EXCEPTION_MSG = "Multiple beans of type %s found, please specify the name of the bean as well";
    private static ContainerConfiguration config;
    private static final Map<BeanType, Set<Metadata<?>>> metadataPerBeanType = new HashMap<>();
    private static final Map<String, Metadata<?>> metadataPerBeanName = new HashMap<>();
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
            logger.debug("Metadata for bean with name: {}\n{}\n", beanName, metadata);
        });

        new BeanInstantiation().instantiateBeans(metadataPerBeanName);
    }

    public <T> T getBean(final Class<T> clazz) {
        final Set<String> namesForType = getNamesWithType(clazz);
        if (namesForType == null) {
            throw new MissingBeanDefinitionException(String.format(MISSING_BEAN_DEF_WITH_TYPE_EXCEPTION_MSG, clazz.getSimpleName()));
        } else if (namesForType.size() > 1) {
            throw new MultipleBeanDefinitionException(String.format(MULTIPLE_BEAN_DEF_EXCEPTION_MSG, clazz.getSimpleName()));
        }

        final List<String> list = new ArrayList<>(namesForType);

        return getBean(list.get(0));
    }

    public <T> T getBean(final Class<?> clazz, final String beanName) {
        final Set<String> namesForType = getNamesWithType(clazz);
        if (!namesForType.contains(beanName)) {
            throw new MissingBeanDefinitionException(String.format(MISSING_BEAN_DEF_WITH_TYPE_NAME_EXCEPTION_MSG, clazz.getSimpleName(), beanName));
        }

        return getBean(beanName);
    }

    public <T> T getBean(final String beanName) {
        final Metadata<?> metadata = metadataPerBeanName.get(beanName);
        if (metadata == null) {
            throw new MissingBeanDefinitionException(String.format(MISSING_BEAN_DEF_WITH_NAME_EXCEPTION_MSG, beanName));
        }

        return (T) metadata.getInstance();
    }

    private Set<String> getNamesWithType(final Class<?> clazz) {
        return beanNamesPerType.keySet().stream()
                .filter(clazz::isAssignableFrom)
                .map(beanNamesPerType::get)
                .flatMap(Set::stream)
                .collect(toSet());
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