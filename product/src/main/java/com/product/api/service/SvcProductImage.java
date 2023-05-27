package com.product.api.service;

import java.util.List;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.ProductImage;

public interface SvcProductImage {
	
	public List<ProductImage> getProductImages(Integer product_id);
	public ApiResponse createProductImage(ProductImage in);
	public ApiResponse deleteProductImage(Integer id);

}
