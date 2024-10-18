package com.stasienko.repository;

import com.stasienko.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
public interface MedicineRepository extends JpaRepository<Medicine, UUID> {
    List<Medicine> findByPharmacyId(String pharmacyId);
}