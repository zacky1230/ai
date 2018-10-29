package com.chineseall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 18:05.
 */
@Controller
public class PageController {

    @RequestMapping("/login")
    public String login() {
        return "loginPage";
    }
}
