package com.f6rnando.web.controllers;

import com.f6rnando.enums.PlansEnum;
import com.f6rnando.web.domain.frontend.ProAccountPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/************************************
 Created by f6rnando@gmail.com
 2018-02-16
 *************************************/

@Controller
public class SignUpController {

    /*      FIELDS      */

    // The application logger
    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    public static final String SIGNUP_URL_MAPPING = "/signup";

    public static final String PAYLOAD_MODEL_KEY_NAME = "/payload";

    public static final String SUBSCRIPTION_VIEW_NAME = "registration/signup";

    /*      METHODS      */

    @RequestMapping(value = SIGNUP_URL_MAPPING, method = RequestMethod.GET)
    public String signUpGet(@RequestParam("planID") int planID, ModelMap model) {

        if (planID != PlansEnum.BASIC.getId() && planID != PlansEnum.PRO.getId()) {
            throw new IllegalArgumentException("The Plan ID " + planID + "is not valid");
        }

        model.addAttribute(PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());

        return SUBSCRIPTION_VIEW_NAME;
    }
}
