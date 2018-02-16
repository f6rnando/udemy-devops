package com.f6rnando.backend.service;

import com.f6rnando.web.domain.frontend.FeedbackPojo;
import org.springframework.mail.SimpleMailMessage;

/************************************
 Created by f6rnando@gmail.com
 2018-02-02
 *************************************/

public interface EmailService {

    /**
     * Sends an email with the content in the Feedback POJO
     * @param feedbackPojo The Feedback POJO
     */
    void sendFeedbackEmail(FeedbackPojo feedbackPojo);

    /**
     * Sends an email with the content of the Simple Mail Message object
     * @param message The object containing the email
     */
    void sendGenericEmailMessage(SimpleMailMessage message);
}
