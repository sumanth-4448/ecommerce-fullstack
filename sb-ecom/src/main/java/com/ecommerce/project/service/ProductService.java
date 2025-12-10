package com.ecommerce.project.service;

import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.payload.ProductDTO;

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);

    ProductResponse getAllProducts();

    ProductResponse getProductsByCategory(Long categoryId);

}
