package com.info6250.bts.dao;

import com.info6250.bts.pojo.*;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

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
}
