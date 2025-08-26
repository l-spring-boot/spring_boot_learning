package com.ig.spring_boot_learning.configurations;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingFilter {

    @Before("execution(* com.ig.spring_boot_learning.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("==> " + joinPoint.getThis().toString());
    }
}
