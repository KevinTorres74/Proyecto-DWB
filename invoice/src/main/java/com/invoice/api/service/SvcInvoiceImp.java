package com.invoice.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.invoice.api.entity.Cart;
import com.invoice.configuration.client.ProductClient;
import com.invoice.exception.ApiException;
import com.invoice.api.repository.RepoCart;
import com.invoice.api.dto.ApiResponse;
import com.invoice.api.entity.Invoice;
import com.invoice.api.entity.Item;
import com.invoice.api.repository.RepoInvoice;
import com.invoice.api.repository.RepoItem;

import java.time.LocalDateTime;

@Service
public class SvcInvoiceImp implements SvcInvoice {

	@Autowired
	RepoInvoice repo;
	
	@Autowired
	RepoItem repoItem;

	@Autowired
	RepoCart repoCart;

	@Autowired
	ProductClient productCl;

	@Override
	public List<Invoice> getInvoices(String rfc) {
		return repo.findByRfcAndStatus(rfc, 1);
	}

	@Override
	public List<Item> getInvoiceItems(Integer invoice_id) {
		return repoItem.getInvoiceItems(invoice_id);
	}

	@Override
	public ApiResponse generateInvoice(String rfc) {
		/*
		 * Requerimiento 5
		 * Implementar el método para generar una factura 
		 */
		List<Cart> carros = repoCart.findByRfcAndStatus(rfc,1);
		if (carros.isEmpty()){
			throw new ApiException(HttpStatus.NOT_FOUND,"cart has nos items");
		}
		Invoice invoice = new Invoice();
		invoice.setRfc(rfc);
		invoice.setTaxes(0.0);
		invoice.setTotal(-1.0);
		invoice.setSubtotal(0.0);
		invoice.setCreated_at(LocalDateTime.now());
		invoice.setStatus(1);
		repo.save(invoice);

		List<Invoice> facturas = repo.findByRfcAndStatus(rfc,1);
		Invoice factura = null;
		for (Invoice i: facturas) {
			if (i.getTotal() == -1){
				factura = i;
				System.out.println(factura.getInvoice_id());
				break;
			}
		}

		List<Item> articulos = new ArrayList<Item>();
		for (Cart c: carros) {
			articulos.add(crearObjeto(rfc,c,factura));
			productCl.updateProductStock(c.getGtin(),c.getQuantity());

			repoCart.clearCart(c.getRfc());
		}
		
		double impuestos = 0.0;
		double total = 0.0;
		double subtotal = 0.0;
		for (Item it: articulos) {
			total += it.getTotal();
			impuestos += it.getTaxes();
			subtotal += it.getSubtotal();
		}
		factura.setSubtotal(subtotal);
		factura.setTaxes(impuestos);
		factura.setTotal(total);
		factura.setCreated_at(LocalDateTime.now());
		factura.setStatus(1);
		repo.updateInvoice(factura.getInvoice_id(),factura.getRfc(),factura.getSubtotal(),factura.getTaxes(),factura.getTotal(),factura.getCreated_at());
		return new ApiResponse("invoice generated");
	}

	private Item crearObjeto(String rfc,Cart carro, Invoice factura){
		Item articulo = new Item();
		articulo.setId_invoice(factura.getInvoice_id());
		articulo.setGtin(carro.getGtin());
		articulo.setQuantity(carro.getQuantity());
		double precio = productCl.getProduct(carro.getGtin()).getBody().getPrice();
		articulo.setUnit_price(precio);
		double total = carro.getQuantity() * precio;
		articulo.setTaxes(total * 0.16);
		articulo.setTotal(total);
		articulo.setSubtotal(total - (articulo.getTaxes()));
		articulo.setStatus(1);
		repoItem.save(articulo);
		return articulo;
	}

}
