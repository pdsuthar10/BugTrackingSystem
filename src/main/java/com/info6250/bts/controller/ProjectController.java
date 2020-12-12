package com.info6250.bts.controller;

import com.info6250.bts.dao.ProjectDAO;
import com.info6250.bts.dao.ProjectUserRoleDAO;
import com.info6250.bts.dao.RoleDAO;
import com.info6250.bts.dao.UserDAO;
import com.info6250.bts.pojo.Project;
import com.info6250.bts.pojo.Project_User_Role;
import com.info6250.bts.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectController {

    @GetMapping("/project/{project_id}/add-developers")
    public String addDevelopers(@PathVariable(name = "project_id") String project_id,
                                HttpSession session, ProjectDAO projectDAO,
                                ProjectUserRoleDAO projectUserRoleDAO, UserDAO userDAO, Model model){
        int id;
        try {
            id = Integer.parseInt(project_id);
        }catch (Exception e){
            return "redirect:/user/dashboard";
        }
        Project project = projectDAO.findProjectById(id);
        if(project == null) return "redirect:/user/dashboard";

        User user = (User) session.getAttribute("user");
        User manager = project.getManager();
        if(user.isAdmin() || user.getUsername().equals(manager.getUsername())){
            List<User> unassignedDevelopers = projectUserRoleDAO.findUnassignedDevelopers(project, userDAO, projectDAO);
            List<User> developers = project.getDevelopers();
            model.addAttribute("unassignedDevelopers", unassignedDevelopers);
            model.addAttribute("project", project);
            model.addAttribute("manager", manager);
            model.addAttribute("developers", developers);
            return "add-developers";
        }else{
            return "redirect:/user/dashboard";
        }

    }

    @PostMapping("/project/{project_id}/add-developers")
    public String addingDevelopers(@PathVariable(name = "project_id") String project_id, HttpServletRequest request,
                                   Model model, UserDAO userDAO, ProjectUserRoleDAO projectUserRoleDAO,
                                   RoleDAO roleDAO, ProjectDAO projectDAO){
        String[] selectedDevelopers = request.getParameterValues("developersSelected");
        String error = "";
        int id = Integer.parseInt(project_id);
        Project project = projectDAO.findProjectById(id);
        User manager = project.getManager();
        if(selectedDevelopers == null || selectedDevelopers.length==0)
        {
            error = "Please select atleast 1 developer to add.";
            model.addAttribute("error", error);
            List<User> unassignedDevelopers = projectUserRoleDAO.findUnassignedDevelopers(project, userDAO, projectDAO);
            List<User> developers = project.getDevelopers();
            model.addAttribute("unassignedDevelopers", unassignedDevelopers);
            model.addAttribute("project", project);
            model.addAttribute("manager", manager);
            model.addAttribute("developers", developers);
            return "add-developers";
        }
        List<User> developers = userDAO.findUsersByUsername(selectedDevelopers);
        projectUserRoleDAO.addDevelopers(Integer.parseInt(project_id),developers,roleDAO, projectDAO);

        return "redirect:/admin";
    }

    @GetMapping("/project/{project_id}/details")
    public String projectDetails(@PathVariable(name = "project_id") String project_id,
                                 ProjectDAO projectDAO, Model model,
                                 ProjectUserRoleDAO projectUserRoleDAO){
        int id;
        try {
            id = Integer.parseInt(project_id);
        }catch (Exception e){
            return "redirect:/user/dashboard";
        }
        Project project = projectDAO.findProjectById(id);
        if(project == null) return "redirect:/user/dashboard";
        User manager = project.getManager();

        model.addAttribute("manager", manager);
        model.addAttribute("project", project);
        model.addAttribute("developers", project.getDevelopers());
        model.addAttribute("issues", project.getIssues());
        return "project-details";
    }
}
