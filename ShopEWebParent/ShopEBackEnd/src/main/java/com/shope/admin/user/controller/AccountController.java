 package com.shope.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shope.admin.security.ShopEUserDetails;
import com.shope.admin.user.UserService;
import com.shope.common.entity.User;

@Controller
public class AccountController {
	
	@Autowired
	private UserService service;
	
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal ShopEUserDetails loggedUser,
			Model model) {
		String email = loggedUser.getUsername();
		User user =service.getByEmail(email);
		model.addAttribute("user",user);
		
		return "users/account_form";
	}
}
