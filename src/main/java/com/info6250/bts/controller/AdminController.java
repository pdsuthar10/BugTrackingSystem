package com.info6250.bts.controller;

import com.info6250.bts.analytics.PdfView;
import com.info6250.bts.dao.*;
import com.info6250.bts.pojo.Issue;
import com.info6250.bts.pojo.Project;
import com.info6250.bts.pojo.User;
import com.info6250.bts.validator.ProjectFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    ProjectFormValidator projectFormValidator;
    @Autowired
    ProjectUserRoleDAO projectUserRoleDAO;

    @GetMapping("/analytics.pdf")
    public ModelAndView pdfView(IssueDAO issueDAO, ModelMap modelMap, ProjectDAO projectDAO){
        List<Issue> allIssues = issueDAO.findAllIssues();
        modelMap.addAttribute("issues", allIssues);
        modelMap.addAttribute("projects", projectDAO.findAllProjects());
        return new ModelAndView(new PdfView(), modelMap);
    }


    @GetMapping("/admin")
    public String adminDashboard(Model model, ProjectDAO projectDao, IssueDAO issueDAO){

        List<Project> projects = projectDao.findAllProjects();
        List<Issue> issues = issueDAO.findAllIssues();
        model.addAttribute("projects", projects);
        model.addAttribute("issues", issues);
        return "admin";
    }

    @GetMapping("/admin/create-project")
    public String redirectToPage(UserDAO userDao, Model model){
        List<User> allUsers = userDao.findAllusers();
        model.addAttribute("users", allUsers);
        model.addAttribute("project", new Project());
        return "create-project";
    }

    @GetMapping("/admin/edit-project/{project_id}")
    public String editProject(@PathVariable(name = "project_id") String id, ProjectDAO projectDao, Model model,
                              UserDAO userDao, ProjectUserRoleDAO projectUserRoleDAO){
        try{
            Integer.parseInt(id);
        }catch (Exception e){
            return "redirect:/admin";
        }
        Project project = projectDao.findProjectById(Integer.parseInt(id));
        projectDao.getSession().refresh(project);
        if(project == null) return "redirect:/admin";

        List<User> users = userDao.findUnassignedUsersProject(project);       
        users.add(project.getManager());
        model.addAttribute("project",project);
        model.addAttribute("manager", project.getManager());
        model.addAttribute("users", users);
       
        return "edit-project";
    }

    @PostMapping("/admin/edit-project/{project_id}/submit")
    public String submitChange(@PathVariable(name = "project_id")String id, ProjectDAO projectDao,
                               HttpServletRequest request, Model model, UserDAO userDao,
                               RoleDAO roleDao, ProjectUserRoleDAO projectUserRoleDao,
                               @Valid @ModelAttribute("project") Project projectToValidate, BindingResult result,
                               SessionStatus status, RedirectAttributes redirectAttributes) throws ParseException {

        try{
            Integer.parseInt(id);
        }catch (Exception e){
            return "redirect:/admin";
        }
        Project project = projectDao.findProjectById(Integer.parseInt(id));
        if(project == null) return "redirect:/admin";

        projectFormValidator.validate(projectToValidate,result);
        if(result.hasErrors()){
            List<User> allUsers = userDao.findAllusers();
            model.addAttribute("users", allUsers);
            return "edit-project";
        }

        status.setComplete();

        Map<String, String> error = new HashMap<String, String>();
        String manager = request.getParameter("manager");
        if(manager == null || manager.trim().equals(""))
            error.put("manager", "Please select a manager!");

        if(error.size() > 0){
            model.addAttribute("error", error);
            model.addAttribute("users", userDao.findAllusers());
            return "edit-project";
        }

        project.setName(request.getParameter("name"));
        project.setDescription(request.getParameter("description"));
        project.setStartDate(projectToValidate.getStartDate());
        project.setTargetEndDate(projectToValidate.getTargetEndDate());
        User user = userDao.findUserByUsername(manager);

        projectDao.updateProject(project, user, roleDao, projectUserRoleDao);
        if(!project.getManager().getUsername().equals(user.getUsername())) projectUserRoleDao.updateManager(project, user);

        return "redirect:/admin";

    }



    @PostMapping("/admin/submit-project")
    public String submitProject(HttpServletRequest request, Model model, ProjectDAO projectDao, UserDAO userDao, RoleDAO roleDao,
                                ProjectUserRoleDAO projectUserRoleDao,
                                @ModelAttribute("project") Project projectToValidate, BindingResult result,
                                SessionStatus status) throws ParseException {
        projectFormValidator.validate(projectToValidate,result);
        if(result.hasErrors()){
            List<User> allUsers = userDao.findAllusers();
            model.addAttribute("users", allUsers);
            return "create-project";
        }

        status.setComplete();

        Map<String, String> error = new HashMap<String, String>();

        String manager = request.getParameter("manager");

        if(manager == null || manager.trim().equals(""))
            error.put("manager", "Please select a manager!");

        if(error.size() > 0){
            model.addAttribute("error", error);
            model.addAttribute("users", userDao.findAllusers());
            return "create-project";
        }


        projectDao.addProject(projectToValidate, userDao.findUserByUsername(manager), roleDao, projectUserRoleDao);
        model.addAttribute("projects", projectDao.findAllProjects());
        return "redirect:/admin";
    }

    @GetMapping("/admin/project/{project_id}/issues")
    public String issuesPageProject(@PathVariable(name = "project_id")String id,
                                    ProjectDAO projectDao){
        try{
            Integer.parseInt(id);
        }catch (Exception e){
            return "redirect:/admin";
        }
        Project project = projectDao.findProjectById(Integer.parseInt(id));

        return "redirect:/admin";
    }

    @GetMapping("/admin/delete-project/{project_id}")
    public String deleteProject(@PathVariable(name = "project_id") String project_id,
                                ProjectDAO projectDAO){
        int id;
        try{
            id = Integer.parseInt(project_id);
        }catch (Exception e){
            return "redirect:/not-found";
        }
        Project project = projectDAO.findProjectById(id);
        if(project == null) return "redirect:/not-found";

        projectDAO.deleteProject(project);
        return "redirect:/admin";
    }

}
