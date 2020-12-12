package com.info6250.bts.dao;

import com.info6250.bts.pojo.Priority;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class PriorityDAO extends DAO{

    public Priority findByName(String name){
        String hql = "FROM Priority where name =: name";
        Query query = getSession().createQuery(hql);
        query.setParameter("name", name);
        return (Priority) query.uniqueResult();
    }
}
