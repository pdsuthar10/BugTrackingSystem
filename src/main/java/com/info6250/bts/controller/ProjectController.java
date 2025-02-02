package com.info6250.bts.controller;

import com.info6250.bts.dao.ProjectDAO;
import com.info6250.bts.dao.ProjectUserRoleDAO;
import com.info6250.bts.dao.RoleDAO;
import com.info6250.bts.dao.UserDAO;
import com.info6250.bts.pojo.Project;
import com.info6250.bts.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProjectController {

    @GetMapping("/user/projects")
    public String getMyProjects(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        model.addAttribute("projects", user.getProjectsManagedByMe());
        return "projects";
    }


    @PostMapping("/project/{project_id}/add-developers")
    public String addingDevelopers(@PathVariable(name = "project_id") String project_id, HttpServletRequest request,
                                   Model model, UserDAO userDAO, ProjectUserRoleDAO projectUserRoleDAO,
                                   RoleDAO roleDAO, ProjectDAO projectDAO){
        String[] selectedDevelopers = request.getParameterValues("developersSelected");
        boolean empty = false;
        for(String s:selectedDevelopers){
            if(s == null || s.trim().equals(""))
                empty = true;
        }
        String error = "";
        int id = Integer.parseInt(project_id);
        Project project = projectDAO.findProjectById(id);
        User manager = project.getManager();
        if(selectedDevelopers == null || selectedDevelopers.length==0 || empty)
        {
            error = "Please select atleast 1 developer to add.";
            model.addAttribute("error", error);
            List<User> unassignedDevelopers = projectUserRoleDAO.findUnassignedDevelopers(project, userDAO, projectDAO);
            List<User> developers = project.getDevelopers();
            model.addAttribute("unassignedDevelopers", unassignedDevelopers);
            model.addAttribute("project", project);
            model.addAttribute("manager", manager);
            model.addAttribute("developers", developers);
            model.addAttribute("issues", project.getIssues());
            return "project-details";
        }
        List<User> developers = userDAO.findUsersByUsername(selectedDevelopers);
        projectUserRoleDAO.addDevelopers(Integer.parseInt(project_id),developers,roleDAO, projectDAO);

        return "redirect:/project/"+project_id+"/details";
    }

    @GetMapping("/project/{project_id}/details")
    public String projectDetails(@PathVariable(name = "project_id") String project_id,
                                 ProjectDAO projectDAO, Model model,
                                 ProjectUserRoleDAO projectUserRoleDAO, UserDAO userDAO){
        int id;
        try {
            id = Integer.parseInt(project_id);
        }catch (Exception e){
            return "redirect:/user/dashboard";
        }
        Project project = projectDAO.findProjectById(id);
        projectDAO.getSession().refresh(project);
        if(project == null) return "redirect:/user/dashboard";
        User manager = project.getManager();

        model.addAttribute("manager", manager);
        model.addAttribute("project", project);
        model.addAttribute("developers", project.getDevelopers());
        model.addAttribute("issues", project.getIssues());
        model.addAttribute("unassignedDevelopers", userDAO.findUnassignedUsersProject(project));
        return "project-details";
    }

    @PostMapping("/project/{project_id}/remove-developer")
    public String removeDeveloper(@PathVariable(name = "project_id") String project_id,
                                  ProjectDAO projectDAO, UserDAO userDAO, ProjectUserRoleDAO projectUserRoleDAO,
                                  Model model, HttpServletRequest request){
        int id;
        try {
            id = Integer.parseInt(project_id);
        }catch (Exception e){
            return "not-found";
        }
        Project project = projectDAO.findProjectById(id);
        if(project == null) return "not-found";

        String username = request.getParameter("developerToRemove");
        if(username == null || username.trim().equals("")) return "redirect:/user/dashboard";

        User user = userDAO.findUserByUsername(username);
        if(user == null) return "not-found";

        int remove = projectUserRoleDAO.removeDeveloper(project, user);
        if(remove == -1){
            return "not-found";
        }
        if(remove == -2){
            model.addAttribute("manager", project.getManager());
            model.addAttribute("project", project);
            model.addAttribute("developers", project.getDevelopers());
            model.addAttribute("issues", project.getIssues());
            model.addAttribute("errorDevelopers", "Developer has been assigned an issue." +
                    "You can remove only after developer has 0 opened issues");
            return "project-details";
        }

        return "redirect:/project/"+project_id+"/details";

    }
}
