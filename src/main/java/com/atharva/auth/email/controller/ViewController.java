package com.atharva.auth.email.controller;

import com.atharva.auth.email.client.AdminServiceClient;
import com.atharva.auth.email.model.ErrorCodes;
import com.atharva.auth.email.model.ResetPasswordModel;
import com.atharva.auth.email.model.VerifyEmailModel;
import com.atharva.auth.email.service.VerifyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;

@Controller
@RequestMapping("/view")
public class ViewController {

    @Autowired
    private VerifyService verifyService;

    @Autowired
    private AdminServiceClient adminClient;

    @RequestMapping("/verify/{uuid}")
    public String verifyEmail(@PathVariable(name = "uuid") String uuid, Model model) {
        model.addAttribute("verifyEmailModel", new VerifyEmailModel(uuid, StringUtils.EMPTY));
        return "VerifyEmail";
    }

    @PostMapping("/verify-result")
    public String verifyEmailResult(@ModelAttribute VerifyEmailModel verifyEmailModel) {
        String id = verifyService.verifyCode(verifyEmailModel.getUuid(), verifyEmailModel.getCode());
        if (!id.equals(StringUtils.EMPTY)) {
            ErrorCodes errorCodes = adminClient.adminVerified(id);
            if (errorCodes == ErrorCodes.SUCCESS) {
                return "successVerifyEmail";
            } else {
                return "VerifyEmail";
            }
        } else {
            return "VerifyEmail";
        }
    }

    @RequestMapping("/reset/{uuid}")
    public String resetPassword(@PathVariable(name = "uuid") String uuid, Model model) {
        model.addAttribute("resetPasswordModel", new ResetPasswordModel(uuid, StringUtils.EMPTY, StringUtils.EMPTY));
        return "ResetPassword";
    }

    @PostMapping("/reset-result")
    public String resetPasswordReset(@ModelAttribute ResetPasswordModel resetPasswordModel) {
        String idAndType = verifyService.verifyCodeType(resetPasswordModel.getUuid(), resetPasswordModel.getCode());
        String[] split = idAndType.split(":");
        if (!split[0].equals(StringUtils.EMPTY)) {
            String idEncoded = Base64.getEncoder().encodeToString(split[0].getBytes());
            String passEncoded = Base64.getEncoder().encodeToString(resetPasswordModel.getPass().getBytes());
            ErrorCodes errorCodes = ErrorCodes.UNKNOWN;
            if ("ADMIN".equals(split[1])) {
                errorCodes = adminClient.resetPassword(idEncoded.concat(":").concat(passEncoded));
            }
            if (errorCodes == ErrorCodes.SUCCESS) {
                return "successResetPassword";
            } else {
                return "index";
            }
        } else {
            return "index";
        }
    }
}
