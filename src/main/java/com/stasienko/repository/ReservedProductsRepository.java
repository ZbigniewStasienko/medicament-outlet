package com.stasienko.repository;

import com.stasienko.model.ReservedProducts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface ReservedProductsRepository extends JpaRepository<ReservedProducts, UUID> {
}