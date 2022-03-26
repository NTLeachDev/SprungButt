package com.nleachdev.noveildi.framework.service;

import com.nleachdev.noveildi.framework.model.ScannablePackage;

public interface PackageResolver {

    public ScannablePackage getPackage(final Class<?> clazz);
}
