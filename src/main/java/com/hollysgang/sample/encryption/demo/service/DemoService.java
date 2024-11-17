package com.hollysgang.sample.encryption.demo.service;

import com.hollysgang.sample.encryption.demo.dto.DemoDto;
import com.hollysgang.sample.encryption.demo.repository.DemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    @Autowired
    DemoRepository demoRepository;

    public void saveRrn(DemoDto dto){
        demoRepository.saveRrn(dto);
    }

    public void saveCard(DemoDto dto){
        demoRepository.saveCard(dto);
    }

    public DemoDto retrieveRrn(String rrn) {
        return demoRepository.retrieveRrn(rrn);
    }

    public DemoDto retrieveCard(String card) {
        return demoRepository.retrieveCard(card);
    }
}
