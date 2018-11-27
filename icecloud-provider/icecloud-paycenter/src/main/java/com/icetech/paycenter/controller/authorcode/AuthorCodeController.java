package com.icetech.paycenter.controller.authorcode;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取授权码
 * @author wangzw
 */
@Controller
public class AuthorCodeController {
    @RequestMapping("/code")
    public String getCode(@RequestParam(defaultValue = "获取code请求") String params, HttpServletRequest request, Model model){
        String wxCode = request.getParameter("code");
        String aliCode = request.getParameter("auth_code");
        model.addAttribute("wxCode",wxCode);
        model.addAttribute("aliCode",aliCode);
        return "wxcode";
    }
    @RequestMapping("/")
    public String index(@RequestParam(defaultValue = "首页跳转") String params){
        return "index";
    }
}
