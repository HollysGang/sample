package com.hollysgang.sample.encryption.framework.module;

public interface CipherManager {
    String encrypt(String plainText);
    String decrypt(String encrypted);
}
