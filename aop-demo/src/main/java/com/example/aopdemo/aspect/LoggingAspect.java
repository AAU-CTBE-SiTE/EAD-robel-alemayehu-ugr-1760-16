package com.example.aopdemo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.example.aopdemo.service..*(..))")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void logBefore(JoinPoint jp) {
        System.out.println("[BEFORE] " + jp.getSignature().getName()
                + " args=" + Arrays.toString(jp.getArgs()));
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logResult(JoinPoint jp, Object result) {
        System.out.println("[RETURNED] " + jp.getSignature().getName() + " -> " + result);
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logError(JoinPoint jp, Exception ex) {
        System.out.println("[ERROR] " + jp.getSignature().getName() + " threw " + ex);
    }

    @Around("serviceMethods()")
    public Object time(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        try {
            return pjp.proceed();
        } finally {
            long micros = (System.nanoTime() - start) / 1000;
            System.out.println("[TIME] " + pjp.getSignature().getName() + " took " + micros + " µs");
        }
    }
}