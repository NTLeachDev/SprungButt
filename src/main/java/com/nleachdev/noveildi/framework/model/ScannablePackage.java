package com.nleachdev.noveildi.framework.model;

import java.util.Objects;
import java.util.StringJoiner;

public class ScannablePackage {
    private final String packageName;
    private final ScanningTarget scanningTarget;

    public ScannablePackage(String packageName, ScanningTarget scanningTarget) {
        this.packageName = packageName;
        this.scanningTarget = scanningTarget;
    }

    public String getPackageName() {
        return packageName;
    }

    public ScanningTarget getPackageType() {
        return scanningTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScannablePackage that = (ScannablePackage) o;
        return Objects.equals(packageName, that.packageName) &&
                scanningTarget == that.scanningTarget;
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageName, scanningTarget);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ScannablePackage.class.getSimpleName() + "[", "]")
                .add("packageName='" + packageName + "'")
                .add("packageType=" + scanningTarget)
                .toString();
    }
}
