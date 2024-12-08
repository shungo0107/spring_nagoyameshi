package com.example.nagoyameshi.controller;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.service.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
	
	private final CategoryRepository categoryRepository;
	private final CategoryService categoryService;	
	
	public CategoryController(CategoryRepository categoryRepository,
													CategoryService categoryService) {
		this.categoryRepository=categoryRepository;
		this.categoryService=categoryService;		
		
	}
	
	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
										@PageableDefault(page = 0, size = 6, sort = "id", direction = Direction.ASC)  Pageable pageable,
										Model model) {
		
		Page<Category> categoryPage;
		
		if (keyword != null && !keyword.isEmpty()) {
			categoryPage = categoryRepository.findByNameLike("%" + keyword + "%",pageable);
		} else {
			categoryPage = categoryRepository.findAll(pageable);
		}

		model.addAttribute("categoryPage" , categoryPage);
		model.addAttribute("categoryName" , keyword);		
		return "admin/categories/index";
		
	}
	
	@GetMapping("/create")
	public String create(@RequestParam(name = "category", required = true) String categoryName,
										RedirectAttributes redirectAttributes) {
		
		categoryService.create(categoryName);
		
		redirectAttributes.addFlashAttribute("successMessage", "カテゴリを登録しました。");
        
		return "redirect:/admin/categories";
	}
	
	@GetMapping("/update")
	public String update(@RequestParam(name = "categoryEditId", required = true) Integer categoryId,
										@RequestParam(name = "categoryEditName", required = true) String categoryName,
										RedirectAttributes redirectAttributes) {
		
		categoryService.update(categoryId,categoryName);
		
		redirectAttributes.addFlashAttribute("successMessage", "カテゴリを更新しました。");
        
		return "redirect:/admin/categories";
	}
	
	@GetMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer categoryId, 
										RedirectAttributes redirectAttributes) {
		
		categoryService.delete(categoryId);
		
		redirectAttributes.addFlashAttribute("successMessage", "カテゴリを削除しました。");
        
		return "redirect:/admin/categories";
	}
	

}
