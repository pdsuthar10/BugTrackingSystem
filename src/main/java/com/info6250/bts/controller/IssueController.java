package com.info6250.bts.controller;

import com.info6250.bts.dao.*;
import com.info6250.bts.pojo.Priority;
import com.info6250.bts.pojo.Project;
import com.info6250.bts.pojo.Status;
import com.info6250.bts.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.file.Path;
import java.util.List;

@Controller
public class IssueController {

    @GetMapping("/project/{project_id}/issues/create-issue")
    public String createIssue(@PathVariable(name = "project_id") String project_id,
                              HttpSession session, ProjectDAO projectDao,
                              ProjectUserRoleDAO projectUserRoleDao, Model model){
        try {
            Integer.parseInt(project_id);
        }catch (Exception e){
            User user = (User) session.getAttribute("user");
            if(user.isAdmin())
                return "redirect:/admin";
            else
                return "redirect:/user";
        }
        Project project = projectDao.findProjectById(Integer.parseInt(project_id));
        if(project == null){
            User user = (User) session.getAttribute("user");
            if(user.isAdmin())
                return "redirect:/admin";
            else
                return "redirect:/user";
        }
//        return "create-issue";
        List<User> developers = project.getDevelopers();
        model.addAttribute("developers", developers);
        model.addAttribute("manager", project.getManager());
        model.addAttribute("project", project);
        return "create-issue";
    }

    @PostMapping("/project/{project_id}/issues/submit-issue")
    public String submitIssue(@PathVariable(name = "project_id") String project_id, HttpServletRequest request,
                              HttpSession session, ProjectDAO projectDAO, PriorityDAO priorityDAO,
                              StatusDAO statusDAO, IssueDAO issueDAO, UserDAO userDAO){
        try {
            Integer.parseInt(project_id);
        }catch (Exception e){
            User user = (User) session.getAttribute("user");
            if(user.isAdmin())
                return "redirect:/admin";
            else
                return "redirect:/user";
        }
        Project project = projectDAO.findProjectById(Integer.parseInt(project_id));
        if(project == null){
            User user = (User) session.getAttribute("user");
            if(user.isAdmin())
                return "redirect:/admin";
            else
                return "redirect:/user";
        }

        Priority priority = priorityDAO.findByName(request.getParameter("priority"));
        Status status = statusDAO.findByName("open");
        User assignTo = userDAO.findUserByUsername(request.getParameter("assignTo"));
        User openedBy = (User) session.getAttribute("user");

        issueDAO.addIssue(request.getParameter("title"),request.getParameter("description"),
                priority,status,request.getParameter("issueType"), assignTo, project,
                openedBy);

        return "redirect:/admin";
    }
}
