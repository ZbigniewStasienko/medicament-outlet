package com.stasienko.controller.pharmacy;

import com.stasienko.model.Medicine;
import com.stasienko.model.Product;
import com.stasienko.security.AuthorizationService;
import com.stasienko.service.MedicineService;
import com.stasienko.service.ProductService;
import com.stasienko.service.PharmacyService;
import com.stasienko.service.UUIDConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    @GetMapping()
    public String viewPharmacyProducts(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            String tempId = principal.getAttribute("user_id");
            UUID pharmacyId = UUIDConverter.convertStringToUUID(tempId);
            List<Product> products = productService.getProductsBasedOnPharmacyId(pharmacyId);
            model.addAttribute("pharmacy", pharmacyService.getPharmacyById(pharmacyId));
            model.addAttribute("products", products);
        }
        model.addAttribute("isPharmacy", AuthorizationService.isPharmacy(principal));
        return "pharmacy/pharmacy-info";
    }

    @GetMapping("/list-medicines")
    public String showAllPharmacyMedicines(@RequestParam("pharmacyId") UUID pharmacyId, Model model) {
        List<Medicine> medicines = medicineService.getMedicinesByPharmacy(pharmacyId);
        model.addAttribute("medicines", medicines);
        model.addAttribute("pharmacyId", pharmacyId);
        return "pharmacy/list-medicines";
    }
}
