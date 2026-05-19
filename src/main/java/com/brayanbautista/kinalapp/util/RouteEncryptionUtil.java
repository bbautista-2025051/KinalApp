package com.brayanbautista.kinalapp.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class RouteEncryptionUtil {

    private static final String SECRET_KEY = "KinalApp@S3cur3!";
    private static final String ALGORITHM  = "AES";


    public static String encrypt(String plainValue) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(plainValue.getBytes());
            // URL-safe Base64 sin '=' para que funcione limpio en rutas
            return Base64.getUrlEncoder().withoutPadding().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar el ID de ruta", e);
        }
    }

    public static String decrypt(String encryptedValue) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getUrlDecoder().decode(encryptedValue);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            throw new RuntimeException("Token de ruta inválido o manipulado", e);
        }
    }

    public static String encryptLong(Long id) {
        return encrypt(String.valueOf(id));
    }

    public static Long decryptLong(String token) {
        return Long.parseLong(decrypt(token));
    }
}