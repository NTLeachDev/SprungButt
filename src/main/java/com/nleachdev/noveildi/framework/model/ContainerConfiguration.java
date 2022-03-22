package com.nleachdev.noveildi.framework.model;

import com.nleachdev.noveildi.framework.annotation.Component;
import com.nleachdev.noveildi.framework.service.BasePackageResolver;
import com.nleachdev.noveildi.framework.service.PackageResolver;

import java.util.HashSet;
import java.util.Set;

@Component
public class ContainerConfiguration {
    private final Class<?> mainClass;
    private PackageResolver packageResolver;
    private final Set<String> availablePackages;

    public static ContainerConfiguration getConfig(final Class<?> mainClass) {
        return new ContainerConfiguration(mainClass);
    }

    private ContainerConfiguration(final Class<?> mainClass) {
        this.mainClass = mainClass;
        packageResolver = new BasePackageResolver();
        availablePackages = new HashSet<>();
        availablePackages.add(mainClass.getPackage().getName());
    }

    public ContainerConfiguration withPackageResolver(final PackageResolver packageResolver) {
        this.packageResolver = packageResolver;
        return this;
    }

    public ContainerConfiguration withScannablePackage(final String scannablePackage) {
        availablePackages.add(scannablePackage);
        return this;
    }

    public ScannablePackage getScannablePackage() {
        return packageResolver.getPackage(mainClass);
    }

    public Set<String> getAvailablePackages() {
        return availablePackages;
    }
}
