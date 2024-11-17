package com.hollysgang.sample.encryption.framework.module;

import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class CipherImpl implements CipherManager {

    private final String AES_ALGORITHM = "AES";
    private final String AES_MODE = "AES/ECB/PKCS5Padding";
    private final String key = "DUMMY_KEY";

    @Override
    public String encrypt(String plainText)  {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), AES_ALGORITHM);

        byte[] encryptedBytes;
        try {
            Cipher cipher = Cipher.getInstance(AES_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encryptedBytes = cipher.doFinal(plainText.getBytes());
        } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException |
                 NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    @Override
    public String decrypt(String cipherText) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), AES_ALGORITHM);

        byte[] decryptedBytes;
        try {
            Cipher cipher = Cipher.getInstance(AES_MODE);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        } catch (NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return new String(decryptedBytes);
    }
}
