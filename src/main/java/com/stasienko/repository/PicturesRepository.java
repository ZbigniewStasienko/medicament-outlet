package com.stasienko.repository;

import com.stasienko.model.Pictures;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface PicturesRepository extends JpaRepository<Pictures, UUID> {
}