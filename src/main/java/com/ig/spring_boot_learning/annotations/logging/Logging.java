package com.ig.spring_boot_learning.annotations.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@Aspect
@Slf4j
public class Logging {

    @Around("@annotation(auditFilter) && execution(public * *(..))")
    public Object logging(ProceedingJoinPoint joinPoint, AuditFilter auditFilter) throws Throwable {
        var pid = UUID.randomUUID().toString();
        Logger log = getLogger(joinPoint);
        log.info("[{}]: Request to controller start ", pid);
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        log.info("[{}]: Execution Time Log: Class: {}, Method: {}, request spent: {} ms",
                pid, className, methodName, (endTime - startTime));

        return result;
    }

    private Logger getLogger(ProceedingJoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    }
}
