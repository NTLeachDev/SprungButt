package com.nleachdev.derivativedi.framework.repository;

import com.nleachdev.derivativedi.framework.annotation.Bean;
import com.nleachdev.derivativedi.framework.annotation.GetProp;
import com.nleachdev.derivativedi.framework.config.ContainerConfig;
import com.nleachdev.derivativedi.framework.domain.BeanMethod;
import com.nleachdev.derivativedi.framework.domain.BeanType;
import com.nleachdev.derivativedi.framework.domain.Dependency;
import com.nleachdev.derivativedi.framework.domain.metadata.ConfiguredBeanMetadata;
import com.nleachdev.derivativedi.framework.domain.metadata.Metadata;
import com.nleachdev.derivativedi.framework.util.BeanUtils;
import com.nleachdev.derivativedi.framework.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Stream;

public class BeanMetadataRepository {
    private static final Logger logger = LoggerFactory.getLogger(BeanMetadataRepository.class);
    private final Map<String, Metadata<?>> metadataPerBeanName;
    private final Map<BeanType, Set<Metadata<?>>> metadataPerBeanType;
    private final Map<Class<?>, Set<String>> beanNamesPerType;

    public BeanMetadataRepository() {
        this.metadataPerBeanName = new HashMap<>();
        this.metadataPerBeanType = new HashMap<>();
        this.beanNamesPerType = new HashMap<>();
    }

    public void clear() {
        this.metadataPerBeanName.clear();
        this.metadataPerBeanType.clear();
        this.beanNamesPerType.clear();
    }

    public void setupBeanMetadata(final ContainerConfig config) {
        config.getClassResolver()
                .getRelevantClasses()
                .forEach(clazz -> this.setupBeanMetadata(config, clazz));
    }

    private <T> void setupBeanMetadata(final ContainerConfig config, final Class<T> clazz) {
        if (!BeanUtils.isPartOfActiveProfiles(clazz, config.getActiveProfiles())) {
            return;
        }

        final String beanName = BeanUtils.getBeanName(clazz);
        final BeanType beanType = BeanUtils.getBeanType(clazz);
        final Dependency[] dependencies = this.getDependencies(clazz, BeanUtils.getBeanConstructor(clazz));

        final Metadata<T> metadata = new Metadata<>(clazz, beanType, beanName, dependencies, new HashSet<>());
        this.metadataPerBeanName.put(beanName, metadata);
        CollectionUtils.addToMapSet(this.metadataPerBeanType, beanType, metadata);
        CollectionUtils.addToMapSet(this.beanNamesPerType, clazz, beanName);

        if (beanType == BeanType.CONFIG_COMPONENT) {
            this.setupConfiguredBeanMetadata(metadata);
        }
    }

    private void setupConfiguredBeanMetadata(final Metadata<?> parentConfigMetadata) {
        final BeanMethod<?>[] beanMethods = this.getBeanMethods(parentConfigMetadata.getType());

        Stream.of(beanMethods).forEach(beanMethod -> {
            final ConfiguredBeanMetadata<?> configuredBeanMetadata = new ConfiguredBeanMetadata<>(
                    beanMethod.getReturnType(), beanMethod.getBeanName(), beanMethod.getDependencies(),
                    new HashSet<>(), beanMethod.getMethod(), this.dependencyFromMetadata(parentConfigMetadata)
            );

            this.metadataPerBeanName.put(configuredBeanMetadata.getBeanName(), configuredBeanMetadata);
            CollectionUtils.addToMapSet(this.metadataPerBeanType, BeanType.CONFIGURED_METHOD_BEAN, configuredBeanMetadata);
            CollectionUtils.addToMapSet(this.beanNamesPerType, configuredBeanMetadata.getType(), configuredBeanMetadata.getBeanName());
        });
    }

    private Dependency dependencyFromMetadata(final Metadata<?> metadata) {
        return new Dependency(
                metadata.getType(),
                metadata.getBeanName(),
                null,
                metadata.getType().isInterface()
        );
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

    private <T> Dependency[] getDependencies(final Class<T> clazz, final Constructor<T> constructor) {
        if (constructor == null || constructor.getParameterCount() == 0) {
            return new Dependency[0];
        }

        return Stream.of(constructor.getParameters())
                .map(this::getDependencyFromParam)
                .toArray(Dependency[]::new);
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
}
