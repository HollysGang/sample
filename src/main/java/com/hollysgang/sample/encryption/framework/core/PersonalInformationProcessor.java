package com.hollysgang.sample.encryption.framework.core;

public interface PersonalInformationProcessor {
    String encryptPersonalInformation(String type, String plain);

    String decryptPersonalInformation(String type, String encrypted);
}
