package com.nleachdev.derivativedi.framework.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Inherited
public @interface GetProp {
    String value() default "";
}
