package com.stasienko.service;

import com.stasienko.model.Product;
import com.stasienko.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
;import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getProductsBasedOnPharmacyId(String pharmacyId) {
        return productRepository.findByMedicine_Pharmacy_Id(pharmacyId);
    }

    public List<Product> getProductsBasedOnMedicineId(UUID medicineId) {
        return productRepository.findByMedicine_Id(medicineId);
    }

    public Product getProductById(UUID productId) {
        return productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProductById(UUID productId) {
        productRepository.deleteById(productId);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}

