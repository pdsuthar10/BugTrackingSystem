package com.info6250.bts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {

    @GetMapping("/not-found")
    public String notFound(){
        return "not-found";
    }

    @GetMapping("/unauthorized")
    public String unauthorizedUser(){
        return "unauthorized";
    }

    @GetMapping("/error")
    public String errorPage(HttpServletRequest request, Model model) {
        String error = "";
        
        int errorCode = (int) request.getAttribute("javax.servlet.error.status_code");
        switch (errorCode) {
            case 400: {
                error = "Http Error Code: 400. Bad Request";
                break;
            }
            case 401: {
                error = "Http Error Code: 401. Unauthorized";
                break;
            }
            case 404: {
                error = "Http Error Code: 404. Resource not found";
                break;
            }
            case 405: {
                error = "Please check the url. Resource not available.";
                break;
            }
            case 500: {
                error = "Http Error Code: 500. Internal Server Error";
                break;
            }
            case 403: {
                error = "Http Error Code: 403. Access Denied";
                break;
            }
        }
        model.addAttribute("error", error);
        return "error";
    }
}
