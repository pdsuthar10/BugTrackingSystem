package com.info6250.bts.dao;

import com.info6250.bts.pojo.*;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class IssueDAO extends DAO{

    public int addIssue(String title, String description, Priority priority, Status status, String issueType,
                        User assignTo, Project project, User openedBy){
        Issue issue = new Issue();
        issue.setIssueType(issueType);
        issue.setProject(project);
        issue.setDescription(description);
        issue.setOpenedBy(openedBy);
        issue.setAssignedTo(assignTo);
        issue.setTitle(title);
        issue.setPriority(priority);
        issue.setStatus(status);
        try {
            begin();
            getSession().save(issue);
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

    public Issue findById(long id){
        String hql = "FROM Issue where id =:id";
        Query query = getSession().createQuery(hql).setParameter("id",id);
        return (Issue) query.uniqueResult();
    }

    public List<Issue> findAllIssues(){
        String hql = "FROM Issue";
        Query query = getSession().createQuery(hql);
        return query.list();
    }

    public List<Issue> findIssuesAssignedOfUser(String username){
        String hql = "FROM Issue where assignedTo.username =: username";
        Query query = getSession().createQuery(hql).setParameter("username", username);
        return query.list();
    }

    public int resolveIssue(String resolutionSummary, Issue issue, User closedBy, Status status){
        try{
            issue.setStatus(status);
            issue.setClosedBy(closedBy);
            issue.setResolutionSummary(resolutionSummary);
            issue.setClosedOn(new Date());
            begin();
            getSession().update(issue);
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

    public int updateIssue(Issue issue){
        try{
            begin();
            getSession().update(issue);
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
}
