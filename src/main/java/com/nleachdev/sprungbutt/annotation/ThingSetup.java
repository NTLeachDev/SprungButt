package com.nleachdev.sprungbutt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Thing()
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ThingSetup {
}
