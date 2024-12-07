package com.stasienko.service;

import com.stasienko.model.Medicine;
import com.stasienko.model.Pharmacy;
import com.stasienko.model.Product;
import com.stasienko.repository.PharmacyRepository;
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
class PharmacyServiceTest {

    @Mock
    private PharmacyRepository pharmacyRepository;

    @Mock
    private MedicineService medicineService;

    @InjectMocks
    private PharmacyService pharmacyService;

    private UUID pharmacyId;
    private Pharmacy pharmacy;

    @BeforeEach
    void setUp() {
        pharmacyId = UUID.randomUUID();
        pharmacy = new Pharmacy();
        pharmacy.setId(pharmacyId);
        pharmacy.setName("Test Pharmacy");
        pharmacy.setEmail("test@example.com");
        pharmacy.setAddress("123 Main St");
        pharmacy.setPhoneNumber("555-1234");
        pharmacy.setLatitude("52.5200");
        pharmacy.setLongitude("13.4050");
        pharmacy.setOpeningHours("8-18");
    }

    @Test
    void testGetAllPharmacies() {
        List<Pharmacy> pharmacies = Collections.singletonList(pharmacy);
        when(pharmacyRepository.findAll()).thenReturn(pharmacies);

        List<Pharmacy> result = pharmacyService.getAllPharmacies();

        assertEquals(pharmacies, result);
        verify(pharmacyRepository, times(1)).findAll();
    }

    @Test
    void testGetPharmacyById() {
        when(pharmacyRepository.findById(pharmacyId)).thenReturn(Optional.of(pharmacy));

        Pharmacy result = pharmacyService.getPharmacyById(pharmacyId);

        assertEquals(pharmacy, result);
        verify(pharmacyRepository, times(1)).findById(pharmacyId);
    }

    @Test
    void testGetPharmacyById_NotFound() {
        when(pharmacyRepository.findById(pharmacyId)).thenReturn(Optional.empty());

        Pharmacy result = pharmacyService.getPharmacyById(pharmacyId);

        assertNull(result);
        verify(pharmacyRepository, times(1)).findById(pharmacyId);
    }

    @Test
    void testAddPharmacy() {
        when(pharmacyRepository.save(pharmacy)).thenReturn(pharmacy);

        Pharmacy result = pharmacyService.addPharmacy(pharmacy);

        assertEquals(pharmacy, result);
        verify(pharmacyRepository, times(1)).save(pharmacy);
    }

    @Test
    void testUpdatePharmacy_Existing() {
        Pharmacy updatedPharmacy = new Pharmacy();
        updatedPharmacy.setName("Updated Name");
        updatedPharmacy.setEmail("updated@example.com");
        updatedPharmacy.setAddress("321 Updated St");
        updatedPharmacy.setPhoneNumber("555-5678");
        updatedPharmacy.setLatitude("48.8566");
        updatedPharmacy.setLongitude("2.3522");
        updatedPharmacy.setOpeningHours("9-19");

        when(pharmacyRepository.findById(pharmacyId)).thenReturn(Optional.of(pharmacy));
        when(pharmacyRepository.save(any(Pharmacy.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pharmacy result = pharmacyService.updatePharmacy(pharmacyId, updatedPharmacy);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("321 Updated St", result.getAddress());
        assertEquals("555-5678", result.getPhoneNumber());
        assertEquals("48.8566", result.getLatitude());
        assertEquals("2.3522", result.getLongitude());
        assertEquals("9-19", result.getOpeningHours());

        verify(pharmacyRepository, times(1)).findById(pharmacyId);
        verify(pharmacyRepository, times(1)).save(any(Pharmacy.class));
    }

    @Test
    void testUpdatePharmacy_NotExisting() {
        Pharmacy newPharmacy = new Pharmacy();
        newPharmacy.setName("New Pharmacy");

        when(pharmacyRepository.findById(pharmacyId)).thenReturn(Optional.empty());
        when(pharmacyRepository.save(any(Pharmacy.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pharmacy result = pharmacyService.updatePharmacy(pharmacyId, newPharmacy);

        assertEquals(pharmacyId, result.getId());
        assertEquals("New Pharmacy", result.getName());
        verify(pharmacyRepository, times(1)).findById(pharmacyId);
        verify(pharmacyRepository, times(1)).save(newPharmacy);
    }

    @Test
    void testDeletePharmacy() {
        Medicine medicine1 = new Medicine();
        medicine1.setId(UUID.randomUUID());
        Medicine medicine2 = new Medicine();
        medicine2.setId(UUID.randomUUID());
        List<Medicine> medicines = Arrays.asList(medicine1, medicine2);

        when(medicineService.getMedicinesByPharmacy(pharmacyId)).thenReturn(medicines);

        pharmacyService.deletePharmacy(pharmacyId);

        verify(medicineService, times(1)).deleteMedicineById(medicine1.getId());
        verify(medicineService, times(1)).deleteMedicineById(medicine2.getId());
        verify(pharmacyRepository, times(1)).deleteById(pharmacyId);
    }

    @Test
    void testCalculateDistances() {
        Pharmacy p1 = new Pharmacy();
        p1.setId(UUID.randomUUID());
        p1.setLatitude("52.5200");
        p1.setLongitude("13.4050");

        Pharmacy p2 = new Pharmacy();
        p2.setId(UUID.randomUUID());
        p2.setLatitude("48.8566");
        p2.setLongitude("2.3522");

        Medicine m1 = new Medicine();
        m1.setPharmacy(p1);

        Medicine m2 = new Medicine();
        m2.setPharmacy(p2);

        Product product1 = new Product();
        product1.setMedicine(m1);

        Product product2 = new Product();
        product2.setMedicine(m2);

        double userLat = 50.1109;
        double userLon = 8.6821;

        List<Product> productList = Arrays.asList(product1, product2);

        HashMap<UUID, Double> distances = pharmacyService.calculateDistances(productList, userLat, userLon);

        assertTrue(distances.containsKey(p1.getId()));
        assertTrue(distances.containsKey(p2.getId()));

        assertNotNull(distances.get(p1.getId()));
        assertNotNull(distances.get(p2.getId()));
    }
}
