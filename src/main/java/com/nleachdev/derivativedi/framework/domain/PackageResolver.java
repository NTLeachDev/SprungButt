package com.nleachdev.derivativedi.framework.domain;

public interface PackageResolver {

    public ScannablePackage getPackage(final Class<?> clazz);
}
