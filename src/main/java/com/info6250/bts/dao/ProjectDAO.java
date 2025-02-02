package com.info6250.bts.dao;

import com.info6250.bts.pojo.Project;
import com.info6250.bts.pojo.Project_User_Role;
import com.info6250.bts.pojo.Role;
import com.info6250.bts.pojo.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository("projecDAO")
public class ProjectDAO extends DAO {

    public List<Project> findAllProjects(){
        String hql = "FROM Project";
        Query query = getSession().createQuery(hql);
        query.setComment("Getting all the projects....");
        List<Project> result = query.list();
//        close();
        return result;
    }

    public Project findProjectById(int id){
        begin();
        String hql = "FROM Project where id =: id";
        Query query = getSession().createQuery(hql);
        query.setParameter("id",id);
        Project result = (Project) query.uniqueResult();
        commit();
        return result;
    }

    public int addProject(Project projectToAdd, User manager, RoleDAO roleDao, ProjectUserRoleDAO projectUserRoleDao){
        try{
            begin();
            Role role = roleDao.findRoleByName("manager");
            Project_User_Role project_user_role = new Project_User_Role();
            project_user_role.setRole(role);
            project_user_role.setUser(manager);
            getSession().save(project_user_role);

            projectToAdd.addUser(project_user_role);
            getSession().save(projectToAdd);

            commit();
        }catch (HibernateException e){
            e.printStackTrace();
            rollback();
        }finally {
            close();
        }

        return 1;
    }

    public int updateProject(Project projectToUpdate, User manager, RoleDAO roleDao, ProjectUserRoleDAO projectUserRoleDao){
        try{
            begin();
//            Project_User_Role project_user_role= projectUserRoleDao.findByProjectIDAndRole(projectToUpdate.getId(),"manager");
//            projectToUpdate.removeUser(project_user_role);
//            project_user_role.setUser(manager);

//            projectToUpdate.addUser(project_user_role);
//            projectToUpdate.updateManager(manager);
//            project_user_role.setProject(projectToUpdate);
//            session.update(project_user_role);
            getSession().update(projectToUpdate);
//            getSession().update(project_user_role);
            commit();
//            getSession().refresh(projectToUpdate);
//            getSession().refresh(project_user_role);
        }catch (HibernateException e){
            e.printStackTrace();
            rollback();
            return -1;
        }finally {
            close();
        }
        return 1;
    }

    public int deleteProject(Project project){
        try {
            begin();
            getSession().delete(project);
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
