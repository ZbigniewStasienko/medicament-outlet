package com.stasienko.service;

import com.stasienko.model.Medicine;
import com.stasienko.model.Pharmacy;
import com.stasienko.model.Product;
import com.stasienko.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    public HashMap<UUID, Double> calculateDistances(List<Product> products, double latitude, double longitude) {
        HashMap<UUID, Double> out = new HashMap<>();
        for(Product product : products) {
            double distance =
                    calculateDistance(latitude, longitude, Double.parseDouble(product.getMedicine().getPharmacy().getLatitude()),
                            Double.parseDouble(product.getMedicine().getPharmacy().getLongitude()));
            distance = Math.round(distance * 100.0) / 100.0;
            out.put(product.getMedicine().getPharmacy().getId(), distance);
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
}
