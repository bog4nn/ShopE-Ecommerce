package com.shope.admin.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shope.admin.FileUploadUtil;
import com.shope.admin.user.UserNotFoundException;
import com.shope.admin.user.UserService;
import com.shope.admin.user.export.UserCsvExporter;
import com.shope.admin.user.export.UserExcelExporter;
import com.shope.admin.user.export.UserPdfExporter;
import com.shope.common.entity.Role;
import com.shope.common.entity.User;


@Controller
public class UserController {
	
	@Autowired
	private UserService service;
	
	
	@GetMapping("/users")
	public String listFirstPage(Model model) {
		
//		List<User> listUser = service.listAll();
//		model.addAttribute("listUser",listUser);
//		return "users";
		return listByPage(1, model, "id", "asc",null);
	}
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum,
			Model model ,
			@Param("sortField") String sortField, 
			@Param("sortDir")String sortDir,
			@Param("keyword") String keyword
			) {
		System.out.println("Sort Field:" + sortField);
		System.out.println("Sort Field:" + sortDir);
		
		Page<User> page = service.listByPage(pageNum,sortField,sortDir,keyword);
		List<User> listUser = page.getContent();
		
		long startCount = (pageNum -1) * UserService.USER_PER_PAGE + 1;
		long endCount = startCount + UserService.USER_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" :"asc";
		
		model.addAttribute("currentPage",pageNum);
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("startCount",startCount);
		model.addAttribute("endCount",endCount);
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("listUser",listUser);
		model.addAttribute("sortField",sortField);
		model.addAttribute("sortDir",sortDir);
		model.addAttribute("reverseSortDir",reverseSortDir);
		model.addAttribute("keyword",keyword);
		
		return "users/users";
	}
	
	@GetMapping("/users/new")
	public String newUsers(Model model) {
		List<Role> listRole = service.listRole();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user",user);
		model.addAttribute("listRoles",listRole);
		model.addAttribute("pageTitle","Create New User");
		return "/users/user_form";
	}
	
	@PostMapping("users/save")
	public String saveUser(User user, RedirectAttributes redirectAttribute,
			@RequestParam("image") MultipartFile multiparFile ) throws IOException {
		if(!multiparFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multiparFile.getOriginalFilename());
			user.setPhotos(fileName);
			User saveUser = service.save(user);
			
			String uploadDir = "user-photos/" + saveUser.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			
			FileUploadUtil.saveFile(uploadDir, fileName, multiparFile);
		}else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			service.save(user);
		}
		
		System.out.println(user);
		System.out.println(multiparFile.getOriginalFilename());
		
		
		//service.save(user); step 1
		redirectAttribute.addFlashAttribute("message","The user has been save successfully.");
		//return "redirect:/users";step 2
		return getRedirectURLtoAffectedUser(user);
	}
	// Send back to url user updated
	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartOfEmail =  user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id ,
			RedirectAttributes redirectAttribute
			,Model model) {
		try {
			User user = service.get(id);
			List<Role> listRole = service.listRole();
			model.addAttribute("user",user);
			model.addAttribute("listRoles",listRole);
			model.addAttribute("pageTitle","Edit User (ID:" + id +")");
			return "/users/user_form";
		} catch (UserNotFoundException e) {
			redirectAttribute.addFlashAttribute("message",e.getMessage());
			return "redirect:/users";
		}
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id ,
			RedirectAttributes redirectAttribute
			,Model model) {
		try {
			service.delete(id);
			redirectAttribute.addFlashAttribute("message", "The user ID : "+id+" has been delete successfully");
		} catch (UserNotFoundException e) {
			redirectAttribute.addFlashAttribute("message",e.getMessage());
		}
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnableStatus(@PathVariable("id") Integer id ,
			@PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttribute
			) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The user ID: " + id + " has been " + status;
		redirectAttribute.addFlashAttribute("message",message);
		
		return "redirect:/users";
	}
	
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPdf(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
	}
}
