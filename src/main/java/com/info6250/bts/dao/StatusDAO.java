package com.info6250.bts.dao;

import com.info6250.bts.pojo.Status;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class StatusDAO extends DAO{

    public Status findByName(String name){
        String hql = "from Status where name =: name";
        Query query = getSession().createQuery(hql);
        query.setParameter("name", name);
        return (Status) query.uniqueResult();
    }
}
