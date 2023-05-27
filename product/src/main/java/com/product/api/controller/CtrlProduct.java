package com.product.api.controller;

import javax.annotation.processing.SupportedSourceVersion;
import javax.validation.Valid;

import com.product.api.dto.CategoryDTO;
import com.product.api.dto.ProductResponse;
import com.product.api.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Product;
import com.product.api.service.SvcProduct;
import com.product.exception.ApiException;

import java.util.List;

@RestController
@RequestMapping("/product")
public class CtrlProduct {

	@Autowired
	SvcProduct svc;

	@GetMapping("/{gtin}")
	public ResponseEntity<Product> getProduct(@PathVariable String gtin){
		return new ResponseEntity<>(svc.getProduct(gtin),HttpStatus.OK);
	}

	@GetMapping("/category/{category_id}")
	public ResponseEntity<List<ProductResponse>> listProducts(@PathVariable("category_id") Integer category_id){
		return new ResponseEntity<>(svc.getProducts(category_id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ApiResponse> createProduct(@Valid @RequestBody Product in, BindingResult bindingResult){
		if(bindingResult.hasErrors())
			throw new ApiException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
		return new ResponseEntity<>(svc.createProduct(in),HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateProduct(@PathVariable("id") Integer id, @Valid @RequestBody Product in, BindingResult bindingResult){
		if(bindingResult.hasErrors())
			throw new ApiException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
		return new ResponseEntity<>(svc.updateProduct(in, id),HttpStatus.OK);
	}
	
	@PutMapping("/{gtin}/stock/{stock}")
	public ResponseEntity<ApiResponse> updateProductStock(@PathVariable("gtin") String gtin, @PathVariable("stock") Integer stock) {
		return new ResponseEntity<>(svc.updateProductStock(gtin,stock), HttpStatus.OK);
	}

	@PutMapping("/{gtin}/category")
	public ResponseEntity<ApiResponse> updateProductCategory(@PathVariable("gtin") String gtin, @Valid @RequestBody CategoryDTO dto,BindingResult bindingResult) {
		return new ResponseEntity<>(svc.updateProdCategory(gtin, dto), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("id") Integer id){
		return new ResponseEntity<>(svc.deleteProduct(id), HttpStatus.OK);
	}
}
