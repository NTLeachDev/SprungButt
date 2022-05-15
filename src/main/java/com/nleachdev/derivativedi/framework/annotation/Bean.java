package com.nleachdev.derivativedi.framework.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Bean {
    String name() default "";
}
