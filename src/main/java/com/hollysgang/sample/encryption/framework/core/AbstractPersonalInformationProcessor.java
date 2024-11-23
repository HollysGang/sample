package com.hollysgang.sample.encryption.framework.core;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public abstract class AbstractPersonalInformationProcessor implements PersonalInformationProcessor {

    private final Map<String, Function<String, String>> typeEncFuncs = new HashMap<>();
    private final Map<String, Function<String, String>> typeDecFuncs = new HashMap<>();

    @Override
    public final String encryptPersonalInformation(String type, String plain) {
        Function<String, String> encFunc = typeEncFuncs.get(type);
        if(encFunc == null) return plain; // 해당 타입에 대한 암호화 함수가 정의 돼있지 않습니다.
        return encFunc.apply(plain);
    }

    @Override
    public final String decryptPersonalInformation(String type, String encrypted) {
        Function<String, String> decFunc = typeDecFuncs.get(type);
        if(decFunc == null) return encrypted; // 해당 타입에 대한 복호화 함수가 정의 돼있지 않습니다.
        return decFunc.apply(encrypted);
    }

    @Override
    public final void encryptPersonalInformation(Object obj) {
        helper(obj, false);
    }

    @Override
    public final void decryptPersonalInformation(Object obj) {
        helper(obj, true);
    }

    private void helper(Object obj, boolean isDecrypt){
        if(obj == null) return;
        if(obj instanceof Collection){
            for(Object item: (Collection) obj){
                helper(item, isDecrypt);
            }
        } else if (obj.getClass().isArray()){
            int len = Array.getLength(obj);
            for(int i = 0; i < len; i += 1){
                Object item = Array.get(obj, i);
                helper(item, isDecrypt);
            }
        } else if (obj instanceof AbstractDto) { // obj가 dto라면
            Class<?> dtoClass = obj.getClass();
            for (Field field : dtoClass.getDeclaredFields()) {
                field.setAccessible(true); // private 필드 접근 허용
                if (field.isAnnotationPresent(PersonalInformation.class)) {
                    PersonalInformation anno = field.getAnnotation(PersonalInformation.class);
                    try {
                        if(field.get(obj) == null) continue;
                        if(!isDecrypt) {
                            field.set(obj, typeEncFuncs.get(anno.value()).apply((String) field.get(obj)));
                        } else {
                            field.set(obj, typeDecFuncs.get(anno.value()).apply((String) field.get(obj)));
                        }
                    } catch (IllegalAccessException e) {
                        log.error("암복호화에 실패했습니다.", e);
                    }
                } else {
                    try {
                        helper(field.get(obj), isDecrypt);
                    } catch (IllegalAccessException e) {
                        log.error("암복호화에 과정에서 오류가 발생했습니다.", e);
                    }
                }
            }
        }
    }

    public final void setEncFunc(String type, Function<String, String> func){
        this.typeEncFuncs.put(type, func);
    }

    public final void setDecFunc(String type, Function<String, String> func){
        this.typeDecFuncs.put(type, func);
    }
}
