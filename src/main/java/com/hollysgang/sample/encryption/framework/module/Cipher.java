package com.hollysgang.sample.encryption.framework.module;

public interface Cipher {
    String encrypt(String plainText);
    String decrypt(String encrypted);
}
