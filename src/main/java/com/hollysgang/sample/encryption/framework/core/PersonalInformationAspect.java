package com.hollysgang.sample.encryption.framework.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class PersonalInformationAspect {
    private final PersonalInformationProcessor piProcessor;

    @Around("execution(public * com.hollysgang.sample.encryption.demo.repository..*.*(..))")
    public Object doAroundAboutPersonalInformation(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        piProcessor.encryptPersonalInformation(args);
        Object result = joinPoint.proceed(args);
        piProcessor.decryptPersonalInformation(result);
        return result;
    }
}
