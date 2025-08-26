package com.ig.spring_boot_learning.annotations.retrys;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class RetryableAspect {

    @Around("@annotation(myRetryable)")
    public Object retryMethod(ProceedingJoinPoint joinPoint, MyRetryable myRetryable) throws Throwable {
        return executeWithRetry(joinPoint, myRetryable);
    }

    @Around("@within(myRetryable) && execution(public * *(..))")
    public Object retryClass(ProceedingJoinPoint joinPoint, MyRetryable myRetryable) throws Throwable {
        // Check if method has its own @CustomRetry annotation (method-level takes precedence)
        var methodAnnotation = joinPoint.getTarget().getClass()
                .getDeclaredMethod(joinPoint.getSignature().getName(),
                        ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getParameterTypes())
                .getAnnotation(MyRetryable.class);

        if (methodAnnotation != null) {
            // Method has its own annotation, skip class-level processing
            return joinPoint.proceed();
        }

        return executeWithRetry(joinPoint, myRetryable);
    }

    private Object executeWithRetry(ProceedingJoinPoint joinPoint, MyRetryable customRetry) throws Throwable {
        int maxAttempts = customRetry.maxRetries();
        var delay = customRetry.retryDelay();
        Class<? extends Throwable>[] retryFor = customRetry.retryFor();
        Class<? extends Throwable>[] noRetryFor = customRetry.noRetryFor();

        String methodName = joinPoint.getSignature().getName();

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                log.info("Executing method: {} - Attempt: {}/{}", methodName, attempt, maxAttempts);
                return joinPoint.proceed();

            } catch (Throwable throwable) {

                // Check if exception should not trigger retry
                if (shouldNotRetry(throwable, noRetryFor)) {
                    log.info("Exception {} is in noRetryFor list. Not retrying.", throwable.getClass().getSimpleName());
                    throw throwable;
                }

                // Check if exception should trigger retry
                if (!shouldRetry(throwable, retryFor)) {
                    log.info("Exception {} is not in retryFor list. Not retrying.", throwable.getClass().getSimpleName());
                    throw throwable;
                }

                // If this is the last attempt, throw the exception
                if (attempt == maxAttempts) {
                    log.error("Method {} failed after {} attempts. Throwing exception: {}",
                            methodName, maxAttempts, throwable.getMessage());
                    throw throwable;
                }

                // Calculate delay for next attempt
                long currentDelay = (long) (delay);

                log.warn("Method {} failed on attempt {}/{}. Retrying in {}ms. Error: {}",
                        methodName, attempt, maxAttempts, currentDelay, throwable.getMessage());

                try {
                    Thread.sleep(currentDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", e);
                }
            }
        }

        // This should never be reached
        throw new RuntimeException("Retry logic error");
    }

    private boolean shouldRetry(Throwable throwable, Class<? extends Throwable>[] retryFor) {
        return Arrays.stream(retryFor)
                .anyMatch(exceptionClass -> exceptionClass.isAssignableFrom(throwable.getClass()));
    }

    private boolean shouldNotRetry(Throwable throwable, Class<? extends Throwable>[] noRetryFor) {
        return Arrays.stream(noRetryFor)
                .anyMatch(exceptionClass -> exceptionClass.isAssignableFrom(throwable.getClass()));
    }
}
