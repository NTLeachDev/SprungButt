package com.nleachdev.derivativedi.framework.core;

import com.nleachdev.derivativedi.framework.domain.BeanMethod;
import com.nleachdev.derivativedi.framework.domain.BeanType;
import com.nleachdev.derivativedi.framework.domain.Dependency;
import com.nleachdev.derivativedi.framework.domain.InjectionPoint;
import com.nleachdev.derivativedi.framework.exception.ConflictingBeanNameException;
import com.nleachdev.derivativedi.framework.annotation.Bean;
import com.nleachdev.derivativedi.framework.annotation.GetProp;
import com.nleachdev.derivativedi.framework.domain.metadata.BeanMetadata;
import com.nleachdev.derivativedi.framework.domain.metadata.ConfigBeanMetadata;
import com.nleachdev.derivativedi.framework.domain.metadata.ConfiguredBeanMetadata;
import com.nleachdev.derivativedi.framework.domain.metadata.Metadata;
import com.nleachdev.derivativedi.framework.util.BeanUtils;
import com.nleachdev.derivativedi.framework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class ContainerSetup {
    private static final String CONFLICTING_BEAN_NAME_EXC_MESSAGE = "Bean with name %s already exists, you must ensure that each bean name is unique";
    private final Map<BeanType, Set<Metadata<?>>> metadataPerBeanType;
    private final Map<String, Metadata<?>> metadataPerBeanName;
    private final Map<Class<?>, Set<String>> beanNamesPerType;

    public ContainerSetup(final Map<BeanType, Set<Metadata<?>>> metadataPerBeanType,
                          final Map<String, Metadata<?>> metadataPerBeanName,
                          final Map<Class<?>, Set<String>> beanNamesPerType) {
        this.metadataPerBeanType = metadataPerBeanType;
        this.metadataPerBeanName = metadataPerBeanName;
        this.beanNamesPerType = beanNamesPerType;
    }

    public void setupBeanMetadata(final Set<Class<?>> classes) {
        classes.forEach(this::setupBeanMetadata);
    }

    private <T> void setupBeanMetadata(final Class<T> clazz) {
        final Constructor<T> constructor = BeanUtils.getBeanConstructor(clazz);
        if (BeanUtils.isConfigurationBean(clazz)) {
            setupMetadataForConfigType(clazz, constructor);
        }

        if (BeanUtils.isComponentBean(clazz)) {
            setupMetadataForComponentType(clazz, constructor);
        }
    }

    private <T> void trackBean(final Class<T> type, final String beanName, final BeanType beanType,
                           final Metadata<T> metadata) {
        CollectionUtils.addToMapSet(metadataPerBeanType, beanType, metadata);
        CollectionUtils.addToMapSet(beanNamesPerType, type, beanName);
        if (metadataPerBeanName.containsKey(beanName)) {
            throw new ConflictingBeanNameException(
                    String.format(CONFLICTING_BEAN_NAME_EXC_MESSAGE, beanName)
            );
        }

        metadataPerBeanName.put(beanName, metadata);
    }

    private <T> void setupMetadataForConfigType(final Class<T> type, final Constructor<T> constructor) {
        final String beanName = BeanUtils.getConfigBeanName(type);
        final InjectionPoint<T> injectionPoint = getInjectionPoint(constructor);
        final BeanMethod<?>[] beanMethods = getBeanMethods(type);
        final ConfigBeanMetadata<T> metadata = new ConfigBeanMetadata<>(type, beanName, injectionPoint, beanMethods);
        trackBean(type, beanName, BeanType.CONFIG_COMPONENT, metadata);
        setupConfiguredBeanMetadata(beanMethods, metadata);
    }

    private void setupConfiguredBeanMetadata(final BeanMethod<?>[] beanMethods, final ConfigBeanMetadata<?> parentConfigMetadata) {
        Stream.of(beanMethods).forEach(beanMethod -> {
            final ConfiguredBeanMetadata<?> configuredBeanMetadata = new ConfiguredBeanMetadata<>(beanMethod, parentConfigMetadata);
            CollectionUtils.addToMapSet(metadataPerBeanType, BeanType.CONFIGURED_METHOD_BEAN, configuredBeanMetadata);
            metadataPerBeanName.put(configuredBeanMetadata.getBeanName(), configuredBeanMetadata);
            CollectionUtils.addToMapSet(beanNamesPerType, configuredBeanMetadata.getType(), configuredBeanMetadata.getBeanName());
        });
    }

    private <T> void setupMetadataForComponentType(final Class<T> type, final Constructor<T> constructor) {
        final String beanName = BeanUtils.getComponentBeanName(type);
        final InjectionPoint<T> injectionPoint = getInjectionPoint(constructor);
        final BeanMetadata<T> metadata = new BeanMetadata<>(type, beanName, injectionPoint);
        trackBean(type, beanName, BeanType.COMPONENT, metadata);
    }

    private <T> InjectionPoint<T> getInjectionPoint(final Constructor<T> constructor) {
        if (constructor == null || constructor.getParameterCount() == 0) {
            return new InjectionPoint<>(constructor, null);
        }

        final Dependency[] dependencies = Stream.of(constructor.getParameters())
                .map(this::getDependencyFromParam)
                .toArray(Dependency[]::new);

        return new InjectionPoint<>(constructor, dependencies);
    }

    private Dependency getDependencyFromParam(final Parameter parameter) {
        final Class<?> type = parameter.getType();
        final String name = BeanUtils.getParameterName(parameter);
        final GetProp annotation = parameter.getAnnotation(GetProp.class);
        final String propertyKey = annotation == null
                ? null
                : annotation.value();
        final boolean isInterface = type.isInterface();
        return new Dependency(type, name, propertyKey, isInterface);
    }

    private BeanMethod<?>[] getBeanMethods(final Class<?> configClass) {
        return Stream.of(configClass.getMethods())
                .map(this::getBeanMethod)
                .filter(Objects::nonNull)
                .toArray(BeanMethod[]::new);
    }

    private BeanMethod<?> getBeanMethod(final Method method) {
        if (!method.isAnnotationPresent(Bean.class)) {
            return null;
        }

        final Dependency[] dependencies = Stream.of(method.getParameters())
                .map(this::getDependencyFromParam)
                .toArray(Dependency[]::new);

        return new BeanMethod(BeanUtils.getMethodBeanName(method), method, method.getReturnType(), dependencies);
    }
}
