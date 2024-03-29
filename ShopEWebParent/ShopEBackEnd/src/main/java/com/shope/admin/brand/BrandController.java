package com.shope.admin.brand;

import java.io.IOException;
import java.util.List;

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
import com.shope.admin.category.CategoryService;
import com.shope.common.entity.Brand;
import com.shope.common.entity.Category;

@Controller
public class BrandController {
	
	@Autowired
	private BrandService service;
	
	@Autowired
	private CategoryService categoryService;
	
//	@GetMapping("/brands")
//	public String listAll(Model model) {
//		List<Brand> listBrands = service.listAll();
//		model.addAttribute("listBrands", listBrands);
//		
//		
//		return "brands/brands";
//	}
	
	@GetMapping("/brands")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "name", "asc", null);
	}
	
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword
					)
			 {
		Page<Brand> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Brand> listBrands = page.getContent();
		
		long startCount = (pageNum - 1) * BrandService.BRAND_PER_PAGE + 1 ;
		long endCount = startCount + BrandService.BRAND_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listBrands", listBrands);
	
		return "brands/brands";
	}
	
	
	
	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Create New Brand");
		
		return "brands/brand_form";
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, @RequestParam("fileImage")MultipartFile multipartFile,
			RedirectAttributes ra)throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			
			Brand saveBrand = service.save(brand);
			String uploadDir = "../brand-logos/" + saveBrand.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			service.save(brand);
		}
		
		ra.addFlashAttribute("message", "The Brand has been saved successfully.");
		
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name = "id")Integer id,
			RedirectAttributes ra,
			
			Model model) {
		try {
			Brand brand = service.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			
			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Edit Brand (ID: "+ id + ")");
			
			return "/brands/brand_form";
		}catch(BrandNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/brands";
		}
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id")Integer id,
			Model model,
			RedirectAttributes ra) {
		
		try {
			service.delete(id);
			String brandDir = "../brand-logos/" + id;
			FileUploadUtil.removeDir(brandDir);
			
			ra.addFlashAttribute("message", "The brand Id" + id + " Has been deleted successfully");
		}catch(BrandNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/brands";
	}
}
