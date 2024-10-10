package com.stasienko.repository;

import com.stasienko.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface UserRepository extends JpaRepository<User, UUID> {
}