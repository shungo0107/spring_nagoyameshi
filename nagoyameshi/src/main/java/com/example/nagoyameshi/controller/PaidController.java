package com.example.nagoyameshi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.PaidInputForm;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.UserService;

@Controller
@RequestMapping("/paid")
public class PaidController {
	
	 private final UserService userService;
	 
	    public PaidController(UserService userService) {
	    	this.userService = userService;
	    }

	@GetMapping
	public String index(Model model) {
		model.addAttribute("paidInputForm", new PaidInputForm());
		return "paid/index";
	}
	
	@GetMapping("/paid/create")
	public String create(@ModelAttribute @Validated PaidInputForm paidInputForm,
										@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
										BindingResult bindingResult,
										RedirectAttributes redirectAttributes) {
		
		 User user = userDetailsImpl.getUser(); 
		 userService.createCredit(user.getId(), paidInputForm);
		
	        if (bindingResult.hasErrors()) {
	            return "paid/index";
	        }
		 
		 redirectAttributes.addFlashAttribute("successMessage", "有料会員情報を登録しました。");    
		 
		 return "redirect:/";
	}

}
