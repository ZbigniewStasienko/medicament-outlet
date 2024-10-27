package com.stasienko.controller.pharmacy;

import com.stasienko.model.Medicine;
import com.stasienko.model.Pharmacy;
import com.stasienko.model.Picture;
import com.stasienko.service.MedicineService;
import com.stasienko.service.PharmacyService;
import com.stasienko.service.PictureService;
import com.stasienko.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.IntegerSyntax;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/pharmacy")
public class MedicineController {

    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PictureService pictureService;

    @GetMapping("/listMedicines")
    public String showAllPharmacyMedicines(@RequestParam("pharmacyId") UUID pharmacyId, Model model) {
        List<Medicine> medicines = medicineService.getMedicinesByPharmacy(pharmacyId);
        HashMap<UUID, Integer> numOfProducts = medicineService.productsAssignedToMedicines(medicines);
        model.addAttribute("medicines", medicines);
        model.addAttribute("pharmacyId", pharmacyId);
        model.addAttribute("numOfProducts", numOfProducts);
        return "pharmacy/list-medicines";
    }

    @GetMapping("/addMedicine")
    public String showAddMedicineForm(@RequestParam("pharmacyId") UUID pharmacyId, Model model) {
        model.addAttribute("medicine", new Medicine());
        model.addAttribute("pharmacyId", pharmacyId);
        return "pharmacy/add-medicine";
    }

    @PostMapping("/addMedicine")
    public String saveMedicine(@RequestParam("pharmacyId") UUID pharmacyId, @ModelAttribute Medicine medicine,
                               @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            Picture savedPicture = pictureService.addPicture(file);
            medicine.setPicture(savedPicture);
        }

        Pharmacy pharmacy = pharmacyService.getPharmacyById(pharmacyId);
        medicine.setPharmacy(pharmacy);

        medicineService.saveMedicine(medicine);

        return "redirect:/pharmacy/add-product";
    }

    @GetMapping("/editMedicine/{medicineId}")
    public String showEditMedicineForm(@PathVariable("medicineId") UUID medicineId, Model model) {
        Medicine toEdit = medicineService.getMedicineById(medicineId);
        int affectedItems = productService.getProductsBasedOnMedicineId(medicineId).size();
        model.addAttribute("medicine", toEdit);
        model.addAttribute("affectedItems", affectedItems);
        return "pharmacy/edit-medicine";
    }

    @PostMapping("/editMedicine/{id}")
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
        return "redirect:/pharmacy";
    }

    @PostMapping("/deleteMedicine")
    public String deleteMedicine(@RequestParam("medicineId") UUID medicineId) {
        medicineService.deleteMedicineById(medicineId);
        return "redirect:/pharmacy";
    }

}
