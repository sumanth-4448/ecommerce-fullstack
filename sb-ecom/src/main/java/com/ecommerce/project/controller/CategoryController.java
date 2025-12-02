package com.ecommerce.project.controller;



import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;

import jakarta.validation.Valid;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    
   
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @PostMapping("/api/public/categories/bulkInsert")
    public String bulkInsert(@RequestBody List<CategoryDTO> categories) {
        categoryService.bulkInsert(categories);
        return "Inserted categories successfully";
    }
    

    @GetMapping("/api/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(value = "pageNo",required=false,defaultValue=AppConstants.DEFAULT_PAGE_NUMBER)int pageNo,
            @RequestParam(value="pageSize",required=false,defaultValue=AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
            @RequestParam(value="sortBy",required=false,defaultValue=AppConstants.DEFAULT_SORT_BY)String sortBy,
            @RequestParam(value="sortOrder",required=false,defaultValue=AppConstants.DEFAULT_SORT_ORDER)String sortOrder)
            {
        return new ResponseEntity<>(categoryService.getAllCategories(pageNo, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @PostMapping("/api/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/api/public/categories/{categoryId}")
    public ResponseEntity<String> deleteCategories(@PathVariable Long categoryId) {

            CategoryDTO categoryDTO=categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>("category deleted successfully with ID:" + categoryId+ " is " + categoryDTO.getCategoryName(), HttpStatus.OK);

        
    }

    @PutMapping("/api/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,@PathVariable Long categoryId) {
        
        
            CategoryDTO updatedCategory=categoryService.updateCategory(categoryDTO,categoryId);
            return new ResponseEntity<>("category updated successfully with ID:" + categoryId+ " is " + updatedCategory.getCategoryName(), HttpStatus.OK);
        
    }

}
