package com.invoice.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invoice.api.entity.Invoice;

@Repository
public interface RepoInvoice extends JpaRepository<Invoice, Integer>{

	List<Invoice> findByRfcAndStatus(String rfc, Integer status);
	/*
	 * Query para actuañizar una factura (bueno el invoice xd) 
	*/
	@Modifying
	@Transactional
	@Query(value = "UPDATE invoice SET invoice_id = :invoice_id, rfc = :rfc, subtotal = :subtotal, taxes = :taxes, total = :total, created_at = :created_at, status = 1 WHERE status = 1 AND invoice_id = :invoice_id", nativeQuery = true)
	void updateInvoice(@Param("invoice_id") Integer invoice_id, @Param("rfc") String rfc, @Param("subtotal") double subtotal, @Param("taxes") double taxes, @Param("total") double total, @Param("created_at")LocalDateTime created_at);

}
