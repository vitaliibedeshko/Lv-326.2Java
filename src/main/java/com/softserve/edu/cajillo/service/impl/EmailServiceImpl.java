package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${mail.smtp.host}")
    private String smtpHost;
    @Value("${mail.smtp.port}")
    private int port;
    @Value("${mail.smtp.login}")
    private String login;
    @Value("${mail.smtp.password}")
    private String password;


    public void sendEmail(String email, String topic, String text) {
        new Thread(() -> {
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", true);
            properties.put("mail.smtp.host", smtpHost);
            properties.put("mail.smtp.port", port);
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(login, password);
                }
            });
            try {
                Message message = new MimeMessage(session);
                String systemEmail = "system@cajillo.ga";
                message.setFrom(new InternetAddress(systemEmail));
                message.setRecipients(
                        Message.RecipientType.TO, InternetAddress.parse(email));
                message.setSubject(topic);
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setContent(text, "text/html");
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);
                message.setContent(multipart);
                log.info("Sending letter to email: " + email);
                Transport.send(message);
            } catch (MessagingException e) {
                log.error("Error sending message: " + e);
            }
        }).start();
    }

}