package com.example.demo.Store.ServiceImpl;

import com.example.demo.Store.Converter.ProductConverter;
import com.example.demo.Store.Entity.Product;
import com.example.demo.Store.Model.ProductModel;
import com.example.demo.Store.Repository.ProductRepository;
import com.example.demo.Store.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductConverter productConverter;

	@Override
	public ProductModel createProduct(ProductModel product) {

		return productConverter.entityToModel(productRepository.save(productConverter.modelToEntidy(product)));
	}

	@Override
	public Page<ProductModel> getProducts(Pageable pageable) {
		Page<Product> products = productRepository.findAll(pageable);
		return products.map(productConverter::entityToModel);
	}

	@Override
	public ProductModel getProductById(int id) {
		return productConverter.entityToModel(productRepository.findById(id).get());
	}
	public Optional<Product> getProductEntityById(int id) {
		return Optional.ofNullable(productRepository.findById(id).get());
	}

	@Override
	public ProductModel getProductByName(String name) {
		return productConverter.entityToModel(productRepository.findByIName(name).get());
	}

	@Override
	public boolean deleteProductById(int id, boolean status) {
		boolean resultado = false;
		try {
			if (productRepository.updateProductSetStatusForNameNativeStock(id, status) != 1)
				resultado = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return resultado;
	}

	@Override
	public ProductModel editarProductModel(ProductModel productModel) {
	return  productConverter.entityToModel(productRepository.save(productConverter.modelToEntidy(productModel)));
	}

}
