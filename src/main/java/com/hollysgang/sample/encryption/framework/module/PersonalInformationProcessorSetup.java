package com.hollysgang.sample.encryption.framework.module;

import com.hollysgang.sample.encryption.framework.core.PersonalInformationProcessorCaching;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class PersonalInformationProcessorSetup implements ApplicationListener<ApplicationReadyEvent> {

    private final PersonalInformationProcessorCaching pip;
    private final CipherManager cipherManager;
    private final ConfigurableApplicationContext context;


    public PersonalInformationProcessorSetup(
            PersonalInformationProcessorCaching pip,
            CipherManager cipherManager,
            ConfigurableApplicationContext context
    ){
        this.pip = pip;
        this.cipherManager = cipherManager;
        this.context = context;
    }

    @PostConstruct
    public void init() {
        // encryption 설정
        pip.setEncFunc(PersonalInformationType.RRN.name(), (plain) -> {
            String trimmed = plain.replace("-", "");
            String encryptedLast6digits = cipherManager.encrypt(trimmed.substring(7));
            return plain.substring(0, 7) + encryptedLast6digits;
        });
        pip.setEncFunc(PersonalInformationType.CARD_NUMBER.name(), (plain) -> {
            String trimmed = plain.replace("-", "");
            String encryptedFirst8digits = cipherManager.encrypt(trimmed.substring(0, 8));
            return encryptedFirst8digits + plain.substring(8);
        });
        pip.setEncFunc(PersonalInformationType.ETC.name(), cipherManager::encrypt);

        // decryption 설정
        pip.setDecFunc(PersonalInformationType.RRN.name(), (encrypted) -> {
            String decryptedLast6digits = cipherManager.decrypt(encrypted.substring(7));
            return encrypted.substring(0, 7) + decryptedLast6digits;
        });
        pip.setDecFunc(PersonalInformationType.CARD_NUMBER.name(), (encrypted) -> {
            String _encrypted = encrypted.substring(0, encrypted.length() - 8);
            String decryptedFirst8digits = cipherManager.decrypt(_encrypted);
            return decryptedFirst8digits + encrypted.substring(encrypted.length() - 8);
        });
        pip.setDecFunc(PersonalInformationType.ETC.name(), cipherManager::decrypt);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 이 빈은 빈 초기 설정 및 어플리케이션 실행 전 pip에 함수를 주입하기 위해서만 쓰인다.
        // 따라서 어플리케이션 실행 후 빈을 삭제한다.
        context.getBeanFactory().destroyBean(this);
    }
}
