package com.stasienko.service;

import com.stasienko.model.*;
import com.stasienko.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class ReservationService {
    @Autowired
    UserService userService;

    @Autowired
    ReservedProductService reservedProductService;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ProductService productService;

    public int saveProducts(UUID userId, List<Product> products) {
        Map<UUID, Reservation> reservations = new HashMap<>();
        for (Product product : products) {
            Pharmacy pharmacy = product.getMedicine().getPharmacy();
            UUID pharmacyId = pharmacy.getId();
            Reservation reservation;
            if (reservations.containsKey(pharmacyId)) {
                reservation = reservations.get(pharmacyId);
            } else {
                User user = userService.findUserById(userId);
                if(user == null) {
                    user = new User();
                    user.setId(userId);
                    userService.saveUser(user);
                }
                reservation = new Reservation();
                reservation.setUser(user);
                reservation.setPharmacy(pharmacy);
                reservation.setRealizationDate(Date.from(LocalDate.now().plusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                reservation.setRealized(false);
                reservation.setStatus(0);
                reservationRepository.save(reservation);
                reservations.put(pharmacyId, reservation);
            }
            reservedProductService.saveReservedProduct(product, reservation);
        }
        return reservations.size();
    }

    public List<Reservation> getReservationByPharmacyId(UUID pharmacyId) {
        return reservationRepository.findByPharmacyId(pharmacyId);
    }

    public List<Reservation> getReservationByUserId(UUID userId) {
        return reservationRepository.findByUserId(userId);
    }

    public int numOfPendingReservations(UUID pharmacyId) {
        List<Reservation> reservations = getReservationByPharmacyId(pharmacyId);
        int counter = 0;
        for (Reservation reservation : reservations) {
            if(reservation.getStatus() == 0) {
                counter++;
            }
        }
        return counter;
    }

    public Reservation updateReservationStatus(UUID reservationId, Integer status) {
        Reservation foundReservation = reservationRepository.findById(reservationId).orElse(null);
        foundReservation.setStatus(status);
        return reservationRepository.save(foundReservation);
    }

    public UUID getPharmacyId(UUID reservationId) {
        return Objects.requireNonNull(reservationRepository.findById(reservationId).orElse(null)).getPharmacy().getId();
    }

    public void reservationCollected(UUID reservationId) {
        List<ReservedProduct> reservedProducts = reservedProductService.getProductsAssignedForReservation(reservationId);
        for(ReservedProduct reservedProduct : reservedProducts) {
            reservedProductService.deleteReservedProduct(reservedProduct.getId());
        }
        List<Product> products = productService.getProductsForReservation(reservedProducts);
        for (Product product : products) {
            productService.deleteProductById(product.getId());
        }
        reservationRepository.deleteById(reservationId);
    }

    public void deleteReservation(UUID reservationId) {
        List<ReservedProduct> reservedProducts = reservedProductService.getProductsAssignedForReservation(reservationId);
        List<Product> products = productService.getProductsForReservation(reservedProducts);
        for (Product product : products) {
            productService.updateProductAvailability(product.getId(), false);
        }
        for(ReservedProduct reservedProduct : reservedProducts) {
            reservedProductService.deleteReservedProduct(reservedProduct.getId());
        }
        reservationRepository.deleteById(reservationId);
    }
}
