package com.stasienko.service;

import com.stasienko.model.Medicine;
import com.stasienko.model.Product;
import com.stasienko.repository.MedicineRepository;
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
class MedicineServiceTest {

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private MedicineService medicineService;

    private UUID pharmacyId;
    private UUID medicineId;
    private Medicine medicine;

    @BeforeEach
    void setUp() {
        pharmacyId = UUID.randomUUID();
        medicineId = UUID.randomUUID();

        medicine = new Medicine();
        medicine.setId(medicineId);
        medicine.setName("Aspirin");
    }

    @Test
    void testGetMedicinesByPharmacy() {
        List<Medicine> medicines = Collections.singletonList(medicine);
        when(medicineRepository.findByPharmacyId(pharmacyId)).thenReturn(medicines);

        List<Medicine> result = medicineService.getMedicinesByPharmacy(pharmacyId);

        assertEquals(medicines, result);
        verify(medicineRepository, times(1)).findByPharmacyId(pharmacyId);
    }

    @Test
    void testGetMedicineById_Found() {
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(medicine));

        Medicine result = medicineService.getMedicineById(medicineId);

        assertEquals(medicine, result);
        verify(medicineRepository, times(1)).findById(medicineId);
    }

    @Test
    void testGetMedicineById_NotFound() {
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.empty());

        Medicine result = medicineService.getMedicineById(medicineId);

        assertNull(result);
        verify(medicineRepository, times(1)).findById(medicineId);
    }

    @Test
    void testSaveMedicine() {
        when(medicineRepository.save(medicine)).thenReturn(medicine);

        Medicine result = medicineService.saveMedicine(medicine);

        assertEquals(medicine, result);
        verify(medicineRepository, times(1)).save(medicine);
    }

    @Test
    void testDeleteMedicineById() {
        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        Product product2 = new Product();
        product2.setId(UUID.randomUUID());

        List<Product> productsForMedicine = Arrays.asList(product1, product2);
        when(productService.getProductsBasedOnMedicineId(medicineId)).thenReturn(productsForMedicine);

        medicineService.deleteMedicineById(medicineId);

        verify(productService, times(1)).deleteProductById(product1.getId());
        verify(productService, times(1)).deleteProductById(product2.getId());

        verify(medicineRepository, times(1)).deleteById(medicineId);
    }

    @Test
    void testProductsAssignedToMedicines() {
        Medicine medicine1 = new Medicine();
        medicine1.setId(UUID.randomUUID());

        Medicine medicine2 = new Medicine();
        medicine2.setId(UUID.randomUUID());

        List<Product> productsForMed1 = Arrays.asList(new Product(), new Product());
        List<Product> productsForMed2 = Collections.singletonList(new Product());

        when(productService.getProductsBasedOnMedicineId(medicine1.getId())).thenReturn(productsForMed1);
        when(productService.getProductsBasedOnMedicineId(medicine2.getId())).thenReturn(productsForMed2);

        List<Medicine> medicines = Arrays.asList(medicine1, medicine2);
        HashMap<UUID, Integer> result = medicineService.productsAssignedToMedicines(medicines);

        assertEquals(2, result.size());
        assertEquals(2, result.get(medicine1.getId()));
        assertEquals(1, result.get(medicine2.getId()));

        verify(productService, times(1)).getProductsBasedOnMedicineId(medicine1.getId());
        verify(productService, times(1)).getProductsBasedOnMedicineId(medicine2.getId());
    }

    @Test
    void testGetMedicinesByPharmacyAndName() {
        String name = "asp";
        List<Medicine> medicines = Collections.singletonList(medicine);
        when(medicineRepository.findByPharmacyIdAndNameContainingIgnoreCase(pharmacyId, name)).thenReturn(medicines);

        List<Medicine> result = medicineService.getMedicinesByPharmacyAndName(pharmacyId, name);

        assertEquals(medicines, result);
        verify(medicineRepository, times(1))
                .findByPharmacyIdAndNameContainingIgnoreCase(pharmacyId, name);
    }
}
