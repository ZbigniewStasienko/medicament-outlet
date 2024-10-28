package com.stasienko.repository;

import com.stasienko.model.Product;
import com.stasienko.model.ReservedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
public interface ReservedProductRepository extends JpaRepository<ReservedProduct, UUID> {
    List<ReservedProduct> findByReservationId(UUID reservationId);
    List<ReservedProduct> findByProduct(Product product);
}