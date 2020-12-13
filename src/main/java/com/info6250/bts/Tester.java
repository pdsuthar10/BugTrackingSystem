package com.info6250.bts;

import com.info6250.bts.dao.ProjectDAO;
import com.info6250.bts.dao.ProjectUserRoleDAO;
import com.info6250.bts.dao.UserDAO;
import com.info6250.bts.pojo.Project;
import com.info6250.bts.pojo.Project_User_Role;
import com.info6250.bts.pojo.Role;
import com.info6250.bts.pojo.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;

public class Tester {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
//        List<User> allUsers = userDAO.findAllusers();
//        for(User u: allUsers)
//            System.out.println(u);
        ProjectDAO projectDAO = new ProjectDAO();
//        User user = userDAO.findUserByUsername("test");
//        Set<Role> roles = userDAO.findRoles(user);
//        for(Role r : roles)
//            System.out.println("Role from tester: "+r.getName());
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        System.out.println(passwordEncoder.encode("admin"));
//        ProjectUserRoleDAO projectUserRoleDAO = new ProjectUserRoleDAO();
        Project project = projectDAO.findProjectById(5);
        List<User> result = userDAO.findUnassignedUsersProject(project);
        for(User u : result){
            System.out.println(u);
        }
//        List<Project_User_Role> assignedUsers = project.getAssignedUsers();
//        System.out.println("Manager: " + project.getManager().getName());
//        for(Project_User_Role p : assignedUsers){
//            System.out.println("User Name: "+p.getUser().getName()+", Role: "+p.getRole().getName());
//        }
//        List<User> unassignedDevelopers = projectUserRoleDAO.findUnassignedDevelopers(project,userDAO, projectDAO);
//        System.out.println("Hereeeeeee........");
//        for(User u:unassignedDevelopers)
//            System.out.println(u);
    }
}
