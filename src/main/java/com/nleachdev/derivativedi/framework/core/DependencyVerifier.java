package com.nleachdev.derivativedi.framework.core;

import com.nleachdev.derivativedi.framework.domain.PropertyResolver;
import com.nleachdev.derivativedi.framework.exception.AmbiguousBeanDefinitionException;
import com.nleachdev.derivativedi.framework.exception.ChickenAndEggException;
import com.nleachdev.derivativedi.framework.exception.MissingBeanDefinitionException;
import com.nleachdev.derivativedi.framework.exception.MissingImplementationException;
import com.nleachdev.derivativedi.framework.domain.Dependency;
import com.nleachdev.derivativedi.framework.domain.metadata.Metadata;
import com.nleachdev.derivativedi.framework.domain.metadata.PropertyMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class DependencyVerifier {
    private static final Logger logger = LoggerFactory.getLogger(DependencyVerifier.class);
    private static final String AMBIGUOUS_BEAN_EXCEPTION_MSG = "More than one bean for type %s defined, which is a dependency for %s; Try using @Named with the correct name to find the instance you want";
    private static final String MISSING_IMPL_EXCEPTION_MSG = "No implementation of interface type %s available for injection, which is a dependency of %s";
    private static final String MISSING_BEAN_EXCEPTION_MSG = "No bean implementation found for type %s, which is a dependency of %s";
    private static final String CHICKEN_AND_EGG_EXCEPTION_MSG = "Circular Dependency exists between %s (and it's parents) and it's dependencies";

    private final Map<String, Metadata<?>> metadataPerBeanName;
    private final Map<Class<?>, Set<String>> beanNamesPerType;

    public DependencyVerifier(final Map<String, Metadata<?>> metadataPerBeanName,
                              final Map<Class<?>, Set<String>> beanNamesPerType) {
        this.metadataPerBeanName = metadataPerBeanName;
        this.beanNamesPerType = beanNamesPerType;
    }

    public void verifyDependencies() {
        resolveDependencies();
        verifyDependencyHierarchies();
    }

    public void resolveDependencies() {
        metadataPerBeanName.forEach(this::resolveDependencies);
    }


    private void verifyDependencyHierarchies() {
        metadataPerBeanName.values().forEach(
                beanMetadata -> verifyHierarchy(beanMetadata.getBeanName())
        );

        final Map<String, Integer> depCostPerBean = new HashMap<>();
        metadataPerBeanName.forEach((beanName, metadata) -> {
            final int depCost = getDepCost(beanName, metadata, depCostPerBean);
            metadata.setDependencyCost(depCost);
        });
    }

    private int getDepCost(final String beanName, final Metadata metadata,
                           final Map<String, Integer> depCostPerBean) {
        final Dependency[] dependencies = metadata.getDependencies();
        if (dependencies == null || dependencies.length == 0) {
            depCostPerBean.put(beanName, 0);
            return 0;
        }

        int depCost = 1;
        for (final Dependency dependency : dependencies) {
            if (dependency.getPropertyKey() != null) {
                continue;
            }
            final String depName = dependency.getName();
            if (!depCostPerBean.containsKey(depName)) {
                getDepCost(depName, metadataPerBeanName.get(depName), depCostPerBean);
            }
            depCost += depCostPerBean.get(depName);
        }

        depCostPerBean.put(beanName, depCost);
        return depCost;
    }

    private void verifyHierarchy(final String beanName, final String... parentBeanNames) {
        final String[] newParentBeanNames = appendValue(beanName, parentBeanNames);
        final Set<String> dependencyNames = getDependencyNamesForBean(beanName);
        final Set<String> circular = Stream.of(newParentBeanNames)
                .filter(dependencyNames::contains)
                .collect(toSet());
        if (!circular.isEmpty()) {
            throw new ChickenAndEggException(String.format(CHICKEN_AND_EGG_EXCEPTION_MSG, beanName));
        }

        dependencyNames.forEach(dependencyName -> verifyHierarchy(dependencyName, newParentBeanNames));
    }

    private void resolveDependencies(final String beanName, final Metadata<?> metadata) {
        final Dependency[] dependencies = metadata.getDependencies();
        if (dependencies == null || dependencies.length == 0) {
            return;
        }
        final Metadata<?>[] dependencyMetadata = Stream.of(dependencies)
                .map(dependency -> getDependencyMetadata(beanName, dependency))
                .toArray(Metadata[]::new);
        metadata.setDependencyMetadata(dependencyMetadata);
    }

    private Metadata<?> getDependencyMetadata(final String parentBeanName, final Dependency dependency) {
        final Class<?> dependencyType = dependency.getType();
        final String propertyKey = dependency.getPropertyKey();
        if (propertyKey != null) {
            return getPropertyMetadata(dependencyType, propertyKey);
        }

        final Set<String> implNames = beanNamesPerType.keySet()
                .stream()
                .filter(dependencyType::isAssignableFrom)
                .map(beanNamesPerType::get)
                .flatMap(Set::stream)
                .collect(toSet());

        return getDependencyMetadata(dependency, parentBeanName, implNames);
    }

    private Metadata<?> getPropertyMetadata(final Class<?> type, final String propertyKey) {
        final PropertyResolver propertyResolver = Container.getInstance().getConfig().getPropertyResolver();
        final Object propertyValue = propertyResolver.getValueForProperty(type, propertyKey);
        return new PropertyMetadata(type, "", propertyKey, propertyValue);
    }

    private Metadata<?> getDependencyMetadata(final Dependency dependency, final String parentBeanName,
                                           final Set<String> namesForDepType) {
        final String dependencyName = dependency.getName();
        final Class<?> dependencyType = dependency.getType();
        if (namesForDepType == null || namesForDepType.isEmpty()) {
            throwForMissingDep(dependencyType, parentBeanName, dependency.isInterfaceType());
        }

        if (namesForDepType.size() != 1 && !namesForDepType.contains(dependencyName)) {
            throw new AmbiguousBeanDefinitionException(String.format(AMBIGUOUS_BEAN_EXCEPTION_MSG, dependencyType, parentBeanName));
        }

        final String implName = namesForDepType.size() == 1
                ? (String) namesForDepType.toArray()[0]
                : dependencyName;
        dependency.setName(implName);
        return metadataPerBeanName.get(implName);
    }

    private Set<String> getDependencyNamesForBean(final String beanName) {
        final Dependency[] dependencies = metadataPerBeanName.get(beanName).getDependencies();
        if (dependencies == null) {
            return new HashSet<>();
        }
        return Stream.of(dependencies)
                .filter(dependency -> dependency.getPropertyKey() == null)
                .map(Dependency::getName)
                .collect(toSet());
    }

    private String[] appendValue(final String str, final String... strArr) {
        if (strArr == null) {
            return new String[]{str};
        }
        final String[] newArr = Arrays.copyOf(strArr, strArr.length + 1);
        newArr[strArr.length] = str;
        return newArr;
    }

    private void throwForMissingDep(final Class<?> dependencyType, final String parentBeanName, final boolean isInterface) {
        if (isInterface) {
            throw new MissingImplementationException(String.format(MISSING_IMPL_EXCEPTION_MSG, dependencyType, parentBeanName));
        }

        throw new MissingBeanDefinitionException(String.format(MISSING_BEAN_EXCEPTION_MSG, dependencyType, parentBeanName));
    }

}
