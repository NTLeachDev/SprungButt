package com.nleachdev.noveildi.framework.model;

public interface PackageResolver {

    public ScannablePackage getPackage(final Class<?> clazz);
}
