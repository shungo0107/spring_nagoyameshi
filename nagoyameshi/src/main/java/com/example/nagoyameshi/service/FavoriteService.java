package com.example.nagoyameshi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.UserRepository;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final RestaurantRepository restaurantRepository;
	private final UserRepository userRepository;

	public FavoriteService(FavoriteRepository favoriteRepository, 
										RestaurantRepository restaurantRepository,
										UserRepository userRepository) {
		this.favoriteRepository = favoriteRepository;
		this.restaurantRepository = restaurantRepository;
		this.userRepository = userRepository;
	}

	/* お気に入り追加機能*/
	@Transactional
	public void add(Integer restaurantId, Integer userId) {
		Favorite favorite = new Favorite();
		User user = userRepository.getReferenceById(userId);
		Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
		favorite.setUser(user);
		favorite.setRestaurant(restaurant);
		favoriteRepository.save(favorite);
	}

	/*お気に入り削除機能*/
	@Transactional
	public void delete(Integer restaurantId, Integer userId) {
		List<Favorite> favorites = favoriteRepository. findByRestaurantIdAndUserId(restaurantId,userId);
		if (favorites != null && !favorites.isEmpty()) {
			favoriteRepository.delete(favorites.get(0));
		}
	}
}