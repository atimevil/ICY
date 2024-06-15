package com.sparta.icy.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.sparta.icy.controller..*(..))")
    public void logBeforeControllerMethods(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        log.info("Request URL: {}, HTTP Method: {}, Method: {}.{} with arguments: {}",
                "URL Placeholder", // 실제 URL을 얻으려면 HttpServletRequest를 이용
                "HTTP Method Placeholder", // 실제 HTTP Method를 얻으려면 HttpServletRequest를 이용
                className,
                methodName,
                args
        );
    }
}
