package com.product.api.service;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Category;
import com.product.api.repository.RepoCategory;
import com.product.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SvcCategoryImp implements SvcCategory{

    @Autowired
    RepoCategory repo;

    @Override
    public List<Category> getCategories() {
        return repo.findByStatus(1);
    }

    @Override
    public Category getCategory(Integer category_id) {
        Category category = repo.findByCategoryId(category_id);
        if (category == null)
            throw new ApiException(HttpStatus.BAD_REQUEST,"category does not exists");
        else
            return category;
    }

    @Override
    public ApiResponse createCategory(Category category) {
        Category categoryS = (Category) repo.findByCategory(category.getCategory());
        if (categoryS != null) {
            if (categoryS.getStatus() == 0) {
                repo.activateCategory(categoryS.getCategory_id());
                return new ApiResponse("category has been activated");
            } else {
                throw new ApiException(HttpStatus.BAD_REQUEST,"category already exists");
            }
        }
        repo.createCategory(category.getCategory());
        return new ApiResponse("category created");
    }

    @Override
    public ApiResponse updateCategory(Integer category_id, Category category) {
        Category categoryS = (Category) repo.findByCategoryId(category_id);
        if (categoryS == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"category does not exist");
        } else {
            if (categoryS.getStatus() == 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST,"category is not active");
            } else {
                categoryS = (Category) repo.findByCategory(category.getCategory());
                if (categoryS != null)
                    throw new ApiException(HttpStatus.BAD_REQUEST,"category already exists");
                repo.updateCategory(category_id,category.getCategory());
                return new ApiResponse("category update");
            }
        }
    }

    @Override
    public ApiResponse deleteCategory(Integer category_id) {
        Category categoryS = (Category) repo.findByCategoryId(category_id);
        if (categoryS == null)
            throw new ApiException(HttpStatus.NOT_FOUND,"category does not exist");
        else {
            repo.deleteById(category_id);
            return new ApiResponse("category removed");
        }
    }
}
