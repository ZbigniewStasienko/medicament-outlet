package com.stasienko.repository;

import com.stasienko.model.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface PharmacyRepository extends JpaRepository<Pharmacy, String> {
}