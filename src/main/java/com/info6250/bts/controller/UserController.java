package com.info6250.bts.controller;

import com.info6250.bts.dao.IssueDAO;
import com.info6250.bts.dao.ProjectDAO;
import com.info6250.bts.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @GetMapping("/user")
    public String userDashboard(ProjectDAO projectDAO, IssueDAO issueDAO, Model model){
        model.addAttribute("projects", projectDAO.findAllProjects());
        model.addAttribute("issues", issueDAO.findAllIssues());
        return "user";
    }

    @GetMapping("/user/dashboard")
    public String dashboard(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user.isAdmin())
            return "redirect:/admin";
        else
            return "redirect:/user";
    }
}
