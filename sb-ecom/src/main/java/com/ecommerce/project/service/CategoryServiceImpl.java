package com.ecommerce.project.service;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse bulkInsert(List<CategoryDTO> categories) {
        List<Category> categoryList= categories.stream()
        .map(categoryDTO -> modelMapper.map(categoryDTO, Category.class)).toList();
        List<Category> savedCategories= categoryRepository.saveAll(categoryList);
        List<CategoryDTO> categoryDTOs=savedCategories.stream()
        .map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setCategories(categoryDTOs);
        return categoryResponse;
    }

    @Override
    public CategoryResponse getAllCategories(int pageNo, int pageSize,String sortBy,String sortOrder) {

        if(categoryRepository.findAll().isEmpty()){
            throw new APIException("No categories found");
        }
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
            ?Sort.by(sortBy).ascending()
            :Sort.by(sortBy).descending();
        Pageable pageable=PageRequest.of(pageNo, pageSize, sortByAndOrder);
        Page<Category> categoriesPage= categoryRepository.findAll(pageable);
        List<Category> categories=categoriesPage.getContent();
        
        List<CategoryDTO> categoryDTOs= categories.stream()
        .map(category -> modelMapper.map(category, CategoryDTO.class)).toList();

        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setCategories(categoryDTOs);
        categoryResponse.setPageNo(categoriesPage.getNumber());
        categoryResponse.setPageSize(categoriesPage.getSize());
        categoryResponse.setTotalElements(categoriesPage.getTotalElements());
        categoryResponse.setTotalPages(categoriesPage.getTotalPages());
        categoryResponse.setLastPage(categoriesPage.isLast());
       
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category createdCategory=modelMapper.map(categoryDTO, Category.class);
        Category savedCategory=categoryRepository.findByCategoryName(createdCategory.getCategoryName());
        if(savedCategory!=null){
            throw new APIException("Category with name " + categoryDTO.getCategoryName() + " already exists");
        }
        savedCategory=categoryRepository.save(createdCategory);
        CategoryDTO dto=modelMapper.map(savedCategory, CategoryDTO.class);
        return dto;
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
       
        Category category=categoryRepository.findById(categoryId)
        .orElseThrow(()-> new ResourceNotFoundException("category", "categoryId", categoryId));
       

        
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        
        

        Category savedCategory=categoryRepository.findById(categoryId)
                            .orElseThrow(()-> new ResourceNotFoundException("category", "categoryId", categoryId));
        
        Category category=modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(categoryId);
        savedCategory=categoryRepository.save(category);
        
        
        
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

}
