package com.nleachdev.noveildi.framework.core;

import com.nleachdev.noveildi.framework.annotation.Bean;
import com.nleachdev.noveildi.framework.annotation.Get;
import com.nleachdev.noveildi.framework.exception.ConflictingBeanNameException;
import com.nleachdev.noveildi.framework.model.*;
import com.nleachdev.noveildi.framework.util.BeanUtils;
import com.nleachdev.noveildi.framework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class ContainerSetup {
    private final Map<BeanType, Set<Metadata>> metadataPerBeanType;
    private final Map<String, Metadata> metadataPerBeanName;
    private final Map<Class<?>, Set<String>> beanNamesPerType;

    public ContainerSetup(final Map<BeanType, Set<Metadata>> metadataPerBeanType,
                          final Map<String, Metadata> metadataPerBeanName,
                          final Map<Class<?>, Set<String>> beanNamesPerType) {
        this.metadataPerBeanType = metadataPerBeanType;
        this.metadataPerBeanName = metadataPerBeanName;
        this.beanNamesPerType = beanNamesPerType;
    }

    public void setupBeanMetadata(final Set<Class<?>> classes) {
        classes.forEach(this::setupBeanMetadata);
    }

    private void setupBeanMetadata(final Class<?> clazz) {
        final Constructor<?> constructor = BeanUtils.getBeanConstructor(clazz);
        if (BeanUtils.isConfigurationBean(clazz)) {
            setupMetadataForConfigType(clazz, constructor);
        }

        if (BeanUtils.isComponentBean(clazz)) {
            setupMetadataForComponentType(clazz, constructor);
        }
    }

    private void trackBean(final Class<?> type, final String beanName, final BeanType beanType,
                           final Metadata metadata) {
        CollectionUtils.addToMapSet(metadataPerBeanType, beanType, metadata);
        CollectionUtils.addToMapSet(beanNamesPerType, type, beanName);
        if (metadataPerBeanName.containsKey(beanName)) {
            throw new ConflictingBeanNameException(
                    String.format("Bean with name %s already exists, you must ensure that each bean name is unique", beanName)
            );
        }

        metadataPerBeanName.put(beanName, metadata);
    }

    private void setupMetadataForConfigType(final Class<?> type, final Constructor<?> constructor) {
        final String beanName = BeanUtils.getConfigBeanName(type);
        final InjectionPoint injectionPoint = getInjectionPoint(constructor);
        final BeanMethod[] beanMethods = getBeanMethods(type);
        final ConfigBeanMetadata metadata = new ConfigBeanMetadata(type, beanName, injectionPoint, beanMethods);
        trackBean(type, beanName, BeanType.CONFIG_COMPONENT, metadata);
        setupConfiguredBeanMetadata(beanMethods);
    }

    private void setupConfiguredBeanMetadata(final BeanMethod[] beanMethods) {
        Stream.of(beanMethods).forEach(beanMethod -> {
            final ConfiguredBeanMetadata configuredBeanMetadata = new ConfiguredBeanMetadata(beanMethod);
            CollectionUtils.addToMapSet(metadataPerBeanType, BeanType.CONFIGURED_METHOD_BEAN, configuredBeanMetadata);
            metadataPerBeanName.put(configuredBeanMetadata.getBeanName(), configuredBeanMetadata);
            CollectionUtils.addToMapSet(beanNamesPerType, configuredBeanMetadata.getType(), configuredBeanMetadata.getBeanName());
        });
    }

    private void setupMetadataForComponentType(final Class<?> type, final Constructor<?> constructor) {
        final String beanName = BeanUtils.getComponentBeanName(type);
        final InjectionPoint injectionPoint = getInjectionPoint(constructor);
        final BeanMetadata metadata = new BeanMetadata(type, beanName, injectionPoint);
        trackBean(type, beanName, BeanType.COMPONENT, metadata);
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
        final String propertyKey = annotation == null
                ? null
                : annotation.value();
        final boolean isInterface = type.isInterface();
        return new Dependency(type, name, propertyKey, isInterface);
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

        return new BeanMethod(BeanUtils.getMethodBeanName(method), method, method.getReturnType(), dependencies);
    }
}
