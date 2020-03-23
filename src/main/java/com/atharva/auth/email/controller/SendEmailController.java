package com.atharva.auth.email.controller;

import com.atharva.auth.email.model.ErrorCodes;
import com.atharva.auth.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/send")
public class SendEmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/verify-email/{email}/{userId}/{projectName}")
    public ErrorCodes verifyEmail(@PathVariable(name = "email") String email, @PathVariable(name = "userId") String userId,
                                  @PathVariable(name = "projectName") String projectName) throws IOException, MessagingException, NoSuchAlgorithmException {
        return emailService.sendVerificationEmail(email, userId, projectName);
    }

    @GetMapping("/reset-password/{email}/{userId}/{projectName}")
    public ErrorCodes resetPassword(@PathVariable(name = "email") String email, @PathVariable(name = "userId") String userId,
                                @PathVariable(name = "projectName") String projectName) throws IOException, MessagingException, NoSuchAlgorithmException {
        return emailService.sendResetPassword(email, userId, projectName);
    }

}
