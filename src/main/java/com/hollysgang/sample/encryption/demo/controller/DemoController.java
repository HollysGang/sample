package com.hollysgang.sample.encryption.demo.controller;

import com.hollysgang.sample.encryption.demo.dto.DemoDto;
import com.hollysgang.sample.encryption.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    public DemoService demoService;

    @GetMapping("/api/demo/rrn-encryption")
    public String rrnEncryption(String rrn){
        DemoDto dto = new DemoDto();
        dto.setRrn(rrn);
        demoService.saveRrn(dto);
        return "ok";
    }

    @GetMapping("/api/demo/card-encryption")
    public String cardEncryption(String card){
        DemoDto dto = new DemoDto();
        dto.setCardNumber(card);
        demoService.saveCard(dto);
        return "ok";
    }

    @GetMapping("/api/demo/rrn-decryption")
    public DemoDto rrnDecryption(String rrn){
        return demoService.retrieveRrn(rrn);
    }

    @GetMapping("/api/demo/card-decryption")
    public DemoDto cardDecryption(String card){
        return demoService.retrieveCard(card);
    }
}
