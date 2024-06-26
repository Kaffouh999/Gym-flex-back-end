package com.binarybrothers.gymflexapi.utils;

import io.nayuki.qrcodegen.QrCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Objects;

public class QRCodeGenerator {

    private QRCodeGenerator() {
    }

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

        return stringBuilder.toString();
    }


    public static String generateQRCodeBase64(String text) throws IOException {
        QrCode qr = QrCode.encodeText(text, QrCode.Ecc.LOW); // Make the QR Code symbol
        BufferedImage img = toImage(qr); // Convert to bitmap image

        // Convert BufferedImage to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(img, "png", byteArrayOutputStream);
        byte[] pngData = byteArrayOutputStream.toByteArray();

        return Base64.getEncoder().encodeToString(pngData);
    }

    private static BufferedImage toImage(QrCode qr) {
        int scale = 10;
        int border = 1;
        Objects.requireNonNull(qr);
        if (qr.size + border * 2L > Integer.MAX_VALUE / scale)
            throw new IllegalArgumentException("Scale or border too large");

        BufferedImage result = new BufferedImage((qr.size + border * 2) * scale, (qr.size + border * 2) * scale, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                boolean color = qr.getModule(x / scale - border, y / scale - border);
                result.setRGB(x, y, color ? 0x000000 : 0xFFFFFF);
            }
        }
        return result;
    }

}
