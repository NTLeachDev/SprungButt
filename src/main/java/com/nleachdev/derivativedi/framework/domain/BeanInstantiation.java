package com.nleachdev.derivativedi.framework.domain;

import com.nleachdev.derivativedi.framework.domain.metadata.Metadataa;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanInstantiation {

    public void instantiateBeans(final Map<String, Metadataa<?>> metadataPerBeanName) {
        final List<Metadataa<?>> metadata = metadataPerBeanName.values()
                .stream()
                .sorted(Metadataa.COMPARATOR)
                .collect(Collectors.toList());

        metadata.forEach(this::instantiateBean);
    }

    private void instantiateBean(final Metadataa<?> metadataa) {
        instantiateComponentBean(metadataa);
    }

    private void instantiateComponentBean(final Metadataa<?> metadataa) {
        final Metadataa<?>[] dependencyMetadata = metadataa.getDependencyMetadata();
        if (dependencyMetadata == null || dependencyMetadata.length == 0) {
            metadataa.createAndSetInstance();
            return;
        }

        final Object[] dependencyInstances = Stream.of(dependencyMetadata)
                .map(Metadataa::getInstance)
                .toArray();
        metadataa.createAndSetInstance(dependencyInstances);
    }
}
