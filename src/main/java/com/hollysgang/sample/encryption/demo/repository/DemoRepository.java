package com.hollysgang.sample.encryption.demo.repository;

import com.hollysgang.sample.encryption.demo.dto.DemoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoRepository {

    public void saveRrn(DemoDto dto){
        log.info("암호화된 주민번호가 나와야함: {}", dto.getRrn());
    }

    public void saveCard(DemoDto dto){
        log.info("암호화된 카드번호가 나와야함: {}", dto.getCardNumber());
    }

    public DemoDto retrieveRrn(String rrn){
        DemoDto dto = new DemoDto();
        dto.setRrn(rrn);
        return dto;
    }

    public DemoDto retrieveCard(String card){
        DemoDto dto = new DemoDto();
        dto.setCardNumber(card);
        return dto;
    }
}
