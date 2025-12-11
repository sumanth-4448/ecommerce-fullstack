package com.ecommerce.project.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.project.exceptions.APIException;
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

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;
    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
       
        if(!productRepository.findByProductNameContainingIgnoreCase(productDTO.getProductName()).isEmpty()){
            throw new APIException("Product with name "+productDTO.getProductName()+" already exists");
        }

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
    public ProductResponse getAllProducts(int pageNo, int pageSize, String sortBy, String sortOrder) {
        
        if(productRepository.findAll().isEmpty()){
            throw new APIException("No products found");
        }
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")
            ?Sort.by(sortBy).ascending()
            :Sort.by(sortBy).descending();
        Pageable pageable=PageRequest.of(pageNo, pageSize, sortByAndOrder);
        Page<Product> productsPage= productRepository.findAll(pageable);
        List<Product> products=productsPage.getContent();
        List<ProductDTO> productDTOs=products.stream()
            .map(product -> modelMapper.map(product, ProductDTO.class))
            .toList();
        
        ProductResponse productResponse=new ProductResponse();
        productResponse.setProducts(productDTOs);
        productResponse.setPageNo(productsPage.getNumber());
        productResponse.setPageSize(productsPage.getSize());        
        productResponse.setTotalElements(productsPage.getTotalElements());
        productResponse.setTotalPages(productsPage.getTotalPages());
        productResponse.setLastPage(productsPage.isLast());

        return productResponse;

    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId) {
       
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        
        List<Product> products= productRepository.findByCategoryOrderByPriceAsc(category);
        if(products.isEmpty()){
            throw new APIException("No products found in category "+category.getCategoryName());
        }
        List<ProductDTO> productDTOs=products.stream()
            .map(product -> modelMapper.map(product, ProductDTO.class))
            .toList();
        
        ProductResponse productResponse=new ProductResponse();
        productResponse.setProducts(productDTOs);

        return productResponse;

    }

    @Override
    public ProductResponse searchProductsByKeyword(String keyword) {
       
        List<Product> products=productRepository.findByProductNameContainingIgnoreCase(keyword);
        if(products.isEmpty()){
            throw new APIException("No products found with keyword "+keyword);
        }
        List<ProductDTO> productDTOs=products.stream()
            .map(product->(modelMapper.map(product, ProductDTO.class)))
            .toList();
        
        ProductResponse productResponse=new ProductResponse();
        productResponse.setProducts(productDTOs);

        return productResponse;

    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        if(existingProduct==null){
            throw new APIException("Product with id "+productId+" not found");
        }
        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setDiscount(productDTO.getDiscount());  
        Double specialPrice=existingProduct.getPrice()*(1-existingProduct.getDiscount()/100);
        existingProduct.setSpecialPrice(specialPrice);

        productRepository.save(existingProduct);

        return modelMapper.map(existingProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {

        Product product=productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        productRepository.deleteById(productId);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        //get the product from db
        Product productFromDB=productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        //upload image to server
        //get the filename of the uploaded image
        
        String fileName=fileService.uploadImage(path,image);

        //updating the new file name to the product
        productFromDB.setImage(fileName);
        //return productDTO
        productRepository.save(productFromDB);
        return modelMapper.map(productFromDB, ProductDTO.class);

    }
    

}
