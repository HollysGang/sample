package com.hollysgang.sample.encryption.demo.repository;

import com.hollysgang.sample.encryption.demo.dto.DemoDto;
import com.hollysgang.sample.encryption.framework.core.PersonalInformationAspect;
import com.hollysgang.sample.encryption.framework.core.PersonalInformationProcessor;
import com.hollysgang.sample.encryption.framework.module.CipherImpl;
import com.hollysgang.sample.encryption.framework.module.PersonalInformationProcessorConfig;
import com.hollysgang.sample.encryption.framework.module.PersonalInformationType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Slf4j
@ContextConfiguration(classes = { CipherImpl.class, PersonalInformationProcessorConfig.class, PersonalInformationAspect.class, DemoRepository.class})
class DemoRepositoryEncryptionAOPTest {

    @Autowired
    PersonalInformationProcessor pip;

    @Autowired
    public DemoRepository demoRepository;

    @Test
    void saveRrn() {
        log.info("repository 클래스: {}", demoRepository.getClass());

        //given
        DemoDto dto = new DemoDto();
        String rrn = "0601121234567";
        dto.setRrn(rrn);

        //execute
        demoRepository.saveRrn(dto);

        //expect
        //로그가 암호화로 찍히는지 확인
    }

    @Test
    void saveCard() {
        //given
        DemoDto dto = new DemoDto();
        String cardNumber = "1111222233334444";
        dto.setCardNumber(cardNumber);

        //execute
        demoRepository.saveCard(dto);

        //expect
        //로그가 암호화로 찍히는지 확인
    }

    @Test
    void retrieveRrn() {
        String rrn = "0601121234567";
        DemoDto dto = demoRepository.retrieveRrn(pip.encryptPersonalInformation(PersonalInformationType.RRN.name(), rrn));
        assertEquals(rrn, dto.getRrn());
    }

    @Test
    void retrieveCard() {
        String cardNumber = "1111222233334444";
        DemoDto dto = demoRepository.retrieveCard(pip.encryptPersonalInformation(PersonalInformationType.CARD_NUMBER.name(), cardNumber));
        assertEquals(cardNumber, dto.getCardNumber());
    }

}