package com.icetech.common.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
@Documented
public @interface NotNull {

    String msg() default "参数为空";
    String condition() default "";
}
