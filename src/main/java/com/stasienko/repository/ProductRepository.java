package com.stasienko.repository;

import com.stasienko.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface ProductRepository extends JpaRepository<Product, UUID> {
}