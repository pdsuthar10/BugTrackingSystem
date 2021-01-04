package com.info6250.bts.dao;

import com.info6250.bts.pojo.Issue;
import com.info6250.bts.pojo.IssueComment;
import org.hibernate.query.Query;

public class IssueCommentDAO extends DAO{

    public int addComment(IssueComment comment){
        try{
            begin();
            getSession().save(comment);
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

    public int deleteCommentsIssue(long id){
        int recordsAffected = -1;
        try{
            String hql = "delete IssueComment where issue.id =: id";
            Query query = getSession().createQuery(hql).setParameter("id", id);
            begin();
            recordsAffected = query.executeUpdate();
            commit();
        }catch (Exception e){
            e.printStackTrace();
            rollback();

        }finally {
            close();
        }
        return recordsAffected;
    }
}
