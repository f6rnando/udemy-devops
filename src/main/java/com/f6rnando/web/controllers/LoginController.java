package com.f6rnando.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/************************************
 Created by f6rnando@gmail.com
 2018-02-06
 *************************************/

@Controller
public class LoginController {

    /**
     * The login view name - user/login
     */
    public static final String LOGIN_VIEW_NAME = "user/login";

    @RequestMapping("/login")
    public String login() {
        return LOGIN_VIEW_NAME;
    }
}
