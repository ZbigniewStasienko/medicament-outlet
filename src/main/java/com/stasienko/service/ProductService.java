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

    public List<Product> getProductsBasedOnPharmacyId(UUID pharmacyId) {
        return productRepository.findByMedicine_Pharmacy_Id(pharmacyId);
    }

    public int getSizeOfAllProductsDataset() {
        return productRepository.findAll().size();
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}

