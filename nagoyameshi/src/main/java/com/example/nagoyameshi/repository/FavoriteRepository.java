package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer>  {
	public Page<Favorite> findByUserId(Integer userId, Pageable pageable);
	public List<Favorite> findByRestaurantIdAndUserId(Integer restaurantId,Integer userId);
}
