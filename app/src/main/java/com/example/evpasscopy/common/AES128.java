package com.example.evpasscopy.common;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.example.evpasscopy.common.ConstantValue.secret_iv;
import static com.example.evpasscopy.common.ConstantValue.secret_key;

public class AES128 {

    public Key getAESKey() throws Exception {
        String iv;
        Key keySpec;

        String key = secret_key;
        iv = secret_iv;
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");

        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }

        System.arraycopy(b, 0, keyBytes, 0, len);
        keySpec = new SecretKeySpec(keyBytes, "AES");

        return keySpec;
    }

    // 암호화
    public String encrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
        String ivStr = secret_key.substring(0, 16);

        // Set key spec & initial vector spec
        SecretKeySpec keySpec = new SecretKeySpec(secret_key.getBytes(CharEncoding.UTF_8), "AES");

        IvParameterSpec ivSpec = new IvParameterSpec(ivStr.getBytes(CharEncoding.UTF_8));

        // Set cipher of CBC & PKCS5
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        // Encrypt & Base64 encoding
        byte[] encrypted = cipher.doFinal(str.getBytes(CharEncoding.UTF_8));
        String enStr = new String(Base64.encodeBase64(encrypted));

        return enStr;
    }

    public String decrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {

        String ivStr = secret_key.substring(0, 16);

        // Set key spec & initial vector spec
        SecretKeySpec keySpec = new SecretKeySpec(secret_key.getBytes(CharEncoding.UTF_8), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivStr.getBytes(CharEncoding.UTF_8));

        // Set cipher of CBC & PKCS5
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] byteStr = Base64.decodeBase64(str.getBytes());

        return new String(cipher.doFinal(byteStr), CharEncoding.UTF_8);
    }
}
