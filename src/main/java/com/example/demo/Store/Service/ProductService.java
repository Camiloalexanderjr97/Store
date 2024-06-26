package com.example.demo.Store.Service;

import com.example.demo.Store.Model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

	  public abstract ProductModel createProduct(ProductModel product);

	  public abstract Page<ProductModel> getProducts(Pageable pageable);

	  public abstract  ProductModel getProductById(int id);

      public abstract  ProductModel getProductByName(String name);
		
      public abstract boolean deleteProductById(int id, boolean status);
	   
	  public abstract ProductModel editarProductModel(ProductModel productModel);
	    
}
