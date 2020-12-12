package com.info6250.bts.interceptor;

import com.info6250.bts.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserInterceptor implements HandlerInterceptor {

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
           return true;
        }
    }
}
