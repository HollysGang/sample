package com.hollysgang.sample.encryption.framework.core;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@Component
public class PITargetHolder {
    private final Map<Class<?>, PITarget[]> piTargetMap = new HashMap<>();

    @PostConstruct
    public void initializeCache() {
        Reflections reflections = new Reflections("com.hollysgang.sample.encryption.demo");

        Set<Class<? extends AbstractDto>> subClasses = reflections.getSubTypesOf(AbstractDto.class);
        for(Class<?> subClass : subClasses){
            setPITarget(subClass);
        }
    }

    void setPITarget(Class<?> clazz) {
        List<PITarget> targets = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true); // private 필드 접근 허용
            if (field.isAnnotationPresent(PersonalInformation.class)) {
                PersonalInformation anno = field.getAnnotation(PersonalInformation.class);
                PITarget pt = new PITarget(true, field, anno.value());
                targets.add(pt);
            } else if (
                    field.getType().isArray() ||
                    Collection.class.isAssignableFrom(field.getType()) ||
                    AbstractDto.class.isAssignableFrom(field.getType())
            ){
                PITarget pt = new PITarget(false, field, null);
                targets.add(pt);
            }
        }
        piTargetMap.put(clazz, targets.toArray(PITarget[]::new));
    }
    public PITarget[] getPITargets(Class<?> clazz){
        if(piTargetMap.containsKey(clazz)){
            return piTargetMap.get(clazz);
        } else {
            return new PITarget[]{};
        }
    }
}
