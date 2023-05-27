package com.product.api.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;

import com.product.api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.product.api.entity.Product;

import java.util.List;

@Repository
public interface RepoProduct extends JpaRepository<Product, Integer> {

	@Query(value = "SELECT * FROM product WHERE status = 1 AND gtin = :gtin", nativeQuery = true)
	Product findByStatusAndGtin(@Param("gtin") String gtin);

	@Query(value = "SELECT p.* FROM product p INNER JOIN " +
			       "category c ON p.category_id=c.category_id WHERE p.category_id = :category_id " +
			       "AND p.status = 1", nativeQuery = true)
	List<Product> findByProdCat(@Param("category_id") int category_id);

	@Query(value = "SELECT * FROM product WHERE gtin = :gtin", nativeQuery = true)
	Product findByGtin(@Param("gtin") String gtin);

	@Query(value = "SELECT * FROM product WHERE product = :product", nativeQuery = true)
	Product findByProduct(@Param("product") String product);

	@Modifying
	@Transactional
	@Query(value = "UPDATE product SET status = 1 WHERE product_id = :product_id", nativeQuery = true)
	Integer activateProduct(@Param("product_id") Integer product_id);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO product "
	                 + "(gtin,product,description,price,stock,category_id,status) "
			         + "VALUES (:gtin,:product,:description,:price,:stock,:category_id,1)", nativeQuery = true)
	void createProduct(
			@Param("gtin") String gtin,
			@Param("product") String product,
			@Param("description") String description,
			@Param("price") Double price,
			@Param("stock") Integer stock,
			@Param("category_id") Integer category_id
	);
	
	@Modifying
	@Transactional
	@Query(value ="UPDATE product "
					+ "SET gtin = :gtin, "
						+ "product = :product, "
						+ "description = :description, "
						+ "price = :price, "
						+ "stock = :stock, "
						+ "status = 1, "
						+ "category_id = :category_id "
					+ "WHERE product_id = :product_id", nativeQuery = true)
	Integer updateProduct(
			@Param("product_id") Integer product_id,
			@Param("gtin") String gtin, 
			@Param("product") String product, 
			@Param("description") String description, 
			@Param("price") Double price, 
			@Param("stock") Integer stock,
			@Param("category_id") Integer category_id
		);
	
	@Modifying
	@Transactional
	@Query(value ="UPDATE product SET status = 0 WHERE product_id = :product_id AND status = 1", nativeQuery = true)
	Integer deleteProduct(@Param("product_id") Integer product_id);
	
	@Modifying
	@Transactional
	@Query(value ="UPDATE product SET stock = :stock WHERE gtin = :gtin AND status = 1", nativeQuery = true)
	Integer updateProductStock(@Param("gtin") String gtin, @Param("stock") Integer stock);

	@Modifying
	@Transactional
	@Query(value ="UPDATE product SET category_id = :category_id WHERE gtin = :gtin AND status = 1", nativeQuery = true)
	Integer updateProdCat(@Param("gtin") String gtin, @Param("category_id") Integer category_id);
}
