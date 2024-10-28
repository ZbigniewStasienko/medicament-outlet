package com.stasienko.service;

import com.stasienko.model.Product;
import com.stasienko.model.ReservedProduct;
import com.stasienko.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCleanupService {
    @Autowired
    ReservedProductService reservedProductService;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ProductService productService;

    @Scheduled(fixedRate = 86400000)
    @Transactional
    public void deleteExpiredProducts() {
        List<Product> expiredProducts = productService.getExpiredProducts();
        for(Product product : expiredProducts) {
            System.out.println("Expired " + product.getMedicine().getName() + " " + product.getExpirationDate());
            List<ReservedProduct> reservedProducts = reservedProductService.getReservedProductsByProduct(product);
            for(ReservedProduct reservedProduct : reservedProducts) {
                reservedProductService.deleteReservedProduct(reservedProduct.getId());
            }
            productService.deleteProductById(product.getId());
        }
    }
}
