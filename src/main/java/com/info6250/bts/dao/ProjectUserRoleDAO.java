package com.info6250.bts.dao;

import com.info6250.bts.pojo.Project;
import com.info6250.bts.pojo.Project_User_Role;
import com.info6250.bts.pojo.Role;
import com.info6250.bts.pojo.User;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("projectUserRoleDao")
public class ProjectUserRoleDAO extends DAO{

//    public int addEntry(Project project, User user, Role role){
//        Project_User_Role entry = new Project_User_Role();
//        entry.setRole(role);
//        entry.setProject(project);
//        entry.setUser(user);
//        begin();
//        getSession().save(entry);
//        commit();
//        return 1;
//    }

    public Project_User_Role findByProjectIDAndRole(int project_id, String role_name){
        String hql = "from Project_User_Role where project.id =: project_id and role.name =: role_name";
        Query query = getSession().createQuery(hql);
        query.setParameter("project_id", project_id);
        query.setParameter("role_name", role_name);
        return (Project_User_Role) query.uniqueResult();
    }

//    public List<User> findUsersOfProjectByType(Project project, String type){
//        List<Project_User_Role> list = project.getAssignedUsers();
//        List<User> result = new ArrayList<User>();
//        for(Project_User_Role p: list){
//            if(p.getRole().getName().equals(type))
//                result.add(p.getUser());
//        }
//        return result;
//    }

    public List<User> findUnassignedDevelopers(Project project, UserDAO userDAO, ProjectDAO projectDAO){
        List<User> developers = project.getDevelopers();
        User manager = project.getManager();
        List<User> allUsers = userDAO.findAllusers();
        List<User> result = new ArrayList<User>();
        for(User u : allUsers){
            if(!developers.contains(u) && u.getUsername()!=manager.getUsername())
                result.add(u);
        }
        return result;
    }

    public int addDevelopers(int project_id, List<User> developersToAdd, RoleDAO roleDAO,
                             ProjectDAO projectDAO){
        Role role = roleDAO.findRoleByName("developer");
        Project project = projectDAO.findProjectById(project_id);
        begin();
        for(User developer : developersToAdd){
            Project_User_Role p = new Project_User_Role();
            p.setRole(role);
            p.setProject(project);
            p.setUser(developer);
            getSession().save(p);
        }
        commit();
        return 1;
    }

}
