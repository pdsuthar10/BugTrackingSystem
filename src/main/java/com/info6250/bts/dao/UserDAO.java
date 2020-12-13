package com.info6250.bts.dao;

import com.info6250.bts.pojo.Project;
import com.info6250.bts.pojo.Project_User_Role;
import com.info6250.bts.pojo.Role;
import com.info6250.bts.pojo.User;
import org.hibernate.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

//import javax.persistence.Query;
import java.util.*;

@Repository
public class UserDAO extends DAO{

    public User checkLogin(String username, String password, PasswordEncoder passwordEncoder){
        String hql = "FROM User where username=:username";
        Query query = getSession().createQuery(hql);
        query.setParameter("username",username);
        query.setComment("Checking user with the database......");
        User userFound = (User) query.uniqueResult();
        if(userFound == null) return null;
        if(passwordEncoder.matches(password, userFound.getPassword()))
            return userFound;
        return null;
    }

    public int addUser(String name, String username, String password, PasswordEncoder passwordEncoder){
        User userToAdd = new User();
        userToAdd.setUsername(username);
        if(passwordEncoder == null) {
            System.out.println("null");
            return 0;
        }
        userToAdd.setPassword(passwordEncoder.encode(password));
        userToAdd.setName(name);
        begin();
        getSession().save(userToAdd);
        commit();
        close();
        return 1;
    }

    public List<User> findAllusers(){
        String hql = "FROM User where isAdmin = false ";
        Query query = getSession().createQuery(hql);
//        javax.persistence.Query query = getManager().createQuery(hql);
        return (List<User>) query.getResultList();
    }

    public User findUserByUsername(String username){
        String hql = "FROM User where username =:username";
        Query query = getSession().createQuery(hql);
        query.setParameter("username",username);
        return (User) query.uniqueResult();
    }

    public Set<Role> findRoles(User user){
        String hql = "FROM Project_User_Role where user.username =: username";
        System.out.println("in find Roles....");
        Query query = getSession().createQuery(hql);
        query.setParameter("username", user.getUsername());
        List<Project_User_Role> results = query.list();

        Set<Role> roles = new HashSet<Role>();
        for(Project_User_Role p : results){
            roles.add(p.getRole());
        }
        return roles;
    }

    public List<User> findUsersByUsername(String[] usernames){
        List<User> result = new ArrayList<User>();
        for(String username: usernames){
            result.add(findUserByUsername(username));
        }
        return result;
    }
    public List<User> findUnassignedUsersProject(Project project){
        List<User> allUsers = findAllusers();
        List<User> assignedUsers = new ArrayList<>();
        for(Project_User_Role t : project.getAssignedUsers()){
            assignedUsers.add(t.getUser());
        }
        List<User> result = new ArrayList<>();
        for(User u : allUsers){
            if(!assignedUsers.contains(u))
                result.add(u);
        }
        return result;
    }

}