package com.sparta.icy.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class RequestLogAop {

    @Pointcut("execution(* com.sparta.icy..*Controller.*(..))")
    private void controller() { }

    @Around("controller()")
    public Object loggingBefore(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            log.warn("null");
            return joinPoint.proceed();
        }

        HttpServletRequest request = requestAttributes.getRequest();

        String methodName = joinPoint.getSignature().getName();

        String requestUri = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);

        String requestURL = request.getRequestURL().toString();

        String httpMethod = request.getMethod();

        String params = getParams(request);

        String headers = request.getHeaderNames().toString();



        log.info("[{}] {}", httpMethod, requestUri);
        log.info("method: {}", methodName);
        log.info("params: {}", params);
        log.info("headers: {}", headers);
        log.info("requestURL: {}", requestURL);

        return joinPoint.proceed();
    }


    private static String getParams(HttpServletRequest request) {

        Map<String, String[]> parameterMap = request.getParameterMap();

        return parameterMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue())) // 각 엔트리를 "key=[value]" 형태의 문자열로 변환한다.
                .collect(Collectors.joining(", ")); // 변환된 문자열들을 ", "로 구분하여 하나의 문자열로 합친다.
    }
}
