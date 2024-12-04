package com.hollysgang.sample.encryption.framework.core;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class PersonalInformationProcessorCaching implements PersonalInformationProcessor{
    private final PITargetHolder piTargetHolder;
    public PersonalInformationProcessorCaching(PITargetHolder piTargetHolder){
        this.piTargetHolder = piTargetHolder;
    }

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
            Object[] objects = (Object[]) obj;
            for(Object item: objects){
                helper(item, isDecrypt);
            }
        } else if (obj instanceof AbstractDto) {
            Class<?> clazz = obj.getClass();
            PITarget[] piTargets = piTargetHolder.getPITargets(clazz);
            for (PITarget piTarget : piTargets) {
                try {
                    if (piTarget.getIsFinal()) {
                        String targetVal = (String) piTarget.getValue(obj);
                        if (targetVal == null) continue;
                        if (!isDecrypt) {
                            piTarget.setValue(obj, typeEncFuncs.get(piTarget.getPIType()).apply(targetVal));
                        } else {
                            piTarget.setValue(obj, typeDecFuncs.get(piTarget.getPIType()).apply(targetVal));
                        }
                    } else {
                        helper(piTarget.getValue(obj), isDecrypt);
                    }
                } catch (Exception e) {
                    log.error("암복호화에 실패했습니다.", e);
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
