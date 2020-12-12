package com.info6250.bts.controller;

import com.google.gson.Gson;
import com.info6250.bts.dao.UserDAO;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @RequestMapping(value = "/testController", method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public String testApi(UserDAO userDAO){
        Gson gson = new Gson();
        return gson.toJson(userDAO.findAllusers());
    }
}
