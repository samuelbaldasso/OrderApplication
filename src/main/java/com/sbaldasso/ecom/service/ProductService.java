package com.sbaldasso.ecom.service;

import com.sbaldasso.ecom.dto.ProductRequest;
import com.sbaldasso.ecom.dto.ProductResponse;
import com.sbaldasso.ecom.model.Product;
import com.sbaldasso.ecom.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(BigDecimal.valueOf(productRequest.getPrice()));
        product.setQuantity(productRequest.getQuantity());
        product.setImageUrl(productRequest.getImageUrl());
        product.setCategory(productRequest.getCategory());

        Product savedProduct = productRepository.save(product);
        return toDTO(savedProduct);
    }

    public Optional<ProductResponse> getProductById(Long id){
        Optional<Product> productResponse = productRepository.findById(id);
        return productResponse.map(this::toDTO);
    }

    public List<ProductResponse> getAllProducts(){
            return productRepository.findByActiveTrue().stream().map(this::toDTO).toList();
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest){
        Product existingProduct = productRepository.findById(id).orElse(null);
        if(existingProduct != null){
            existingProduct.setName(productRequest.getName());
            existingProduct.setDescription(productRequest.getDescription());
            existingProduct.setPrice(BigDecimal.valueOf(productRequest.getPrice()));
            existingProduct.setQuantity(productRequest.getQuantity());
            existingProduct.setImageUrl(productRequest.getImageUrl());
            existingProduct.setCategory(productRequest.getCategory());
            productRepository.save(existingProduct);
            return toDTO(existingProduct);
        }else{
            return null;
        }
    }

    public void deleteProduct(Long id){
        ProductResponse product = getProductById(id).orElse(null);
        assert product != null;
        product.setActive(Boolean.FALSE);
    }

    public List<ProductResponse> searchProducts(String keyword){
        return productRepository.searchProducts(keyword).stream().map(this::toDTO).toList();
    }

    private ProductResponse toDTO(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice().doubleValue());
        productResponse.setQuantity(product.getQuantity());
        productResponse.setImageUrl(product.getImageUrl());
        productResponse.setCategory(product.getCategory());
        return productResponse;
    }

}
