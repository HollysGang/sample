package com.hollysgang.sample.encryption.framework.module;

public enum PersonalInformationType {
    RRN("RRN"),
    CARD_NUMBER("CARD_NUMBER"),
    ETC("ETC");

    static public PersonalInformationType of(String name){
        for(PersonalInformationType type: PersonalInformationType.values()){
            if(type.id.equals(name)){
                return type;
            }
        }
        throw new IllegalArgumentException(name + " id의 PersonalInformationType은 존재하지 않습니다.");
    }

    private final String id;
    PersonalInformationType(String name){
        this.id = name;
    }
}
