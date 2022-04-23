package com.nleachdev.derivativedi.framework.domain;

import com.nleachdev.derivativedi.framework.domain.metadata.Metadata;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanInstantiation {

    public void instantiateBeans(final Map<String, Metadata<?>> metadataPerBeanName) {
        final List<Metadata<?>> metadata = metadataPerBeanName.values()
                .stream()
                .sorted(Metadata.COMPARATOR)
                .collect(Collectors.toList());

        metadata.forEach(this::instantiateBean);
    }

    private void instantiateBean(final Metadata<?> metadata) {
        instantiateComponentBean(metadata);
    }

    private void instantiateComponentBean(final Metadata<?> metadata) {
        final Metadata<?>[] dependencyMetadata = metadata.getDependencyMetadata();
        if (dependencyMetadata == null || dependencyMetadata.length == 0) {
            metadata.createAndSetInstance();
            return;
        }

        final Object[] dependencyInstances = Stream.of(dependencyMetadata)
                .map(Metadata::getInstance)
                .toArray();
        metadata.createAndSetInstance(dependencyInstances);
    }
}
