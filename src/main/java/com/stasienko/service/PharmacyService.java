package com.stasienko.service;

import com.stasienko.model.Medicine;
import com.stasienko.model.Pharmacy;
import com.stasienko.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PharmacyService {
    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private MedicineService medicineService;

    public List<Pharmacy> getAllPharmacies() {
        return pharmacyRepository.findAll();
    }

    public Pharmacy getPharmacyById(UUID id) {
        return pharmacyRepository.findById(id).orElse(null);
    }

    public Pharmacy addPharmacy(Pharmacy pharmacy) {
        return pharmacyRepository.save(pharmacy);
    }

    public Pharmacy updatePharmacy(UUID id, Pharmacy updatedPharmacy) {
        return pharmacyRepository.findById(id)
                .map(pharmacy -> {
                    pharmacy.setAddress(updatedPharmacy.getAddress());
                    pharmacy.setPhoneNumber(updatedPharmacy.getPhoneNumber());
                    pharmacy.setName(updatedPharmacy.getName());
                    pharmacy.setEmail(updatedPharmacy.getEmail());
                    pharmacy.setLatitude(updatedPharmacy.getLatitude());
                    pharmacy.setLongitude(updatedPharmacy.getLongitude());
                    pharmacy.setOpeningHours(updatedPharmacy.getOpeningHours());
                    pharmacy.setPicture(updatedPharmacy.getPicture());
                    return pharmacyRepository.save(pharmacy);
                }).orElseGet(() -> {
                    updatedPharmacy.setId(id);
                    return pharmacyRepository.save(updatedPharmacy);
                });
    }

    public void deletePharmacy(UUID id) {
        List<Medicine> toDel = medicineService.getMedicinesByPharmacy(id);
        for (Medicine toDelete : toDel) {
            medicineService.deleteMedicineById(toDelete.getId());
        }
        pharmacyRepository.deleteById(id);
    }
}
