package com.info6250.bts.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.info6250.bts.dao.UserDAO;
import com.info6250.bts.pojo.User;
import com.info6250.bts.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@CrossOrigin
@Controller
public class LoginController {
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserValidator userValidator;


	@RequestMapping(value = "/checkForLogin", method = RequestMethod.POST)
	public String handlePostRequest(HttpServletRequest request, UserDAO userDao, Model model, HttpSession session) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = userDao.checkLogin(username, password, passwordEncoder);
		if(user != null)
		{
			model.addAttribute("user", user);

			session.setAttribute("user", user);

			if(user.isAdmin())
				return "redirect:/admin";
			else
				return "redirect:/user";
		}
		else {
			model.addAttribute("error", "Username and password combination dont match!");
			return "login";
		}
	}

	@GetMapping("sign-up.htm")
	public String redirectSignUp(Model model){
		model.addAttribute("user", new User());
		return "sign-up" ;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String handleRegistration(HttpServletRequest request, UserDAO userDao,
									 @ModelAttribute("user") @Valid User user, BindingResult result,
									 SessionStatus status, Model model){

		userValidator.validate(user, result);
		if(result.hasErrors()){
			return "sign-up";
		}

		String username = user.getUsername();
		String password = user.getPassword();
		String confirmPassword = request.getParameter("confirmPassword");


		String name = request.getParameter("name");
		if(confirmPassword == null || confirmPassword.trim().equals("")){
			model.addAttribute("error", "Please confirm your password!");
			return "sign-up";
		}

		if(password.equals(confirmPassword)) {
			userDao.addUser(name, username, password, passwordEncoder);
			return "login";
		}

		model.addAttribute("error", "Passwords no not match");
		return "sign-up";
	}

	@GetMapping("/logout")
	public String logoutUser(HttpSession session){
		session.invalidate();
		return "redirect:/";
	}


}
