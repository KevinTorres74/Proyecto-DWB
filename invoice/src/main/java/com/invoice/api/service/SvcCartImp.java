package com.invoice.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.invoice.api.dto.DtoProduct;
import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoCustomer;
import com.invoice.api.entity.Cart;
import com.invoice.api.repository.RepoCart;
import com.invoice.configuration.client.CustomerClient;
import com.invoice.configuration.client.ProductClient;
import com.invoice.exception.ApiException;

@Service
public class SvcCartImp implements SvcCart {

	@Autowired
	RepoCart repo;
	
	@Autowired
	CustomerClient customerCl;
	
	@Autowired
	ProductClient productCl;
	
	@Override
	public List<Cart> getCart(String rfc) {
		return repo.findByRfcAndStatus(rfc,1);
	}

	@Override
	public ApiResponse addToCart(Cart cart) {
		if(!validateCustomer(cart.getRfc()))
			throw new ApiException(HttpStatus.BAD_REQUEST, "customer does not exist");

		/*
		 * Requerimiento 3
		 * Validar que el GTIN exista. Si existe, asignar el stock del producto a la variable product_stock
		 */
		Integer product_stock = getCuantoProducto(cart.getGtin()); // cambiar el valor de cero por el stock del producto recuperado de la API Product

		if(cart.getQuantity() > product_stock) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "invalid quantity");
		}

		/*
		 * Requerimiento 4
		 * Validar si el producto ya había sido agregado al carrito para solo actualizar su cantidad
		 */

		 Cart car = (Cart) repo.findCartbygtinAndRfc(cart.getGtin(), cart.getRfc());
		 if(car!=null){
			 if(car.getQuantity() + cart.getQuantity() > product_stock) {
				 throw new ApiException(HttpStatus.BAD_REQUEST, "invalid quantity");
			 }else{
				 product_stock=cart.getQuantity() + car.getQuantity();
				 car.setQuantity(product_stock);
				 repo.updateCartQuantity(car.getQuantity(),car.getGtin(),car.getRfc());
				 return new ApiResponse("quantity updated");
			 }
		 }else{
			 cart.setStatus(1);
			 repo.save(cart);
		 }
		return new ApiResponse("item added");
	}
	
	/**
	* Metodo auxiliar para saber cuanto hay de un producto
	*/
	private Integer getCuantoProducto(String gtin){
		if(validateProduct(gtin)){
			DtoProduct dtoProduct = productCl.getProduct(gtin).getBody();
			return dtoProduct.getStock();
		}else
			throw new ApiException(HttpStatus.BAD_REQUEST, "product doesn't exist");
	}

	@Override
	public ApiResponse removeFromCart(Integer cart_id) {
		if (repo.removeFromCart(cart_id) > 0)
			return new ApiResponse("item removed");
		else
			throw new ApiException(HttpStatus.BAD_REQUEST, "item cannot be removed");
	}

	@Override
	public ApiResponse clearCart(String rfc) {
		if (repo.clearCart(rfc) > 0)
			return new ApiResponse("cart removed");
		else
			throw new ApiException(HttpStatus.BAD_REQUEST, "cart cannot be removed");
	}
	
	private boolean validateCustomer(String rfc) {
		try {
			ResponseEntity<DtoCustomer> response = customerCl.getCustomer(rfc);
			if(response.getStatusCode() == HttpStatus.OK)
				return true;
			else
				return false;
		}catch(Exception e) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "unable to retrieve customer information");
		}
	}

	private boolean validateProduct(String gtin){
		try{
			ResponseEntity<DtoProduct> response = productCl.getProduct(gtin);
			if(response.getStatusCode() == HttpStatus.OK)
				return true;
			else
				return false;
		}catch(Exception e){
			throw new ApiException(HttpStatus.BAD_REQUEST, "unable to retrieve product information");
		}
	}

}
