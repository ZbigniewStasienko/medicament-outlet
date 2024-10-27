package com.stasienko.service;

import com.stasienko.model.Medicine;
import com.stasienko.model.Product;
import com.stasienko.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private ProductService productService;

    public List<Medicine> getMedicinesByPharmacy(UUID pharmacyId) {
        return medicineRepository.findByPharmacyId(pharmacyId);
    }

    public Medicine getMedicineById(UUID medicineId) {
        return medicineRepository.findById(medicineId).orElse(null);
    }

    public Medicine saveMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    public void deleteMedicineById(UUID medicineId) {
        List<Product> toDelete = productService.getProductsBasedOnMedicineId(medicineId);
        for (Product toDel : toDelete) {
            productService.deleteProductById(toDel.getId());
        }
        medicineRepository.deleteById(medicineId);
    }

    public HashMap<UUID, Integer> productsAssignedToMedicines(List<Medicine> medicines) {
        HashMap<UUID, Integer> out = new HashMap<>();
        for (Medicine medicine : medicines) {
            int numOfProducts = productService.getProductsBasedOnMedicineId(medicine.getId()).size();
            out.put(medicine.getId(), numOfProducts);
        }
        return out;
    }

    public List<Medicine> getMedicinesByPharmacyAndName(UUID pharmacyId, String name) {
        return medicineRepository.findByPharmacyIdAndNameContainingIgnoreCase(pharmacyId, name);
    }


}

