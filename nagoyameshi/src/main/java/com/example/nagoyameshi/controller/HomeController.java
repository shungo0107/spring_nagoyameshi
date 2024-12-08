package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.repository.RestaurantRepository;

@Controller
public class HomeController {

    private final RestaurantRepository restaurantRepository;        
    
    public HomeController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;            
    }  
	
    @GetMapping("/")
    public String index(Model model) {
        List<Restaurant> newHouses = restaurantRepository.findTop10ByOrderByCreateDateDesc();
        model.addAttribute("newHouses", newHouses);   
        return "index";
    }  
}
