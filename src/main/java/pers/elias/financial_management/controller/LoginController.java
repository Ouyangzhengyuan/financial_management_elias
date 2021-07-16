package pers.elias.financial_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
    /**
     * 跳转到登录页面
     */
    @RequestMapping("/login")
    public String login(Model model, HttpServletRequest request){
        model.addAttribute("msg", request.getAttribute("msg"));
        return "login";
    }
}
