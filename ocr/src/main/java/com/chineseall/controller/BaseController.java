package com.chineseall.controller;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 16:32.
 */
@Controller
public class BaseController {
    protected double parseHttpDoubleVauleByKey(HttpServletRequest request, String key) {
        if (request.getParameterMap().containsKey(key)) {
            return Double.parseDouble(request.getParameter(key));
        }
        return -0.0;
    }

    protected String parseHttpStringVauleByKey(HttpServletRequest request, String key) {
        if (request.getParameterMap().containsKey(key)) {
            return request.getParameter(key);
        }
        return null;
    }

    protected String parseHttpIntVauleByKey(HttpServletRequest request, String key) {
        if (request.getParameterMap().containsKey(key)) {
            return request.getParameter(key);
        }
        return null;
    }
}
