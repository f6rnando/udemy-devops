package com.f6rnando.web.controllers;

import com.f6rnando.backend.persistence.domain.backend.PasswordResetToken;
import com.f6rnando.backend.persistence.domain.backend.User;
import com.f6rnando.backend.service.EmailService;
import com.f6rnando.backend.service.I18NService;
import com.f6rnando.backend.service.PasswordResetTokenService;
import com.f6rnando.backend.service.UserService;
import com.f6rnando.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;

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

    public static final String CHANGE_PASSWORD_VIEW_NAME = "forgotpassword/changePassword";

    private static final String PASSWORD_RESET_ATTR_NAME = "passwordReset";

    private static final String MESSAGE_ATTR_NAME = "message";

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private I18NService i18NService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

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

    @RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.GET)
    public String changeUserPasswordGet(@RequestParam("id") long id, @RequestParam("token") String token,
                                        Locale locale, ModelMap model) {
        if (StringUtils.isEmpty(token) || id == 0) {
            logger.error("Invalid user id {} or token value {}", id, token);
            model.addAttribute(PASSWORD_RESET_ATTR_NAME, "false");
            model.addAttribute(MESSAGE_ATTR_NAME, "Invalid user ID or token value");
        } else {
            PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);

            if (passwordResetToken == null) {
                logger.warn("A token couldn't be found with value {}", token);
                model.addAttribute(PASSWORD_RESET_ATTR_NAME, "false");
                model.addAttribute(MESSAGE_ATTR_NAME, "Token not found");
            } else {
                User user = passwordResetToken.getUser();

                if (user.getId() != id) {
                    logger.error("The user ID {} passes as a param doesn't match with the user ID {} associated with the token {}", id, user.getId(), token);
                    model.addAttribute(PASSWORD_RESET_ATTR_NAME, "false");
                    model.addAttribute(MESSAGE_ATTR_NAME, i18NService.getMessage("resetPassword.token.invalid", locale));
                } else if (LocalDateTime.now(Clock.systemUTC()).isAfter(passwordResetToken.getExpiryDate())) {
                    logger.error("The token {} has expired", token);
                    model.addAttribute(PASSWORD_RESET_ATTR_NAME, "false");
                    model.addAttribute(MESSAGE_ATTR_NAME, i18NService.getMessage("resetPassword.token.expired", locale));
                } else { // Success case
                    model.addAttribute("principalID", user.getId());

                    // Ok to proceed. We auto-authenticate the user so it passes the POST request verification
                    Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        return CHANGE_PASSWORD_VIEW_NAME;
    }

    @RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.POST)
    public String changeUserPasswordPost(@RequestParam("principal_id") long userID,
                                         @RequestParam("password") String password, ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            logger.error("An unauthenticated user tried to invoke the reset password POST method");
            model.addAttribute(PASSWORD_RESET_ATTR_NAME, "false");
            model.addAttribute(MESSAGE_ATTR_NAME, "You're not authorized to perform this request");
        } else {
            User user = (User) auth.getPrincipal();

            if (user.getId() != userID) {
                logger.error("SECURITY BREACH - The user {} is trying to make a password reset request on behalf of {}", user.getId(), userID);
                model.addAttribute(PASSWORD_RESET_ATTR_NAME, "false");
                model.addAttribute(MESSAGE_ATTR_NAME, "You're not authorized to perform this request");
            } else { // Success case
                userService.updateUserPassword(userID, password);
                logger.info("Password successfully updated for user {}", user.getUsername());
                model.addAttribute(PASSWORD_RESET_ATTR_NAME, "true");
            }
        }

        return CHANGE_PASSWORD_VIEW_NAME;
    }
}
