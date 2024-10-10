package com.stasienko.repository;

import com.stasienko.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
}