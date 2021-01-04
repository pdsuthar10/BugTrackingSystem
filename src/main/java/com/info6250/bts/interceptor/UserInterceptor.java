package com.info6250.bts.interceptor;

import com.info6250.bts.dao.UserDAO;
import com.info6250.bts.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    UserDAO userDAO;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null) {
            response.sendRedirect("http://localhost:8080/bts/");
            return false;
        }
        else
        {
            String requestUrl = request.getRequestURI();
            if(requestUrl.endsWith("issues")){
                Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                if(pathVariables.get("user_id") == null)
                    return true;
                UUID user_id = UUID.fromString(pathVariables.get("user_id"));
                User u = userDAO.findById(user_id);
                if(u == null){
                    response.sendRedirect("http://localhost:8080/bts/not-found");
                    return false;
                }
            }

           return true;
        }
    }
}
