package com.info6250.bts.dao;

import com.info6250.bts.pojo.*;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("projectUserRoleDao")
public class ProjectUserRoleDAO extends DAO{


    public Project_User_Role findByProjectIDAndRole(int project_id, String role_name){
        String hql = "from Project_User_Role where project.id =: project_id and role.name =: role_name";
        Query query = getSession().createQuery(hql);
        query.setParameter("project_id", project_id);
        query.setParameter("role_name", role_name);
        return (Project_User_Role) query.uniqueResult();
    }


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

    public int updateManager(Project project, User manager){
        try {
            begin();
            Project_User_Role project_user_role = findByProjectIDAndRole(project.getId(), "manager");
            project_user_role.setUser(manager);
            getSession().update(project_user_role);
            commit();
        }catch (HibernateException e){
            e.printStackTrace();
            rollback();
            return -1;
        }finally {
            close();
        }
        return 1;
    }

    public int removeDeveloper(Project project, User developer){
        try{
            begin();
            String hql = "FROM Project_User_Role where project.id =: id and role.name =: role and user.username =: username";
            Query query = getSession().createQuery(hql)
                    .setParameter("id", project.getId())
                    .setParameter("role","developer")
                    .setParameter("username", developer.getUsername());
            Project_User_Role p = (Project_User_Role) query.uniqueResult();
            if(p == null) {
                System.out.println("Not found");
                return -1;
            }
            User u = p.getUser();
            List<Issue> assignedIssues = u.getAssignedIssues();
            if(assignedIssues.size() > 0){
                for(Issue issue : assignedIssues){
                    if(issue.getStatus().getName().equals("open"))
                        return -2;
                }
            }
            p.getUser().removeLink(p);
            p.getProject().removeLink(p);
            getSession().delete(p);
            commit();
        }catch (Exception e){
            e.printStackTrace();
            rollback();
            return -1;
        }finally {
            close();
        }
        return 1;
    }

}
