package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;    
    
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;        
    }    
    
	@Transactional
	public void create (String categoryName) {
		Category category = new Category();
		
		category.setName(categoryName);
		
		categoryRepository.save(category);
	}
	
	@Transactional
	public void update (Integer id,String categoryName) {
		Category category = categoryRepository.getReferenceById(id);
		
		category.setName(categoryName);
		
		categoryRepository.save(category);
	}
	
	@Transactional
	public void delete (Integer id) {
		categoryRepository.deleteById(id);
	}

}
