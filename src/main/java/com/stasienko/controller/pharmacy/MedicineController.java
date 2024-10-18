package com.stasienko.controller.pharmacy;

import com.stasienko.model.Medicine;
import com.stasienko.model.Pharmacy;
import com.stasienko.model.Picture;
import com.stasienko.service.MedicineService;
import com.stasienko.service.PharmacyService;
import com.stasienko.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/pharmacy")
public class MedicineController {

    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private PictureService pictureService;

    @GetMapping("/{id}/add-medicine")
    public String showAddMedicineForm(@PathVariable("id") UUID pharmacyId, Model model) {
        model.addAttribute("medicine", new Medicine());
        model.addAttribute("pharmacyId", pharmacyId);
        return "pharmacy/add-medicine";
    }

    @PostMapping("/{id}/add-medicine")
    public String saveMedicine(@PathVariable("id") String pharmacyId, @ModelAttribute Medicine medicine,
                               @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            Picture savedPicture = pictureService.addPicture(file);
            medicine.setPicture(savedPicture);
        }

        Pharmacy pharmacy = pharmacyService.getPharmacyById(pharmacyId);
        medicine.setPharmacy(pharmacy);

        medicineService.saveMedicine(medicine);

        return "redirect:/pharmacy/" + pharmacyId + "/add-product";
    }

    @GetMapping("/edit-medicine/{medicineId}")
    public String showEditMedicineForm(@PathVariable("medicineId") UUID medicineId, Model model) {
        Medicine toEdit = medicineService.getMedicineById(medicineId);
        model.addAttribute("medicine", toEdit);
        return "pharmacy/edit-medicine";
    }

    @PostMapping("/edit-medicine/{id}")
    public String editMedicine(@PathVariable("id") UUID medicineId,
                               @RequestParam("name") String name,
                               @RequestParam("description") String description,
                               @RequestParam("size") String size,
                               @RequestParam("file") MultipartFile file) throws IOException {
        Medicine medicine = medicineService.getMedicineById(medicineId);
        medicine.setName(name);
        medicine.setDescription(description);
        medicine.setSize(size);
        if (!file.isEmpty()) {
            pictureService.deletePicture(medicine.getPicture().getId());
            Picture savedPicture = pictureService.addPicture(file);
            medicine.setPicture(savedPicture);
        }
        medicineService.saveMedicine(medicine);
        return "redirect:/pharmacy/" + medicine.getPharmacy().getId();
    }

    @PostMapping("/delete-medicine")
    public String deleteMedicine(@RequestParam("medicineId") UUID medicineId) {
        String pharmacyId = medicineService.getMedicineById(medicineId).getPharmacy().getId();
        medicineService.deleteMedicineById(medicineId);
        return "redirect:/pharmacy/" + pharmacyId;
    }

}
