package com.example.demo.Controller_jwt;

import com.example.demo.Store.Model.ProductModel;
import com.example.demo.Store.Service.ProductService;
import com.example.demo.User.dto.Mensaje;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin
@RestController
@RequestMapping("/auth/product")
public class ProductController {


	@Autowired
	private ProductService productService;


    private static final Log LOG = LogFactory.getLog(ProductController.class);

    @PostMapping("/new")
    public ResponseEntity<?> nuevo(@Valid @RequestBody ProductModel productModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new Mensaje("Misplaced or invalid fields",productModel));
        }
        try {
           productModel= productService.createProduct(productModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Mensaje("ProductModel Save",productModel));
        } catch (Exception e) {
            LOG.error("Error saving product", e);
            return ResponseEntity.badRequest().body(new Mensaje("Fail Saving ProductModel",null));
        }
    }


  

    @GetMapping("/products")
    public ResponseEntity<Page<?>> getAllProductModels(@RequestParam int page, @RequestParam int size) {

        try {
	        Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(productService.getProducts(pageable));
        } catch (HibernateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/products/find/{id}")
	public ResponseEntity<?> findByID(@PathVariable int id) {

        try {
            ProductModel product = productService.getProductById(id);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("Product not found with id: "+id,null));
            }
            return ResponseEntity.ok(product);
        } catch (HibernateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Mensaje("Internal Server Error",null));
        }
    }

    @DeleteMapping("/products/delete/{id}")
    public ResponseEntity<?> deleteProductModel(@PathVariable int id) {

        try {
            if (productService.getProductById(id)==null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("Product not found with id: "+id, null));
            }else{
				productService.deleteProductById(id);
				return ResponseEntity.ok(new Mensaje("Product #:"+id+"Deleted", null));
			}
        } catch (HibernateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Mensaje("Internal Server Error",null));
        }
    }

    @PostMapping("/products/edit/{id}")
    public ResponseEntity<?> editProductModel(@PathVariable int id, @Valid @RequestBody ProductModel productModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new Mensaje("Misplaced or invalid fields",null));
        }
        try {
            if (productService.getProductById(id)== null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("Product not found with id:"+id,null));
            }else{       
    
            productService.editarProductModel(productModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Mensaje("Product Updated",productService.getProductById(id)));
    
            }
        } catch (HibernateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Mensaje("Internal Server Error",null));
        }
	
    }
}