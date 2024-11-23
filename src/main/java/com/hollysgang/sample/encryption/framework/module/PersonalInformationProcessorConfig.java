package com.hollysgang.sample.encryption.framework.module;

import com.hollysgang.sample.encryption.framework.core.AbstractPersonalInformationProcessor;
import com.hollysgang.sample.encryption.framework.core.PersonalInformationProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class PersonalInformationProcessorConfig {

    @Bean
    PersonalInformationProcessor personalInformationProcessor(CipherManager cipherManager){
        AbstractPersonalInformationProcessor pip = new AbstractPersonalInformationProcessor() {};
        // encryption 설정
        pip.setEncFunc(PersonalInformationType.RRN.name(), (plain) -> {
            String trimmed = plain.replace("-" , "");
            String encryptedLast6digits = cipherManager.encrypt(trimmed.substring(7));
            return plain.substring(0, 7) + encryptedLast6digits;
        });
        pip.setEncFunc(PersonalInformationType.CARD_NUMBER.name(), (plain) -> {
            String trimmed = plain.replace("-" , "");
            String encryptedFirst8digits = cipherManager.encrypt(trimmed.substring(0,8));
            return encryptedFirst8digits + plain.substring(8);
        });
        pip.setEncFunc(PersonalInformationType.ETC.name(), cipherManager::encrypt);

        // decryption 설정
        pip.setDecFunc(PersonalInformationType.RRN.name(), (encrypted)->{
            String decryptedLast6digits = cipherManager.decrypt(encrypted.substring(7));
            return encrypted.substring(0, 7) + decryptedLast6digits;
        });
        pip.setDecFunc(PersonalInformationType.CARD_NUMBER.name(), (encrypted) -> {
            String _encrypted = encrypted.substring(0, encrypted.length() - 8);
            String decryptedFirst8digits = cipherManager.decrypt(_encrypted);
            return decryptedFirst8digits + encrypted.substring(encrypted.length() - 8);
        });
        pip.setDecFunc(PersonalInformationType.ETC.name(), cipherManager::decrypt);

        return pip;
    }
}
