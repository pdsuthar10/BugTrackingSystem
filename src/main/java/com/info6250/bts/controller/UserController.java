package com.info6250.bts.controller;

import com.info6250.bts.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @GetMapping("/user")
    public String userDashboard(){ return "user"; }

    @GetMapping("/user/dashboard")
    public String dashboard(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user.isAdmin())
            return "redirect:/admin";
        else
            return "redirect:/user";
    }
}
