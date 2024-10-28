package com.stasienko.repository;

import com.stasienko.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByMedicine_Pharmacy_Id(UUID pharmacyId);
    List<Product> findByMedicine_Id(UUID medicineId);
    @Query(value = "SELECT * FROM Product p " +
            "WHERE p.is_reserved = true " +
            "AND NOT EXISTS (SELECT 1 FROM ReservedProduct rp WHERE rp.product_id = p.id)", nativeQuery = true)
    List<Product> findReservedProductsNotInReservedProductTable();
    List<Product> findByExpirationDateBefore(LocalDate currentDate);
}
