package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Restaurant;

public interface RestaurantRepository extends  JpaRepository<Restaurant, Integer>{
	public Page<Restaurant> findByNameLike(String keyword, Pageable pageable);
	
    public Page<Restaurant> findByNameLikeOrAddressLikeOrderByCreateDateDesc(String nameKeyword, String addressKeyword, Pageable pageable);  
    public Page<Restaurant> findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable);  
    public Page<Restaurant> findByAddressLikeOrderByCreateDateDesc(String area, Pageable pageable);
    public Page<Restaurant> findByAddressLikeOrderByPriceAsc(String area, Pageable pageable);
    public Page<Restaurant> findByPriceLessThanEqualOrderByCreateDateDesc(Integer price, Pageable pageable);
    public Page<Restaurant> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable); 
    public Page<Restaurant> findAllByOrderByCreateDateDesc(Pageable pageable);
    public Page<Restaurant> findAllByOrderByPriceAsc(Pageable pageable);  

    public List <Restaurant> findTop10ByOrderByCreateDateDesc();
    
}
