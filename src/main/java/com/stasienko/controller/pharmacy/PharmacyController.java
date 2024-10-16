package com.stasienko.controller.pharmacy;

import com.stasienko.model.Medicine;
import com.stasienko.model.Product;
import com.stasienko.service.MedicineService;
import com.stasienko.service.ProductService;
import com.stasienko.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/pharmacy")
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/{id}")
    public String viewPharmacyProducts(@PathVariable("id") UUID pharmacyId, Model model) {
        List<Product> products = productService.getProductsBasedOnPharmacyId(pharmacyId);
        model.addAttribute("pharmacy", pharmacyService.getPharmacyById(pharmacyId));
        model.addAttribute("products", products);
        return "pharmacy/pharmacy-info";
    }

    @GetMapping("/{id}/list-medicines")
    public String showAllPharmacyMedicines(@PathVariable("id") UUID pharmacyId, Model model) {
        List<Medicine> medicines = medicineService.getMedicinesByPharmacy(pharmacyId);
        model.addAttribute("medicines", medicines);
        model.addAttribute("pharmacyId", pharmacyId);
        return "pharmacy/list-medicines";
    }
}
