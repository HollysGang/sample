package com.hollysgang.sample.encryption.framework.module;

public enum PersonalInformationType {
    RRN("RRN"),
    CARD_NUMBER("CARD_NUMBER"),
    ETC("ETC");

    static public PersonalInformationType of(String id){
        for(PersonalInformationType type: PersonalInformationType.values()){
            if(type.id.equals(id)){
                return type;
            }
        }
        throw new IllegalArgumentException(id + " id의 PersonalInformationType은 존재하지 않습니다.");
    }

    public final String id;
    PersonalInformationType(String id){
        this.id = id;
    }
}
