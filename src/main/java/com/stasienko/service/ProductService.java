package com.stasienko.service;

import com.stasienko.model.Product;
import com.stasienko.model.ReservedProduct;
import com.stasienko.repository.ProductRepository;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
;import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getProductsBasedOnPharmacyId(UUID pharmacyId) {
        return productRepository.findByMedicine_Pharmacy_Id(pharmacyId);
    }

    public List<Product> getProductsBasedOnMedicineId(UUID medicineId) {
        return productRepository.findByMedicine_Id(medicineId);
    }

    public Product getProductById(UUID productId) {
        return productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProductById(UUID productId) {
        productRepository.deleteById(productId);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProductAvailability(UUID productId, boolean isReserved) {
        Product product = getProductById(productId);
        product.setIsReserved(isReserved);
        saveProduct(product);
        return product;
    }

    public List<Product> getProductsFromCart() {
        return productRepository.findReservedProductsNotInReservedProductTable();
    }

    public List<Product> getProductsForReservation(List<ReservedProduct> reservedProducts) {
        List<Product> out = new ArrayList<>();
        for (ReservedProduct reservedProduct : reservedProducts) {
            out.add(getProductById(reservedProduct.getProduct().getId()));
        }
        return out;
    }

    public List<Product> getSortedByDistance(double latitude, double longitude) {
        List<Product> products = getAllProducts();

        products.sort((p1, p2) -> {
            double distanceToP1 = calculateDistance(
                    latitude,
                    longitude,
                    Double.parseDouble(p1.getMedicine().getPharmacy().getLatitude()),
                    Double.parseDouble(p1.getMedicine().getPharmacy().getLongitude())
            );
            double distanceToP2 = calculateDistance(
                    latitude,
                    longitude,
                    Double.parseDouble(p2.getMedicine().getPharmacy().getLatitude()),
                    Double.parseDouble(p2.getMedicine().getPharmacy().getLongitude())
            );
            return Double.compare(distanceToP1, distanceToP2);
        });

        return products;
    }

    private double calculateDistance(
            double lat1, double lon1,
            double lat2, double lon2
    ) {
        final int EARTH_RADIUS_KM = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    @PreDestroy
    public void onShutdown() {
        List<Product> toBeRestored = getProductsFromCart();
        for (Product product : toBeRestored) {
            updateProductAvailability(product.getId(), false);
        }
    }
}

