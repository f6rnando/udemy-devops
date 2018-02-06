package com.f6rnando.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/************************************
 Created by f6rnando@gmail.com
 2018-02-02
 *************************************/

public class SmtpEmailService extends AbstractEmailService {

    // The application logger
    private static final Logger logger = LoggerFactory.getLogger(SmtpEmailService.class);

    @Autowired
    private MailSender mailSender;

    @Override
    public void sendGenericEmailMessage(SimpleMailMessage message) {
        logger.debug("Sending email for: {}", message);
        try {
            mailSender.send(message);
            logger.debug("Email sent...");
        } catch (Exception e) {
            logger.error("An error occurred trying to send an email using SMTP Gmail");
        }

    }
}
