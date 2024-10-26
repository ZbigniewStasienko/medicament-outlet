package com.stasienko.service;

import com.stasienko.model.Product;
import com.stasienko.model.Reservation;
import com.stasienko.model.ReservedProduct;
import com.stasienko.repository.ReservedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReservedProductService {
    @Autowired
    ReservedProductRepository reservedProductRepository;

    public void saveReservedProduct(Product product, Reservation reservation) {
        ReservedProduct reservedProduct = new ReservedProduct();
        reservedProduct.setReservation(reservation);
        reservedProduct.setProduct(product);
        reservedProductRepository.save(reservedProduct);
    }

    public List<ReservedProduct> getProductsAssignedForReservation(UUID reservationId) {
        return reservedProductRepository.findByReservationId(reservationId);
    }

    public void deleteReservedProduct(UUID reservedProductId) {
        reservedProductRepository.deleteById(reservedProductId);
    }
}
