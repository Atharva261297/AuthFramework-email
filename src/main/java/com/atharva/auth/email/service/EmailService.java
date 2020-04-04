package com.atharva.auth.email.service;

import com.atharva.auth.email.model.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private VerifyService verifyService;
    
    @Value("${url.self}")
    private String serverAddress;

    private static final String verifyEmail = "verify/";
    private static final String resetEmail = "reset/";

    @Autowired
    ResourceLoader resourceLoader;

    public ErrorCodes sendVerificationEmail(String email, String userId, String projectName) throws IOException, MessagingException, NoSuchAlgorithmException {
        MimeMessage msg = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, "utf-8");
        helper.setTo(email);
        helper.setSubject("Verify Email Id for " + projectName);

        StringBuilder text = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(resourceLoader.getResource("classpath:templates/emails/verify-email.html").getInputStream()));
        br.lines().forEach(text::append);
        String msgText = text.toString();
        String uuid = verifyService.createUuid(userId);
        msgText = msgText.replace("{appName}", projectName)
                .replace("{userId}", userId)
                .replace("{code}", verifyService.createCode(uuid))
                .replaceAll("\\{link}", serverAddress.concat(verifyEmail).concat(uuid));
        helper.setText(msgText, true);
        emailSender.send(msg);
        return ErrorCodes.SUCCESS;
    }

    public ErrorCodes sendResetPassword(String email, String userId, String projectName, String type) throws IOException, MessagingException, NoSuchAlgorithmException {
        MimeMessage msg = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, "utf-8");
        helper.setTo(email);
        helper.setSubject("Reset Password for " + projectName);

        StringBuilder text = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(resourceLoader.getResource("classpath:templates/emails/reset-password.html").getInputStream()));
        br.lines().forEach(text::append);
        String msgText = text.toString();
        String uuid = verifyService.createUuidType(userId, type);
        msgText = msgText.replace("{appName}", projectName)
                .replace("{userId}", userId)
                .replace("{code}", verifyService.createCode(uuid))
                .replaceAll("\\{link}", serverAddress.concat(resetEmail).concat(uuid));
        helper.setText(msgText, true);
        emailSender.send(msg);
        return ErrorCodes.SUCCESS;
    }
}
