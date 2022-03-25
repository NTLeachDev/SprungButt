package com.nleachdev.noveildi.framework.core;

import com.nleachdev.noveildi.framework.exception.AmbiguousBeanDefinitionException;
import com.nleachdev.noveildi.framework.exception.MissingBeanDefinitionException;
import com.nleachdev.noveildi.framework.exception.MissingImplementationException;
import com.nleachdev.noveildi.framework.model.Dependency;
import com.nleachdev.noveildi.framework.model.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class DependencyVerifier {
    private static final Logger logger = LoggerFactory.getLogger(DependencyVerifier.class);
    private static final String AMBIGUOUS_BEAN_EXCEPTION_MSG = "More than one bean for type %s defined, which is a dependency for %s; Try using @Named with the correct name to find the instance you want";
    private static final String MISSING_IMPL_EXCEPTION_MSG = "No implementation of interface type %s available for injection, which is a dependency of %s";
    private static final String MISSING_BEAN_EXCEPTION_MSG = "No bean implementation found for type %s, which is a dependency of %s";
    private final Map<String, Metadata> metadataPerBeanName;
    private final Map<Class<?>, Set<String>> beanNamesPerType;
    private final Map<Class<?>, Set<String>> beanNamesPerInterfaceType;

    public DependencyVerifier(final Map<String, Metadata> metadataPerBeanName,
                              final Map<Class<?>, Set<String>> beanNamesPerType,
                              final Map<Class<?>, Set<String>> beanNamesPerInterfaceType) {
        this.metadataPerBeanName = metadataPerBeanName;
        this.beanNamesPerType = beanNamesPerType;
        this.beanNamesPerInterfaceType = beanNamesPerInterfaceType;
    }

    public void verifyDependencyExistence() {
        metadataPerBeanName.forEach(this::verifyBeanDependencies);
    }

    private void verifyBeanDependencies(final String beanName, final Metadata metadata) {
        final Class<?> type = metadata.getType();
        final Dependency[] dependencies = metadata.getDependencies();

        Stream.of(dependencies).forEach(dependency -> verifyDependency(beanName, dependency));
        logger.info("{} with name {} has been verified", type, beanName);
    }

    private void verifyDependency(final String dependentName, final Dependency dependency) {
        final String dependencyName = dependency.getName();
        final Class<?> dependencyType = dependency.getType();

        if (dependency.isInterfaceType()) {
            final Set<String> implNames = beanNamesPerInterfaceType.get(dependencyType);
            if (implNames == null || implNames.isEmpty()) {
                throw new MissingImplementationException(String.format(MISSING_IMPL_EXCEPTION_MSG, dependencyType, dependentName));
            }

            if (implNames.size() == 1 || implNames.contains(dependencyName)) {
                return;
            } else {
                throw new AmbiguousBeanDefinitionException(String.format(AMBIGUOUS_BEAN_EXCEPTION_MSG, dependencyType, dependentName));
            }
        }

        final Set<String> namesForDepType = beanNamesPerType.get(dependencyType);
        if (namesForDepType == null || namesForDepType.isEmpty() || !namesForDepType.contains(dependencyName)) {
            throw new MissingBeanDefinitionException(String.format(MISSING_BEAN_EXCEPTION_MSG, dependencyType, dependentName));
        }
    }

}
