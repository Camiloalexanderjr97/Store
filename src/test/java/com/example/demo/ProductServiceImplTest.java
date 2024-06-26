package com.example.demo;

import com.example.demo.Store.Converter.ProductConverter;
import com.example.demo.Store.Entity.Product;
import com.example.demo.Store.Model.ProductModel;
import com.example.demo.Store.Repository.ProductRepository;
import com.example.demo.Store.Service.ProductService;
import com.example.demo.Store.ServiceImpl.ProductServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductConverter productConverter;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductModel productModel;
    private Product product;
    private Pageable pageable;
    private Page<Product> productPage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        productModel = new ProductModel();
        product = new Product();
        pageable = PageRequest.of(0, 10);
        productPage = new PageImpl<>(List.of(product));

        product.setId(1);
        product.setName("Test Product");
        product.setStock(true);
        product.setAmount(100);
        product.setPrice(10.0);
    }

    @Test
    void testCreateProduct() {
        when(productConverter.modelToEntidy(productModel)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productConverter.entityToModel(product)).thenReturn(productModel);

        ProductModel result = productService.createProduct(productModel);

        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetProducts() {
        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productConverter.entityToModel(product)).thenReturn(productModel);

        Page<ProductModel> result = productService.getProducts(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productConverter.entityToModel(product)).thenReturn(productModel);

        ProductModel result = productService.getProductById(1);

        assertNotNull(result);
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void testGetProductEntityById() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductEntityById(1);

        assertTrue(result.isPresent());
        assertEquals(product, result.get());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void testGetProductByName() {
        when(productRepository.findByIName("Test Product")).thenReturn(Optional.of(product));
        when(productConverter.entityToModel(product)).thenReturn(productModel);

        ProductModel result = productService.getProductByName("Test Product");

        assertNotNull(result);
        verify(productRepository, times(1)).findByIName("Test Product");
    }

    @Test
    void testDeleteProductById() {
        when(productRepository.updateProductSetStatusForNameNativeStock(anyInt(), anyBoolean()))
                .thenReturn(1);

        boolean result = productService.deleteProductById(1, false);

        assertFalse(result);
        verify(productRepository, times(1))
                .updateProductSetStatusForNameNativeStock(anyInt(), anyBoolean());
    }

    @Test
    void testEditarProductModel() {
        when(productConverter.modelToEntidy(productModel)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productConverter.entityToModel(product)).thenReturn(productModel);

        ProductModel result = productService.editarProductModel(productModel);

        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
    }
}
