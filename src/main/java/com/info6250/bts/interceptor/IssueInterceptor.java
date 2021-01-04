package com.info6250.bts.interceptor;

import com.info6250.bts.dao.IssueDAO;
import com.info6250.bts.pojo.Issue;
import com.info6250.bts.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class IssueInterceptor implements HandlerInterceptor {

    @Autowired
    IssueDAO issueDAO;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            response.sendRedirect("http://localhost:8080/bts");
            return false;
        }

        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long issue_id;
        try {
            issue_id = Long.valueOf(pathVariables.get("issue_id"));
        }catch (Exception e){
            response.sendRedirect("http://localhost:8080/bts/not-found");
            return false;
        }

        Issue issue = issueDAO.findById(issue_id);
        if(issue == null){
            response.sendRedirect("http://localhost:8080/bts/not-found");
            return false;
        }

        if(!(user.isAdmin() || user.getUsername().equals(issue.getProject().getManager().getUsername())
           || user.getUsername().equals(issue.getAssignedTo().getUsername()))){
            response.sendRedirect("http://localhost:8080/bts/unauthorized");
            return false;
        }

        return true;
    }
}
