package com.info6250.bts.dao;

import com.info6250.bts.pojo.Priority;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PriorityDAO extends DAO{

    public Priority findByName(String name){
        String hql = "FROM Priority where name =: name";
        Query query = getSession().createQuery(hql);
        query.setParameter("name", name);
        return (Priority) query.uniqueResult();
    }

    public List<String> findAllPriorities(){
        String hql = "FROM Priority";
        Query query = getSession().createQuery(hql);
        List<String> result = new ArrayList<>();
        List<Priority> all = query.list();
        for(Priority p : all)
            result.add(p.getName());
        return result;
    }
}
