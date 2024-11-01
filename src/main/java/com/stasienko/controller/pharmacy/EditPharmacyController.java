package com.stasienko.controller.pharmacy;

import com.stasienko.model.Pharmacy;
import com.stasienko.model.Picture;
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
public class EditPharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private PictureService pictureService;

    @GetMapping("/edit")
    public String showEditPharmacyForm(@RequestParam("pharmacyId") UUID pharmacyId, Model model) {
        Pharmacy pharmacy = pharmacyService.getPharmacyById(pharmacyId);
        model.addAttribute("pharmacy", pharmacy);
        return "pharmacy/edit-pharmacy";
    }

    @PostMapping("/edit")
    public String updatePharmacy(@RequestParam("pharmacyId") UUID pharmacyId,
                                 @ModelAttribute("pharmacy") Pharmacy pharmacy,
                                 @RequestParam("file") MultipartFile file) throws IOException {

        Pharmacy existingPharmacy = pharmacyService.getPharmacyById(pharmacyId);
        if (!file.isEmpty()) {
            Picture updatedPicture = pictureService.addPicture(file);
            pharmacy.setPicture(updatedPicture);
        } else {
            pharmacy.setPicture(existingPharmacy.getPicture());
        }

        pharmacyService.updatePharmacy(pharmacyId, pharmacy);
        return "redirect:/pharmacy";
    }
}
