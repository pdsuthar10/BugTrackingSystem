package com.info6250.bts.controller;

import com.info6250.bts.dao.ProjectDAO;
import com.info6250.bts.dao.ProjectUserRoleDAO;
import com.info6250.bts.dao.RoleDAO;
import com.info6250.bts.dao.UserDAO;
import com.info6250.bts.pojo.Project;
import com.info6250.bts.pojo.Project_User_Role;
import com.info6250.bts.pojo.User;
import com.info6250.bts.validator.ProjectFormValidator;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    ProjectFormValidator projectFormValidator;
    @Autowired
    ProjectUserRoleDAO projectUserRoleDAO;


    @GetMapping("/admin")
    public String adminDashboard(Model model, ProjectDAO projectDao, ProjectUserRoleDAO projectUserRoleDAO){

        List<Project> projects = projectDao.findAllProjects();
        Map<Integer, User> managerMap = new HashMap<Integer, User>();
        for(Project p : projects){
            Project_User_Role pu = projectUserRoleDAO.findByProjectIDAndRole(p.getId(),"manager");
            managerMap.put(p.getId(),pu.getUser());
        }
        model.addAttribute("projects", projects);
        model.addAttribute("managerMap", managerMap);
        model.addAttribute("projectUserRoleDAO", projectUserRoleDAO);
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
        if(project == null) return "redirect:/admin";
        System.out.println("validator........ page return");
        System.out.println(project.getId()+", "+project.getName()+", "+project.getStartDate()+
                ", "+project.getTargetEndDate()+", "+project.getManager().getName());
        model.addAttribute("project",project);
        model.addAttribute("manager", project.getManager());
        model.addAttribute("users", userDao.findAllusers());
        return "edit-project";
    }

    @PostMapping("/admin/edit-project/{project_id}/submit")
    public String submitChange(@PathVariable(name = "project_id")String id, ProjectDAO projectDao,
                               HttpServletRequest request, Model model, UserDAO userDao,
                               RoleDAO roleDao, ProjectUserRoleDAO projectUserRoleDao,
                               @ModelAttribute("project") Project projectToValidate, BindingResult result,
                               SessionStatus status) throws ParseException {

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
            return "create-project";
        }

        status.setComplete();

        Map<String, String> error = new HashMap<String, String>();
        String manager = request.getParameter("manager");
        System.out.println("manager: "+manager);
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
//        project.updateManager(user);
//        projectToValidate.setId(project.getId());
        System.out.println(projectToValidate);
        projectDao.updateProject(project, user, roleDao, projectUserRoleDao);
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
        System.out.println("After errors resolved....");
        Map<String, String> error = new HashMap<String, String>();

        String manager = request.getParameter("manager");
        System.out.println("manager: "+manager);
        if(manager == null || manager.trim().equals(""))
            error.put("manager", "Please select a manager!");

        if(error.size() > 0){
            model.addAttribute("error", error);
            model.addAttribute("users", userDao.findAllusers());
            return "create-project";
        }

        System.out.println("After error.size resolved....");
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


}
