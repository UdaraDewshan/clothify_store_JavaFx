package util;

import java.security.MessageDigest;
import java.util.Base64;

public class PasswordUtil {

    public static String encryptPassword(String plainTextPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(plainTextPassword.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }
}