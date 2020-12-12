package com.info6250.bts.dao;

import com.info6250.bts.pojo.Role;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository("roleDao")
public class RoleDAO extends DAO{

//    public int addRole(int id, String name){
//        Role roleToAdd = new Role();
//        roleToAdd.setName(name);
//        roleToAdd.setId(id);
//        begin();
//        getSession().save(roleToAdd);
//        commit();
//        return 1;
//    }

    public Role findRoleByName(String name){
        String hql = "FROM Role where name =: name";
        Query query = getSession().createQuery(hql);
        query.setParameter("name", name);
        return (Role) query.uniqueResult();
    }
}
