package com.info6250.bts.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.info6250.bts.dao.UserDAO;
import com.info6250.bts.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Controller
public class LoginController {
	@Autowired
	PasswordEncoder passwordEncoder;


	@RequestMapping(value = "/checkForLogin", method = RequestMethod.POST)
	public String handlePostRequest(HttpServletRequest request, UserDAO userDao, Model model, HttpSession session) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = userDao.checkLogin(username, password, passwordEncoder);
		if(user != null)
		{
			model.addAttribute("user", user);
			System.out.println(user);
			session = request.getSession();
			session.setAttribute("user", user);
			System.out.println("Storing in session");
			if(user.isAdmin())
				return "redirect:/admin";
			else
				return "redirect:/user";
		}
		else
			return "login-error";
	}

	@GetMapping("sign-up.htm")
	public String redirectSignUp(){ return "sign-up" ;}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String handleRegistration(HttpServletRequest request, UserDAO userDao){
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String name = request.getParameter("name");
		//validate user details

		if(password.equals(confirmPassword)) {
			userDao.addUser(name, username, password, passwordEncoder);
			return "login";
		}
		return "sign-up";
	}

	@GetMapping("/logout")
	public String logoutUser(HttpSession session){
		session.invalidate();
		return "redirect:/";
	}


}
