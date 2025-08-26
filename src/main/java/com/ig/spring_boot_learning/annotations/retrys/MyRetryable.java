package com.ig.spring_boot_learning.annotations.retrys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use class all public method will apply
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyRetryable {

    int maxRetries() default 3;

    /**
     * delay time in millisecond
     * default 10 millisecond
     * @return
     */
    int retryDelay() default 10;

    Class<? extends Throwable>[] retryFor() default {Throwable.class};

    Class<? extends Throwable>[] noRetryFor() default {};
}
