package com.f6rnando.web.controllers;

import com.f6rnando.backend.persistence.domain.backend.PasswordResetToken;
import com.f6rnando.backend.persistence.domain.backend.User;
import com.f6rnando.backend.service.EmailService;
import com.f6rnando.backend.service.I18NService;
import com.f6rnando.backend.service.PasswordResetTokenService;
import com.f6rnando.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/************************************
 Created by f6rnando@gmail.com
 2018-02-13
 *************************************/

@Controller
public class ForgotMyPasswordController {

    // The application logger
    private static final Logger logger = LoggerFactory.getLogger(ForgotMyPasswordController.class);

    public static final String EMAIL_ADDRESS_VIEW_NAME = "forgotpassword/emailForm";

    public static final String FORGOT_PASSWORD_URL_MAPPING = "/forgotpassword";

    public static final String MAIL_SENT_KEY = "mailSent";

    public static final String CHANGE_PASSWORD_PATH = "/changeuserpassword";

    public static final String EMAIL_MSG_PROPERTY = "forgotpassword.email.text";

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private I18NService i18NService;

    @Autowired
    private EmailService emailService;

    @Value("${webmaster.email}")
    private String webMasterEmail;

    @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.GET)
    public String forgotPasswordGet() {
        return EMAIL_ADDRESS_VIEW_NAME;
    }

    @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.POST)
    public String forgotPasswordPost(HttpServletRequest request, @RequestParam("email") String email, ModelMap model) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenByEmail(email);

        if (passwordResetToken == null) {
            logger.warn("Couldn't find a password reset token for email {}", email);
        } else {
            User user = passwordResetToken.getUser();
            String token = passwordResetToken.getToken();
            String resetPasswordURL = UserUtils.createPasswordResetUrl(request, user.getId(), token);
            logger.debug("Reset Password URL: {}", resetPasswordURL);

            String emailText = i18NService.getMessage(EMAIL_MSG_PROPERTY, request.getLocale());
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("[f6rnando] - How to reset your password");
            mailMessage.setText(emailText + "\r\n" + resetPasswordURL);
            mailMessage.setFrom(webMasterEmail);

            emailService.sendGenericEmailMessage(mailMessage);
        }

        model.addAttribute(MAIL_SENT_KEY, "true");

        return EMAIL_ADDRESS_VIEW_NAME;
    }
}
