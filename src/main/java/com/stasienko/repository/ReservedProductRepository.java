package com.stasienko.repository;

import com.stasienko.model.ReservedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface ReservedProductRepository extends JpaRepository<ReservedProduct, UUID> {
}