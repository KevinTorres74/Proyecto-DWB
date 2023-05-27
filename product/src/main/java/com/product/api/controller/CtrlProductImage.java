package com.product.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.ProductImage;
import com.product.api.service.SvcProductImage;
import com.product.exception.ApiException;

@RestController
@RequestMapping("/product-image")
public class CtrlProductImage {
	
	@Autowired
	SvcProductImage svc;

	@GetMapping("/{product_id}")
	public ResponseEntity<List<ProductImage>> getProductImages(@PathVariable("product_id") Integer product_id){
		return new ResponseEntity<>(svc.getProductImages(product_id), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse> uploadProductImage(@Valid @RequestBody ProductImage in, BindingResult bindingResult){
		if(bindingResult.hasErrors())
			throw new ApiException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
		return new ResponseEntity<>(svc.createProductImage(in),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteProductImage(@PathVariable("id") Integer id){
		return new ResponseEntity<>(svc.deleteProductImage(id), HttpStatus.OK);
	}

}
