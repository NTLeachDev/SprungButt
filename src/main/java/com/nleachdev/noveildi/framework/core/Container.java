package com.nleachdev.noveildi.framework.core;

import com.nleachdev.noveildi.framework.annotation.Bean;
import com.nleachdev.noveildi.framework.annotation.Get;
import com.nleachdev.noveildi.framework.exception.ClassScanningException;
import com.nleachdev.noveildi.framework.model.*;
import com.nleachdev.noveildi.framework.service.ClassScanner;
import com.nleachdev.noveildi.framework.service.DirectoryClassScanner;
import com.nleachdev.noveildi.framework.service.JarFileClassScanner;
import com.nleachdev.noveildi.framework.util.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public enum Container {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(Container.class);

    private static ContainerConfiguration config;
    private final Map<String, BeanMetadata> beanMetadataPerName = new HashMap<>();
    private final Map<String, ConfigBeanMetadata> configBeanMetadataPerName = new HashMap<>();

    public static Container getInstance() {
        return INSTANCE;
    }

    public void startContainer(final ContainerConfiguration providedConfig) {
        config = providedConfig;
        final Set<Class<?>> relevantClasses = getRelevantClasses();
        setupBeanMetadata(relevantClasses);
        relevantClasses.forEach(clazz -> {
            logger.info("Relevant Class: {}", clazz);
        });

        logger.info("\n\n\n");

        beanMetadataPerName.forEach((beanName, beanMetadata) -> {
            logger.info("Name: {}", beanName);
            logger.info("BeanMetadata: {}", beanMetadata);
        });

        logger.info("\n\n\n");

        configBeanMetadataPerName.forEach((beanName, beanMetadata) -> {
            logger.info("Name: {}", beanName);
            logger.info("BeanMetadata: {}", beanMetadata);
        });
    }

    private void setupBeanMetadata(final Set<Class<?>> classes) {
        classes.forEach(this::setupBeanMetadataAndVerify);
    }


    private void setupBeanMetadataAndVerify(final Class<?> clazz) {
        final String beanName = BeanUtils.getBeanName(clazz);
        final Constructor<?> constructor = BeanUtils.getBeanConstructor(clazz);
        if (BeanUtils.isConfigurationBean(clazz)) {
            setupMetadataForConfigType(clazz, beanName, constructor);
        }

        if (BeanUtils.isComponentBean(clazz)) {
            setupMetadataForComponentType(clazz, beanName, constructor);
        }

    }

    private void setupMetadataForComponentType(final Class<?> clazz, final String beanName, final Constructor<?> constructor) {
        final InjectionPoint injectionPoint = getInjectionPoint(constructor);
        final BeanMetadata metadata = new BeanMetadata(clazz, beanName, BeanType.COMPONENT, injectionPoint);
        beanMetadataPerName.put(beanName, metadata);
    }

    private InjectionPoint getInjectionPoint(final Constructor<?> constructor) {
        if (constructor == null || constructor.getParameterCount() == 0) {
            return new InjectionPoint(null, null);
        }

        final Dependency[] dependencies = Stream.of(constructor.getParameters())
                .map(this::getDependencyFromParam)
                .toArray(Dependency[]::new);

        return new InjectionPoint(constructor, dependencies);
    }

    private Dependency getDependencyFromParam(final Parameter parameter) {
        final Class<?> type = parameter.getType();
        final String name = BeanUtils.getParameterName(parameter);
        final Get annotation = parameter.getAnnotation(Get.class);
        final String propValue = annotation == null
                ? ""
                : annotation.value();
        return new Dependency(type, name, propValue);
    }

    private void setupMetadataForConfigType(final Class<?> clazz, final String beanName, final Constructor<?> constructor) {
        final InjectionPoint injectionPoint = getInjectionPoint(constructor);
        final BeanMetadata metadata = new BeanMetadata(clazz, beanName, BeanType.CONFIGURATION, injectionPoint);
        final BeanMethod[] beanMethods = getBeanMethods(clazz);
        configBeanMetadataPerName.put(beanName, new ConfigBeanMetadata(metadata, beanMethods));
    }

    private BeanMethod[] getBeanMethods(final Class<?> configClass) {
        return Stream.of(configClass.getMethods())
                .map(this::getBeanMethod)
                .filter(Objects::nonNull)
                .toArray(BeanMethod[]::new);
    }

    private BeanMethod getBeanMethod(final Method method) {
        if (!method.isAnnotationPresent(Bean.class)) {
            return null;
        }


        final Dependency[] dependencies = Stream.of(method.getParameters())
                .map(this::getDependencyFromParam)
                .toArray(Dependency[]::new);

        return new BeanMethod(BeanUtils.getBeanName(method), method, dependencies);
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
