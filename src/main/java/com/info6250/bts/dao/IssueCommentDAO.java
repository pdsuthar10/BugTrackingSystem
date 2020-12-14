package com.info6250.bts.dao;

import com.info6250.bts.pojo.Issue;
import com.info6250.bts.pojo.IssueComment;

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
}
