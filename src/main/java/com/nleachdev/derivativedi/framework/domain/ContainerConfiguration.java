package com.nleachdev.derivativedi.framework.domain;

import com.nleachdev.derivativedi.framework.annotation.Component;
import com.nleachdev.derivativedi.framework.annotation.Config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ContainerConfiguration {
    private static final Class<?>[] BASE_COMPONENT_ANNOTATION_TYPES = {Component.class, Config.class};
    private final Class<?> mainClass;
    private PackageResolver packageResolver;
    private final Set<String> availablePackages;
    private final Set<String> propertyFiles;
    private final Set<String> activeProfiles;
    private final Set<Class<?>> componentAnnotations;
    private final PropertyResolver propertyResolver;

    public static ContainerConfiguration getConfig(final Class<?> mainClass) {
        return new ContainerConfiguration(mainClass);
    }

    private ContainerConfiguration(final Class<?> mainClass) {
        this.mainClass = mainClass;
        propertyFiles = new HashSet<>();
        activeProfiles = new HashSet<>();
        packageResolver = new BasePackageResolver();
        availablePackages = new HashSet<>();
        availablePackages.add(mainClass.getPackage().getName());
        componentAnnotations = new HashSet<>(Arrays.asList(BASE_COMPONENT_ANNOTATION_TYPES));
        propertyResolver = new PropertyResolverImpl(propertyFiles);
    }

    public ContainerConfiguration addComponentAnnotations(final Class<?>[] additionalComponentAnnotations) {
        componentAnnotations.addAll(Arrays.asList(additionalComponentAnnotations));
        return this;
    }

    public ContainerConfiguration addComponentAnnotation(final Class<?> additionalComponentAnnotation) {
        componentAnnotations.add(additionalComponentAnnotation);
        return this;
    }

    public ContainerConfiguration withPackageResolver(final PackageResolver packageResolver) {
        this.packageResolver = packageResolver;
        return this;
    }

    public ContainerConfiguration withScannablePackage(final String scannablePackage) {
        availablePackages.add(scannablePackage);
        return this;
    }

    public ContainerConfiguration withPropertyFile(final String propertyFile) {
        propertyFiles.add(propertyFile);
        return this;
    }

    public ContainerConfiguration withPropertyFiles(final Set<String> propertyFiles) {
        this.propertyFiles.addAll(propertyFiles);
        return this;
    }

    public ContainerConfiguration withProfile(final String profileName) {
        activeProfiles.add(profileName);
        return this;
    }

    public ContainerConfiguration withProfiles(final Set<String> profileNames) {
        activeProfiles.addAll(profileNames);
        return this;
    }

    public Set<String> getPropertyFiles() {
        return propertyFiles;
    }

    public ScannablePackage getScannablePackage() {
        return packageResolver.getPackage(mainClass);
    }

    public Set<String> getAvailablePackages() {
        return availablePackages;
    }

    public Set<Class<?>> getComponentAnnotations() {
        return componentAnnotations;
    }

    public PropertyResolver getPropertyResolver() {
        return propertyResolver;
    }

    public Set<String> getActiveProfiles() {
        return activeProfiles;
    }
}
