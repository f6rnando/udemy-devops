package com.f6rnando.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/************************************
 Created by f6rnando@gmail.com
 2018-02-06
 *************************************/

@Controller
public class PayloadController {

    public static final String PAYLOAD_VIEW_NAME = "payload/payload";

    @RequestMapping("/payload")
    public String payload() {
        return PAYLOAD_VIEW_NAME;
    }
}
