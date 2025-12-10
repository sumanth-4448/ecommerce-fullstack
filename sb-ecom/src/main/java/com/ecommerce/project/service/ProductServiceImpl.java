package com.ecommerce.project.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
       
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        
        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(category);
        Double specialPrice=product.getPrice()*(1-product.getDiscount()/100);
        product.setSpecialPrice(specialPrice);
        product.setImage("default.png");
        Product savedProduct = productRepository.save(product);
        ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);
        return savedProductDTO;


    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products=productRepository.findAll();
        List<ProductDTO> productDTOs=products.stream()
            .map(product -> modelMapper.map(product, ProductDTO.class))
            .toList();
        
        ProductResponse productResponse=new ProductResponse();
        productResponse.setProducts(productDTOs);

        return productResponse;

    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId) {
       
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        
        List<Product> products= productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOs=products.stream()
            .map(product -> modelMapper.map(product, ProductDTO.class))
            .toList();
        
        ProductResponse productResponse=new ProductResponse();
        productResponse.setProducts(productDTOs);

        return productResponse;

    }

}
