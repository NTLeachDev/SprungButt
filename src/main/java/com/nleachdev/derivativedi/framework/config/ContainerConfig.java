package com.nleachdev.derivativedi.framework.config;

import com.nleachdev.derivativedi.framework.annotation.Component;
import com.nleachdev.derivativedi.framework.annotation.Config;
import com.nleachdev.derivativedi.framework.scanner.BaseClassResolver;
import com.nleachdev.derivativedi.framework.scanner.ClassResolver;
import com.nleachdev.derivativedi.framework.scanner.ScannablePackage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ContainerConfig {
    private static final Class<?>[] BASE_COMPONENT_ANNOTATION_TYPES = {Component.class, Config.class};
    private final Class<?> mainClass;
    private final Set<String> availablePackages;
    private final Set<String> propertyFiles;
    private final Set<String> activeProfiles;
    private final Set<Class<?>> componentAnnotations;
    private PropertyResolver propertyResolver;
    private PackageResolver packageResolver;
    private ClassResolver classResolver;

    public static ContainerConfig getConfig(final Class<?> mainClass) {
        return new ContainerConfig(mainClass);
    }

    private ContainerConfig(final Class<?> mainClass) {
        this.mainClass = mainClass;
        this.propertyFiles = new HashSet<>();
        this.activeProfiles = new HashSet<>();
        this.availablePackages = new HashSet<>();
        this.availablePackages.add(mainClass.getPackage().getName());
        this.componentAnnotations = new HashSet<>(Arrays.asList(BASE_COMPONENT_ANNOTATION_TYPES));
        this.propertyResolver = new BasePropertyResolver(this.propertyFiles);
        this.packageResolver = new BasePackageResolver();
        this.classResolver = new BaseClassResolver(mainClass, availablePackages, packageResolver);
    }

    public ContainerConfig addComponentAnnotations(final Class<?>[] additionalComponentAnnotations) {
        this.componentAnnotations.addAll(Arrays.asList(additionalComponentAnnotations));
        return this;
    }

    public ContainerConfig addComponentAnnotation(final Class<?> additionalComponentAnnotation) {
        this.componentAnnotations.add(additionalComponentAnnotation);
        return this;
    }

    public ContainerConfig withPackageResolver(final PackageResolver packageResolver) {
        this.packageResolver = packageResolver;
        return this;
    }

    public ContainerConfig withScannablePackage(final String scannablePackage) {
        this.availablePackages.add(scannablePackage);
        return this;
    }

    public ContainerConfig withPropertyFile(final String propertyFile) {
        this.propertyFiles.add(propertyFile);
        return this;
    }

    public ContainerConfig withPropertyFiles(final Set<String> propertyFiles) {
        this.propertyFiles.addAll(propertyFiles);
        return this;
    }

    public ContainerConfig withProfile(final String profileName) {
        this.activeProfiles.add(profileName);
        return this;
    }

    public ContainerConfig withProfiles(final Set<String> profileNames) {
        this.activeProfiles.addAll(profileNames);
        return this;
    }

    public ContainerConfig withPropertyResolver(final PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
        return this;
    }

    public ContainerConfig withClassResolver(final ClassResolver classResolver) {
        this.classResolver = classResolver;
        return this;
    }

    public Set<String> getPropertyFiles() {
        return this.propertyFiles;
    }

    public PackageResolver getPackageResolver() {
        return this.packageResolver;
    }

    public ScannablePackage getScannablePackage() {
        return this.packageResolver.getPackage(this.mainClass);
    }

    public Set<String> getAvailablePackages() {
        return this.availablePackages;
    }

    public Set<Class<?>> getComponentAnnotations() {
        return this.componentAnnotations;
    }

    public PropertyResolver getPropertyResolver() {
        return this.propertyResolver;
    }

    public Set<String> getActiveProfiles() {
        return this.activeProfiles;
    }

    public ClassResolver getClassResolver() {
        return this.classResolver;
    }

    public Class<?> getMainClass() {
        return this.mainClass;
    }
}
