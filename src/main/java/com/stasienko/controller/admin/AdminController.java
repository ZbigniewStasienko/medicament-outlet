package com.stasienko.controller.admin;

import com.stasienko.model.Pharmacy;
import com.stasienko.model.Picture;
import com.stasienko.security.AuthorizationService;
import com.stasienko.service.PharmacyService;
import com.stasienko.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private PictureService pictureService;

    @GetMapping
    public String getAllPharmacies(@AuthenticationPrincipal OAuth2User principal, Model model) {
        List<Pharmacy> pharmacies = pharmacyService.getAllPharmacies();
        model.addAttribute("isSuperAdmin", AuthorizationService.isSuperAdmin(principal));
        model.addAttribute("pharmacies", pharmacies);
        return "admin/all-pharmacies";
    }

    @GetMapping("/new")
    public String showAddPharmacyForm(Model model) {
        model.addAttribute("pharmacy", new Pharmacy());
        return "admin/add-pharmacy.html";
    }

    @PostMapping("/new")
    public String addPharmacy(@ModelAttribute("pharmacy") Pharmacy pharmacy, @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            Picture savedPicture = pictureService.addPicture(file);
            pharmacy.setPicture(savedPicture);
        }
        pharmacyService.addPharmacy(pharmacy);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String showEditPharmacyForm(@PathVariable("id") String id, Model model) {
        Pharmacy pharmacy = pharmacyService.getPharmacyById(id);
        model.addAttribute("pharmacy", pharmacy);
        return "admin/edit-pharmacy";
    }

    @PostMapping("/edit/{id}")
    public String updatePharmacy(@PathVariable("id") String id,
                                 @ModelAttribute("pharmacy") Pharmacy pharmacy,
                                 @RequestParam("file") MultipartFile file) throws IOException {

        Pharmacy existingPharmacy = pharmacyService.getPharmacyById(id);
        if (!file.isEmpty()) {
            pictureService.deletePicture(existingPharmacy.getPicture().getId());
            Picture updatedPicture = pictureService.addPicture(file);
            pharmacy.setPicture(updatedPicture);
        } else {
            pharmacy.setPicture(existingPharmacy.getPicture());
        }

        pharmacyService.updatePharmacy(id, pharmacy);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deletePharmacy(@PathVariable("id") String id) {
        pharmacyService.deletePharmacy(id);
        return "redirect:/admin";
    }
}

