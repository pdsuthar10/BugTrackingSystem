package com.info6250.bts.controller;

import com.info6250.bts.dao.*;
import com.info6250.bts.pojo.*;
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

    @GetMapping("/issues")
    public String issuesList(IssueDAO issueDAO, Model model){
        model.addAttribute("issues", issueDAO.findAllIssues());
        return "issues";
    }

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

    @GetMapping("/project/{project_id}/issues/{issue_id}/details")
    public String issueDetails(@PathVariable(name = "project_id") String project_id,
                               @PathVariable(name = "issue_id") String issue_id,
                               ProjectDAO projectDAO, IssueDAO issueDAO, HttpSession session,
                               Model model){
        try {
            Integer.parseInt(project_id);
            Integer.parseInt(issue_id);
        }catch (Exception e){
            User user = (User) session.getAttribute("user");
            if(user.isAdmin())
                return "redirect:/admin";
            else
                return "redirect:/user";
        }
        Project project = projectDAO.findProjectById(Integer.parseInt(project_id));
        Issue issue = issueDAO.findById(Integer.parseInt(issue_id));
        if(project == null || issue == null){
            User user = (User) session.getAttribute("user");
            if(user.isAdmin())
                return "redirect:/admin";
            else
                return "redirect:/user";
        }


        model.addAttribute("issue", issue);
        return "issue-details";
    }
}
