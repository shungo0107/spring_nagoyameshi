package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewEditForm;
import com.example.nagoyameshi.form.ReviewInputForm;
import com.example.nagoyameshi.form.ReviewRegisterForm;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.ReviewService;

@Controller
@RequestMapping("/review")
public class ReviewController {
	 private final ReviewRepository reviewRepository;
	 private final ReviewService reviewService;
	 private final RestaurantRepository restaurantRepository;
	 
	    public ReviewController(ReviewRepository reviewRepository,
	    		                             RestaurantRepository restaurantRepository,
	    		                             ReviewService reviewService) {
	        this.reviewRepository = reviewRepository;
	        this.restaurantRepository = restaurantRepository;
	        this.reviewService = reviewService;
	    }    
	    
	    @GetMapping("/list/{id}")
	    public String index( @PathVariable(name = "id") Integer id,
	                                  @PageableDefault(page = 0, size = 6, direction = Direction.ASC) Pageable pageable,
	                                  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
	    		                      Model model) {

	    	Restaurant restaurant = restaurantRepository.getReferenceById(id);
	    	Page<Review> review = reviewRepository. findByRestaurantId(id, pageable);

	        model.addAttribute("review",review);
	        model.addAttribute("restaurant",restaurant);  
	        
	        if (userDetailsImpl != null) {
	        	User user = userDetailsImpl.getUser();
		        model.addAttribute("user",user);
	        	
	        }
	        
	        return "review/list";
	    } 
	   
	    @GetMapping("/register/{id}")
	    public String register(@PathVariable(name = "id") Integer id,
	    		                         Model model) {
	    	
	    	Restaurant restaurant = restaurantRepository.getReferenceById(id);
	    		    	
	    	 model.addAttribute("reviewInputForm",new ReviewInputForm());
	    	 model.addAttribute("restaurant",restaurant);  
	    	 
	    	return "review/register";
	    }
	    
	    @PostMapping("/create/{id}")	    
	     public String create(@ModelAttribute 
                                        @Validated ReviewInputForm reviewInputForm, 
                                        BindingResult bindingResult,
                       	    		    @PathVariable(name = "id") Integer restaurantId,
                       	    		    @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {        
	    	 
	    	 if (bindingResult.hasErrors()) {
	    		 // エラーがあった場合にリターンし、エラーが発生している箇所にエラーメッセージを表示する
	    		 Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
	    		 model.addAttribute("restaurant",restaurant);  
	    		 return "review/register";
	    		 }
	         User user = userDetailsImpl.getUser(); 
	    	 ReviewRegisterForm reviewRegisterForm = new  ReviewRegisterForm(restaurantId,user.getId(),reviewInputForm.getScore(),reviewInputForm.getComment());
	    	 reviewService.create(reviewRegisterForm);
	    	 
	    	 return "redirect:/restaurants/" + restaurantId;
	    }
	    
	    @GetMapping("/edit/{id}")
	    public String edit(@PathVariable(name = "id") Integer id,
	    		                         Model model) {
	    	
	    	Restaurant restaurant = restaurantRepository.getReferenceById(id);
	    	Review review = reviewRepository.getReferenceById(id);
	    	ReviewEditForm reviewEditForm = new ReviewEditForm(id,review.getAssessment(),review.getComment());
	    	
	    	 model.addAttribute("reviewEditForm",reviewEditForm);
	    	 model.addAttribute("restaurant",restaurant);  
	    	 
	    	return "review/edit";
	    }
	    
	    @PostMapping("/update/{id}")
	    public String update(@ModelAttribute @Validated ReviewEditForm reviewEditForm,
	    		                        BindingResult bindingResult,
	    		                        @PathVariable(name = "id") Integer id,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {
	    	
	    	Review review = reviewRepository.getReferenceById(reviewEditForm.getId());
	    	
	    	 if (bindingResult.hasErrors()) {
	    		 // エラーがあった場合にリターンし、エラーが発生している箇所にエラーメッセージを表示する
	    		 Restaurant restaurant = restaurantRepository.getReferenceById(review.getRestaurant().getId());
	    		 model.addAttribute("restaurant",restaurant);  
	    		 return "review/edit";
	    		 }
	    	 
	    	reviewService.update(reviewEditForm);

	    	return "redirect:/restaurants/" + review.getRestaurant().getId();
	    }
	    
	    @PostMapping("/delete/{reviewId}")
	    public String delete(@PathVariable(name = "reviewId") Integer reviewId,
	    								RedirectAttributes redirectAttributes) {

	    	
	    	Review review = reviewRepository.getReferenceById(reviewId);
	    	Integer restaurantId = review.getRestaurant().getId();
	        reviewRepository.deleteById(reviewId);
	        
        	return "redirect:/restaurants/" + restaurantId ;
	        }    
}