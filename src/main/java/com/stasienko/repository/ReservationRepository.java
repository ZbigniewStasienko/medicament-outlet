package com.stasienko.repository;

import com.stasienko.model.Medicine;
import com.stasienko.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByPharmacyId(UUID pharmacyId);
}