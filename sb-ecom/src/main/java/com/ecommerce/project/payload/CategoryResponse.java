package com.ecommerce.project.payload;

import java.util.List;

import lombok.Data;

@Data
public class CategoryResponse {

   List<CategoryDTO> categories;
   private int pageNo;
   private int pageSize;
   private long totalElements;
   private int totalPages;
   private boolean lastPage;
}
