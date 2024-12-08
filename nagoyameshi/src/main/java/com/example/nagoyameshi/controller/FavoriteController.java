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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.FavoriteService;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {
	private FavoriteRepository favoriteRepository;
	private FavoriteService favoriteService;	
	
	public FavoriteController(FavoriteRepository favoriteRepository,
											FavoriteService favoriteService) {
		this.favoriteRepository = favoriteRepository;
		this.favoriteService = favoriteService;
	}
	
	@GetMapping("/list")
	public String list(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
								@PageableDefault(page = 0, size = 10, direction = Direction.ASC) Pageable pageable,
								Model model){
		
		Page <Favorite> favorite = favoriteRepository.findByUserId(userDetailsImpl.getUser().getId(),pageable);
		
		model.addAttribute("favoritePage",favorite);
		
		return "/favorite/list";
	}
	
	/* お気に入り追加機能 */
	@PostMapping("/add/{restaurantId}")
	public String add(@PathVariable("restaurantId") Integer restaurantId,
								@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		User user = userDetailsImpl.getUser();
		favoriteService.add(restaurantId, user.getId());
		return "redirect:/restaurants/" + restaurantId;
	}

	/* お気に入り解除機能 */
	@PostMapping("/delete/{restaurantId}")
	public String delete(@PathVariable("restaurantId") Integer restaurantId,
									@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		User user = userDetailsImpl.getUser();
		favoriteService.delete(restaurantId,user.getId());
		return "redirect:/restaurants/" + restaurantId;
	}

}
