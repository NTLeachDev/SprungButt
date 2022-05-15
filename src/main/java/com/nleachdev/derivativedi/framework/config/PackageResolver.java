package com.nleachdev.derivativedi.framework.config;

import com.nleachdev.derivativedi.framework.scanner.ScannablePackage;

public interface PackageResolver {

    ScannablePackage getPackage(final Class<?> clazz);
}
