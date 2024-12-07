package com.stasienko.service;

import com.stasienko.model.Medicine;
import com.stasienko.model.Pharmacy;
import com.stasienko.model.Product;
import com.stasienko.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        Pharmacy pharmacyA = new Pharmacy();
        pharmacyA.setId(UUID.randomUUID());
        pharmacyA.setLatitude("52.5200");
        pharmacyA.setLongitude("13.4050");

        Pharmacy pharmacyB = new Pharmacy();
        pharmacyB.setId(UUID.randomUUID());
        pharmacyB.setLatitude("48.8566");
        pharmacyB.setLongitude("2.3522");

        Medicine medicineA = new Medicine();
        medicineA.setId(UUID.randomUUID());
        medicineA.setName("Aspirin");
        medicineA.setDescription("Pain relief medicine");
        medicineA.setPharmacy(pharmacyA);

        Medicine medicineB = new Medicine();
        medicineB.setId(UUID.randomUUID());
        medicineB.setName("Paracetamol");
        medicineB.setDescription("Fever reducer medicine");
        medicineB.setPharmacy(pharmacyB);

        Medicine medicineC = new Medicine();
        medicineC.setId(UUID.randomUUID());
        medicineC.setName("Ibuprofen");
        medicineC.setDescription("Anti-inflammatory");
        medicineC.setPharmacy(pharmacyA);

        product1 = new Product();
        product1.setId(UUID.randomUUID());
        product1.setMedicine(medicineA);
        product1.setPrice(5.0);

        product2 = new Product();
        product2.setId(UUID.randomUUID());
        product2.setMedicine(medicineB);
        product2.setPrice(10.0);

        product3 = new Product();
        product3.setId(UUID.randomUUID());
        product3.setMedicine(medicineC);
        product3.setPrice(7.0);
    }

    @Test
    void testGetProducts_NoSearchTermNoSort_ReturnsAllProducts() {
        List<Product> allProducts = Arrays.asList(product1, product2, product3);
        when(productRepository.findAll()).thenReturn(allProducts);

        List<Product> result = productService.getProducts(null, null, null, null);

        assertEquals(3, result.size());
        assertTrue(result.containsAll(allProducts));
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProducts_WithSearchTerm_FiltersProducts() {
        List<Product> allProducts = Arrays.asList(product1, product2, product3);
        when(productRepository.findAll()).thenReturn(allProducts);

        String searchTerm = "fever";

        List<Product> result = productService.getProducts(searchTerm, null, null, null);

        assertEquals(1, result.size());
        assertTrue(result.contains(product2));
    }

    @Test
    void testGetProducts_SortByPrice_SortsAscending() {
        List<Product> allProducts = Arrays.asList(product1, product2, product3);
        when(productRepository.findAll()).thenReturn(allProducts);

        List<Product> result = productService.getProducts(null, "price", null, null);

        assertEquals(3, result.size());
        assertEquals(product1, result.get(0));
        assertEquals(product3, result.get(1));
        assertEquals(product2, result.get(2));
    }

    @Test
    void testGetProducts_SortByLocation_SortsByNearestPharmacy() {
        double userLat = 50.1109;
        double userLon = 8.6821;

        List<Product> allProducts = Arrays.asList(product1, product2, product3);
        when(productRepository.findAll()).thenReturn(allProducts);

        List<Product> result = productService.getProducts(null, "location", userLat, userLon);

        assertEquals(3, result.size());
        assertTrue((result.get(0).equals(product1) || result.get(0).equals(product3)) &&
                (result.get(1).equals(product1) || result.get(1).equals(product3)) &&
                result.get(2).equals(product2));
    }

    @Test
    void testGetProducts_WithSearchTermAndSortByPrice() {
        List<Product> allProducts = Arrays.asList(product1, product2, product3);
        when(productRepository.findAll()).thenReturn(allProducts);

        String searchTerm = "Anti-inflammatory";

        List<Product> result = productService.getProducts(searchTerm, "price", null, null);

        assertEquals(1, result.size());
        assertEquals(product3, result.get(0));
    }
}
