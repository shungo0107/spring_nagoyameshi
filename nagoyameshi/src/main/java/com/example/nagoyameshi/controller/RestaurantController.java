package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;        
    private final FavoriteRepository favoriteRepository;    
    
    public RestaurantController(RestaurantRepository restaurantRepository,
    													ReviewRepository reviewRepository,
    													FavoriteRepository favoriteRepository) {
        this.restaurantRepository = restaurantRepository; 
        this.favoriteRepository = favoriteRepository;  
        this.reviewRepository = reviewRepository;
    }     
  
    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "area", required = false) String area,
                        @RequestParam(name = "price", required = false) Integer price,
                        @RequestParam(name = "order", required = false) String order,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) 
    {
        Page<Restaurant> restaurantPage;
                
        if (keyword != null && !keyword.isEmpty()) {
            if (order != null && order.equals("priceAsc")) {
                restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%", "%" + keyword + "%", pageable);
            } else {
                restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByCreateDateDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
            }            
       } else if (area != null && !area.isEmpty()) {
            if (order != null && order.equals("priceAsc")) {
                restaurantPage = restaurantRepository.findByAddressLikeOrderByPriceAsc("%" + area + "%", pageable);
            } else {
                restaurantPage = restaurantRepository.findByAddressLikeOrderByCreateDateDesc("%" + area + "%", pageable);
            }            
       } else if (price != null) {
            if (order != null && order.equals("priceAsc")) {
                restaurantPage = restaurantRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
            } else {
                restaurantPage = restaurantRepository.findByPriceLessThanEqualOrderByCreateDateDesc(price, pageable);
            }            
       } else {
            if (order != null && order.equals("priceAsc")) {
                restaurantPage = restaurantRepository.findAllByOrderByPriceAsc(pageable);
            } else {
                restaurantPage = restaurantRepository.findAllByOrderByCreateDateDesc(pageable);   
            }            
       }                 
        
        model.addAttribute("restaurantPage", restaurantPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("area", area);
        model.addAttribute("price", price);
        model.addAttribute("order", order);
        
        return "restaurants/index";
    }
    
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer restaurantId, 
    									@PageableDefault(page = 0, size = 6, direction = Direction.ASC) Pageable pageable,
    									@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, 
    									Model model) {
    	
        Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
        Page<Review> review = reviewRepository.findByRestaurantIdOrderByCreateDate(restaurantId,pageable);
        
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("reservationInputForm", new ReservationInputForm());
        model.addAttribute("review", review);
        
        if (userDetailsImpl != null) {
        	User user = userDetailsImpl.getUser();
        	boolean hasUserReviewed = !reviewRepository.findByRestaurantIdAndUserId(restaurantId, user.getId()).isEmpty();
            boolean hasUserFavorite  =  !favoriteRepository.findByRestaurantIdAndUserId(restaurantId, user.getId()).isEmpty();
        	
		    model.addAttribute("hasUserReviewed", hasUserReviewed);
		    model.addAttribute("hasUserFavorite", hasUserFavorite);
		    model.addAttribute("user", user);
        }
        
        return "restaurants/show";
    } 
}