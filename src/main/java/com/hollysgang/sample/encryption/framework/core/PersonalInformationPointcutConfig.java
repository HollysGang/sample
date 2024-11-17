package com.hollysgang.sample.encryption.framework.core;

public class PersonalInformationPointcutConfig {
    private String encryptionPointCut;

    public void setEncryptionPointCut(String encryptionPointcut){
        this.encryptionPointCut = encryptionPointcut;
    }

    public String getEncryptionPointcut(){
        return encryptionPointCut;
    };
}
