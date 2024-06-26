package com.binarybrothers.gymflexapi.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class QRCodeGenerator {

    public static String generateUniqueCode() throws NoSuchAlgorithmException {
        // Get current timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(formatter);

        // Generate a random number
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        int randomNumber = secureRandom.nextInt(10000);

        // Combine timestamp and random number to create a unique code
        String code = timestamp + "-" + randomNumber;

        // Hash the code using SHA-256
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(code.getBytes());

        // Convert the hash to a hexadecimal string
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hash) {
            stringBuilder.append(String.format("%02x", b));
        }
        String hashedCode = stringBuilder.toString();

        return hashedCode;
    }

}
