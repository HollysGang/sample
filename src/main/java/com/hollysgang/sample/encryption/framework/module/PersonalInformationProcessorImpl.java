package com.hollysgang.sample.encryption.framework.module;

import com.hollysgang.sample.encryption.framework.core.PersonalInformationProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class PersonalInformationProcessorImpl implements PersonalInformationProcessor {

    private final Cipher cipher;

    @Override
    public String encryptPersonalInformation(String type, String plain) {
        PersonalInformationType piType = PersonalInformationType.of(type);
        switch (piType){
            case RRN -> {
                String trimmed = plain.replace("-" , "");
                String encryptedLast6digits = cipher.encrypt(trimmed.substring(7));
                return plain.substring(0, 7) + encryptedLast6digits;
            }
            case CARD_NUMBER -> {
                String trimmed = plain.replace("-" , "");
                String encryptedFirst8digits = cipher.encrypt(trimmed.substring(0,8));
                return encryptedFirst8digits + plain.substring(8);
            }
            case ETC -> {
                return cipher.encrypt(plain);
            }
        }
        log.error("암호화 타입이 명확하지 않아, 암호화에 실패했습니다.");
        return plain;
    }

    @Override
    public String decryptPersonalInformation(String type, String encrypted) {
        PersonalInformationType piType = PersonalInformationType.of(type);
        switch (piType){
            case RRN -> {
                String decryptedLast6digits = cipher.decrypt(encrypted.substring(7));
                return encrypted.substring(0, 7) + decryptedLast6digits;
            }
            case CARD_NUMBER -> {
                String decryptedFirst8digits = cipher.decrypt(encrypted.substring(0,8));
                return decryptedFirst8digits + encrypted.substring(8);
            }
            case ETC -> {
                return cipher.decrypt(encrypted);
            }
        }
        log.error("암호화 타입이 명확하지 않아, 복호화에 실패했습니다.");
        return encrypted;
    }
}
