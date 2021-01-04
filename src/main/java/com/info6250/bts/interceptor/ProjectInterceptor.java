package com.info6250.bts.interceptor;

import com.info6250.bts.dao.IssueDAO;
import com.info6250.bts.dao.ProjectDAO;
import com.info6250.bts.pojo.Issue;
import com.info6250.bts.pojo.Project;
import com.info6250.bts.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class ProjectInterceptor implements HandlerInterceptor {

    @Autowired
    ProjectDAO projectDAO;
    @Autowired
    IssueDAO issueDAO;
    final static String unauthorized = "http://localhost:8080/bts/unauthorized";
    final static String notFound = "http://localhost:8080/bts/not-found";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User userInSession = (User) request.getSession().getAttribute("user");
        if(userInSession == null){
            response.sendRedirect(unauthorized);
            return false;
        }
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String requestUrl = request.getRequestURI();
        int project_id;
        try{
            project_id = Integer.parseInt(pathVariables.get("project_id"));
        }catch (Exception e){
            response.sendRedirect(notFound);
            return false;
        }

        Project project = projectDAO.findProjectById(project_id);
        if(project == null){
            response.sendRedirect(notFound);
            return false;
        }

        if(requestUrl.endsWith("add-developers") || requestUrl.endsWith("remove-developer")){

            User manager = project.getManager();

            if(!(userInSession.isAdmin() || userInSession.getUsername().equals(manager.getUsername()))){
                response.sendRedirect(unauthorized);
                return false;
            }

        }

        if(requestUrl.contains("issues")){
            if(requestUrl.endsWith("create-issue") || requestUrl.endsWith("submit-issue")){
                return true;
            }
            long issue_id;
            try {
                issue_id = Long.parseLong(pathVariables.get("issue_id"));
            }catch (Exception e){
                response.sendRedirect(notFound);
                return false;
            }
            Issue issue = issueDAO.findById(issue_id);
            if(issue == null){
                response.sendRedirect(notFound);
                return false;
            }
            if(requestUrl.endsWith("edit-issue")
                    || requestUrl.endsWith("submit-change")
                    || requestUrl.endsWith("delete-issue")){
                if(!(userInSession.isAdmin() || userInSession.getUsername().equals(project.getManager().getUsername())
                    || userInSession.getUsername().equals(issue.getAssignedTo().getUsername()))){
                    response.sendRedirect(unauthorized);
                    return false;
                }
            }
        }

        return true;
    }
}
