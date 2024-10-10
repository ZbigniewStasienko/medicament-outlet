package com.stasienko.repository;

import com.stasienko.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface PictureRepository extends JpaRepository<Picture, UUID> {
}