package com.example.demo;

import com.example.demo.Controller_jwt.ProductController;
import com.example.demo.Store.Model.ProductModel;
import com.example.demo.Store.Service.ProductService;
import com.example.demo.User.dto.Mensaje;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private ProductController productController;

    private ProductModel productModel;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        productModel = new ProductModel();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testNuevo_ProductIsValid() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(productService.createProduct(productModel)).thenReturn(productModel);

        ResponseEntity<?> response = productController.nuevo(productModel, bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("ProductModel Save", ((Mensaje) response.getBody()).getMensaje());
        verify(productService, times(1)).createProduct(productModel);
    }

    @Test
    void testNuevo_ProductInvalidFields() {
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = productController.nuevo(productModel, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Misplaced or invalid fields", ((Mensaje) response.getBody()).getMensaje());
    }

    @Test
    void testNuevo_Exception() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(productService.createProduct(productModel)).thenThrow(RuntimeException.class);

        ResponseEntity<?> response = productController.nuevo(productModel, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Fail Saving ProductModel", ((Mensaje) response.getBody()).getMensaje());
    }

    @Test
    void testGetAllProductModels() {
        Page<?> page = new PageImpl<>(Collections.emptyList());

        ResponseEntity<Page<?>> response = productController.getAllProductModels(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

  

    @Test
    void testFindByID_ProductFound() {
        when(productService.getProductById(1)).thenReturn(productModel);

        ResponseEntity<?> response = productController.findByID(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productModel, response.getBody());
    }

    @Test
    void testFindByID_ProductNotFound() {
        when(productService.getProductById(1)).thenReturn(null);

        ResponseEntity<?> response = productController.findByID(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found with id: 1", ((Mensaje) response.getBody()).getMensaje());
    }

 
    @Test
    void testDeleteProductLogicModel_ProductFound() {
        when(productService.getProductById(1)).thenReturn(productModel);

        ResponseEntity<?> response = productController.deleteProductLogicModel(1, "true");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product #:1Deleted(Desactivated)", ((Mensaje) response.getBody()).getMensaje());
        verify(productService, times(1)).deleteProductById(1, true);
    }

    @Test
    void testDeleteProductLogicModel_ProductNotFound() {
        when(productService.getProductById(1)).thenReturn(null);

        ResponseEntity<?> response = productController.deleteProductLogicModel(1, "true");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found with id: 1", ((Mensaje) response.getBody()).getMensaje());
    }


    @Test
    void testEditProductModel_ProductIsValid() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(productService.getProductById(1)).thenReturn(productModel);
        when(productService.editarProductModel(productModel)).thenReturn(productModel);

        ResponseEntity<?> response = productController.editProductModel(1, productModel, bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Product Updated", ((Mensaje) response.getBody()).getMensaje());
        verify(productService, times(1)).editarProductModel(productModel);
    }

    @Test
    void testEditProductModel_ProductInvalidFields() {
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = productController.editProductModel(1, productModel, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Misplaced or invalid fields", ((Mensaje) response.getBody()).getMensaje());
    }

    @Test
    void testEditProductModel_ProductNotFound() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(productService.getProductById(1)).thenReturn(null);

        ResponseEntity<?> response = productController.editProductModel(1, productModel, bindingResult);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found with id:1", ((Mensaje) response.getBody()).getMensaje());
    }

  
}
