package com.stasienko.service;

import com.stasienko.model.Medicine;
import com.stasienko.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    public List<Medicine> getMedicinesByPharmacy(UUID pharmacyId) {
        return medicineRepository.findByPharmacyId(pharmacyId);
    }

    public Medicine getMedicineById(UUID medicineId) {
        return medicineRepository.findById(medicineId).orElse(null);
    }

    public Medicine saveMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }
}

