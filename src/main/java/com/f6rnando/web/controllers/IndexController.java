package com.f6rnando.web.controllers;

/*
Created by f6rnando@gmail.com
2018-02-01
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String home() {
        return "index";
    }
}
