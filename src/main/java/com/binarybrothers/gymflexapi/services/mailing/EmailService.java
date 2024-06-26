package com.binarybrothers.gymflexapi.services.mailing;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    public void sendEmail(String emailSender, String eamilAppPassword, String to, String subject, String text) throws MessagingException {
        // Create a new JavaMailSenderImpl object
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Set the SMTP server host and port
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        // Set the sender email and password
        mailSender.setUsername(emailSender);
        mailSender.setPassword(eamilAppPassword);

        // Create a properties object for the additional properties
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");

        // Set the additional properties
        mailSender.setJavaMailProperties(props);

        // Create a new MimeMessage and set its contents
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text,true);

        // Send the email using the JavaMailSenderImpl object
        mailSender.send(message);
    }
}
