package com.stasienko.service;

import com.stasienko.model.Medicine;
import com.stasienko.model.Product;
import com.stasienko.model.ReservedProduct;
import com.stasienko.repository.ProductRepository;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
;import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<Product> getExpiredProducts() {
        return productRepository.findByExpirationDateBefore(LocalDate.now());
    }

    public List<Product> getProductsForReservation(List<ReservedProduct> reservedProducts) {
        List<Product> out = new ArrayList<>();
        for (ReservedProduct reservedProduct : reservedProducts) {
            out.add(getProductById(reservedProduct.getProduct().getId()));
        }
        return out;
    }

    public List<Product> getProducts(String searchTerm, String sortBy, Double latitude, Double longitude) {
        List<Product> products;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            products = searchProducts(searchTerm);
        } else {
            products = getAllProducts();
        }

        if ("price".equals(sortBy)) {
            products = sortByPrice(products);
        } else if ("location".equals(sortBy) && latitude != null && longitude != null) {
            products = getSortedByDistance(products, latitude, longitude);
        }

        return products;
    }

    public List<Product> getSortedByDistance(List<Product> products, double latitude, double longitude) {
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

    public List<Product> sortByPrice(List<Product> products) {
        products.sort(Comparator.comparingDouble(Product::getPrice));
        return products;
    }

    public List<Product> searchProducts(String searchTerm) {
        List<Product> products = getAllProducts();

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return products;
        }

        String[] searchWords = searchTerm.trim().split("\\s+");

        return products.stream()
                .filter(product -> {
                    Medicine medicine = product.getMedicine();
                    if (medicine == null) {
                        return false;
                    }
                    String name = medicine.getName() != null ? medicine.getName().toLowerCase() : "";
                    String description = medicine.getDescription() != null ? medicine.getDescription().toLowerCase() : "";

                    for (String word : searchWords) {
                        String lowerCaseWord = word.toLowerCase();
                        if (name.contains(lowerCaseWord) || description.contains(lowerCaseWord)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    public List<Product> filterProductsBasedOnPharmacyId(List<Product> products, UUID pharmacyId) {
        List<Product> out = new ArrayList<>();
        for(Product product : products) {
            if(product.getMedicine().getPharmacy().getId().equals(pharmacyId)) {
                out.add(product);
            }
        }
        return out;
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

