package com.icetech.cloudcenter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class IndexController {

    @RequestMapping("/")
    public void executeReport(HttpServletResponse response) throws IOException {
        response.getWriter().append("Hello CloudCenter！");
    }
}
