package com.hollysgang.sample.encryption.framework.core;

public interface PersonalInformationProcessor {
    String encryptPersonalInformation(PersonalInformationType type, String plain);

    String decryptPersonalInformation(PersonalInformationType type, String encrypted);
}
