package com.hollysgang.sample.encryption.demo.dto;

import com.hollysgang.sample.encryption.framework.core.AbstractDto;
import com.hollysgang.sample.encryption.framework.core.PersonalInformation;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DemoDto extends AbstractDto {
    @PersonalInformation("RRN")
    private String rrn;

    @PersonalInformation("CARD_NUMBER")
    private String cardNumber;

}
