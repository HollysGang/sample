package com.hollysgang.sample.encryption.framework.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class PersonalInformationAspect {
    private final PersonalInformationProcessor piProcessor;

    @Around("execution(public * com.hollysgang.sample.encryption.demo.repository..*.*(..))")
    public Object doAroundAboutPersonalInformation(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        doEncrypt(args);
        Object result = joinPoint.proceed(args);
        doDecrypt(result);
        return result;
    }

    // 일단 단일 DTO가 넘어온다고 가정
    private void doEncrypt(Object dto) {
        Class<?> dtoClass = dto.getClass();
        for (Field field : dtoClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(PersonalInformation.class)) {
                PersonalInformation anno = field.getAnnotation(PersonalInformation.class);
                field.setAccessible(true); // private 필드 접근 허용
                try {
                    field.set(dto, piProcessor.encryptPersonalInformation(anno.value(), (String) field.get(dto)));
                } catch (IllegalAccessException e) {
                    log.error("암호화에 실패했습니다.");
                }
            }
        }
    }

    private void doDecrypt(Object dto) {
        Class<?> dtoClass = dto.getClass();
        for (Field field : dtoClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(PersonalInformation.class)) {
                PersonalInformation anno = field.getAnnotation(PersonalInformation.class);
                field.setAccessible(true); // private 필드 접근 허용
                try {
                    field.set(dto, piProcessor.decryptPersonalInformation(anno.value(), (String) field.get(dto)));
                } catch (IllegalAccessException e) {
                    log.error("복호화에 실패했습니다.");
                }
            }
        }
    }
}
