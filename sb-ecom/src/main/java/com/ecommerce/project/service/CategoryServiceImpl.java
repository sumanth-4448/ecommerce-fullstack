package com.ecommerce.project.service;


import java.util.List;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
@Service
public class CategoryServiceImpl implements CategoryService {

     
    

     @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
       
        Category category=categoryRepository.findById(categoryId)
        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with categoryId: " + categoryId));
       

        
        categoryRepository.delete(category);
        return "Category deleted successfully with categoryId: " + categoryId;
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        
        

        Category savedCategory=categoryRepository.findById(categoryId)
                            .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with categoryId: " + categoryId));
        
        category.setCategoryId(categoryId);
        savedCategory=categoryRepository.save(category);
        
        
        
        return savedCategory;
    }

}
