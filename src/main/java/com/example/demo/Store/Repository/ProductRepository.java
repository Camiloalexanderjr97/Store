package com.example.demo.Store.Repository;

import com.example.demo.Store.Entity.Product;
import com.example.demo.Store.Model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

	@Repository
	public interface ProductRepository extends JpaRepository<Product, Integer> {
	    Page<Product> findAll(Pageable pageable);

		  @Modifying
	  @Query(value = "  Select * FROM product WHERE product.name = :name",
	    nativeQuery = true)
	  Optional<Product> findByIName(String name);


	  @Modifying
	  @Query(value = "  UPDATE product SET product.name = :model.getName() , product.description = :model.getDescription(), product.price =  :model.getPrice(),  product.stock= :model.getStock(),product.amount = :model.getAmount() WHERE product.id = :model.getId()",
	    nativeQuery = true)
	  int updateProductSetStatusForNameNative(ProductModel model);

  @Modifying
	  @Transactional
	  @Query(value = "  UPDATE product SET ticket.stock= :status WHERE product.id = :id",
	    nativeQuery = true)
	  int updateProductSetStatusForNameNativeStock(int id, boolean status);

	
}
