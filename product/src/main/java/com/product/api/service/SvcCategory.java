package com.product.api.service;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Category;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface SvcCategory {

    List<Category> getCategories();

    Category getCategory(Integer category_id);

    ApiResponse createCategory(Category category);

    ApiResponse updateCategory(Integer category_id, Category category);

    ApiResponse deleteCategory(Integer category_id);
}
