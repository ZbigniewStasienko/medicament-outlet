package com.stasienko.repository;

import com.stasienko.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface CompanyRepository extends JpaRepository<Company, UUID> {
}