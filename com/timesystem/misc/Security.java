package com.timesystem.misc;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dlaird on 11/22/14.
 */
public class Security {
    String text = "Hello World";
    String key = "Bar12345Bar12345"; // 128 bit key

    Key aesKey;
    Cipher cipher;

    public Security() {

        // Create key and cipher
        aesKey = new SecretKeySpec(key.getBytes(), "AES");
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchPaddingException s) {
            s.printStackTrace();
        } catch (NoSuchAlgorithmException s) {
            s.printStackTrace();
        }

    }

    public String decryptText(String password) {
        // decrypt the text
        System.out.println("Decrypting: " + password.getBytes().length + " Bytes");
        try {
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(password.getBytes()));
            return decrypted;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        System.out.println("Couldnt encrypt");
        return null;
    }

    public String encryptText(String password) {
        // encrypt the text
        try {
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(password.getBytes());
            return String.valueOf(encrypted);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        System.out.println("Couldnt encrypt");
        return null;
    }

    public void testMethod() {
        System.out.println("Value should be ---");
        System.out.println(encryptText("Password"));
        System.out.println(decryptText(encryptText("Password")));
    }
}
