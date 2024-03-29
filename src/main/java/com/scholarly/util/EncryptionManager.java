package com.scholarly.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class EncryptionManager {

    private static final String KEY_ALGORITHM = "AES";
    private static final String SECRET_KEY_FILE = "secret.key";


    public static String encrypt(String input) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
        byte[] encryptedBytes = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedInput) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedInput);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    private static SecretKey getSecretKey() {
        SecretKey secretKey = loadSecretKeyFromFile();

        if (secretKey == null) {
            try {
                secretKey = generateSecretKey();
                saveSecretKeyToFile(secretKey);
            } catch (Exception ignored) {

            }
            secretKey = loadSecretKeyFromFile();
        }

        return secretKey;
    }

    private static SecretKey generateSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
            keyGenerator.init(256); // You can adjust the key size
            return keyGenerator.generateKey();
        } catch (Exception ignored) {

        }
        return null;
    }

    private static void saveSecretKeyToFile(SecretKey secretKey) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(SECRET_KEY_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(secretKey);
        }
    }

    private static SecretKey loadSecretKeyFromFile() {
        try (FileInputStream fis = new FileInputStream(SECRET_KEY_FILE);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (SecretKey) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }

}
