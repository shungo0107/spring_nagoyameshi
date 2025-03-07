package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.ReservationService;
import com.example.nagoyameshi.service.StripeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReservationController {
    private final ReservationRepository reservationRepository;      
    private final RestaurantRepository restaurantRepository;
    private final ReservationService reservationService;
    private final StripeService stripeService;
    
    public ReservationController(ReservationRepository reservationRepository, 
    													RestaurantRepository restaurantRepository, 
    													ReservationService reservationService,
    													StripeService stripeService) {        
        this.reservationRepository = reservationRepository;
        this.restaurantRepository = restaurantRepository;
        this.reservationService = reservationService;
        this.stripeService = stripeService;
    }    

    @GetMapping("/reservations")
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, 
    									@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
    									Model model) {
        User user = userDetailsImpl.getUser();
        Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreateDateDesc(user, pageable);
        
        model.addAttribute("reservationPage", reservationPage);         
        
        return "reservations/index";
    }
    		
    @GetMapping("/restaurants/{id}/reservations/input")
    public String input(@PathVariable(name = "id") Integer id,
    									@ModelAttribute @Validated ReservationInputForm reservationInputForm,
    									BindingResult bindingResult,
    									RedirectAttributes redirectAttributes,
    									Model model)
    {   
        Restaurant restaurant = restaurantRepository.getReferenceById(id);
        Integer numberOfPeople = reservationInputForm.getNumberOfPeople();   
        Integer capacity = restaurant.getCapacity();
        
        if (numberOfPeople != null) {
            if (!reservationService.isWithinCapacity(numberOfPeople, capacity)) {
                FieldError fieldError = new FieldError(bindingResult.getObjectName(), "numberOfPeople", "人数が定員を超えています。");
                bindingResult.addError(fieldError);                
            }            
        }         
        
        if (bindingResult.hasErrors()) {            
            model.addAttribute("restaurant", restaurant);            
            model.addAttribute("errorMessage", "予約内容に不備があります。"); 
            return "restaurants/show";
        }
        
        redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm);           
        
        return "redirect:/restaurants/{id}/reservations/confirm";
    }    
    
    @GetMapping("/restaurants/{id}/reservations/confirm")
    public String confirm(@PathVariable(name = "id") Integer id,
                          @ModelAttribute ReservationInputForm reservationInputForm,
                          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                          HttpServletRequest httpServletRequest,
                          Model model) 
    {        
        Restaurant restaurant = restaurantRepository.getReferenceById(id);
        User user = userDetailsImpl.getUser();
 
        // 料金を計算する
        Integer price = restaurant.getPrice();        
        Integer amount = reservationService.calculateAmount(reservationInputForm.getNumberOfPeople(), price);
        
        ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(restaurant.getId(), user.getId(), reservationInputForm.getReserveDate(), reservationInputForm.getNumberOfPeople(), amount);
        
        String sessionId = stripeService.createStripeSession(restaurant.getName(), reservationRegisterForm, httpServletRequest);
        
        model.addAttribute("restaurant", restaurant);  
        model.addAttribute("reservationRegisterForm", reservationRegisterForm);       
        model.addAttribute("sessionId", sessionId);
        
        return "reservations/confirm";
    }   
    
    /*
    @PostMapping("/restaurants/{id}/reservations/create")
    public String create(@ModelAttribute ReservationRegisterForm reservationRegisterForm) {                
        reservationService.create(reservationRegisterForm);        
        
        return "redirect:/reservations?reserved";
    }
    */
}

