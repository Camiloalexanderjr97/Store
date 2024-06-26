package com.example.demo.Store.Converter;

import com.example.demo.Store.Entity.Product;
import com.example.demo.Store.Model.ProductModel;
import org.springframework.stereotype.Service;

@Service
public class ProductConverter {

	
	public Product modelToEntidy(ProductModel model) {
		Product product = new Product();
		product.setId(model.getId());
		product.setName(model.getName());
		product.setDescription(model.getDescription());
		product.setPrice(model.getPrice());
		product.setStock(model.getStock());
		product.setAmount(model.getAmount());
		return product;
	}

	public ProductModel entityToModel(Product product){
		ProductModel model = new ProductModel();
		model.setId(product.getId());
		model.setDescription(product.getDescription());
		model.setName(product.getName());
		model.setPrice(product.getPrice());		
		model.setStock(product.getStock());
		model.setAmount(product.getAmount());
		return model;
	}
}
