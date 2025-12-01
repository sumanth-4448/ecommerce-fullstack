package com.ecommerce.project.payload;

import java.util.List;

import lombok.Data;

@Data
public class CategoryResponse {

   List<CategoryDTO> categories;
}
