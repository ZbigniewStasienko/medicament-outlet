package com.stasienko.controller.pharmacy;

import com.stasienko.model.Medicine;
import com.stasienko.model.Product;
import com.stasienko.service.MedicineService;
import com.stasienko.service.ProductService;
import com.stasienko.service.UUIDConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/pharmacy")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/add-product")
    public String showAddProductForm(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            model.addAttribute("product", new Product());
            String tempId = principal.getAttribute("user_id");
            UUID pharmacyId = UUIDConverter.convertStringToUUID(tempId);
            model.addAttribute("pharmacyId", pharmacyId);
            model.addAttribute("medicines", medicineService.getMedicinesByPharmacy(pharmacyId));
        }
        return "pharmacy/add-product";
    }

    @PostMapping("/add-product")
    public String addProduct(@RequestParam("medicineId") UUID medicineId,
                             @ModelAttribute Product product) {
        if (medicineId != null) {
            Medicine medicine = medicineService.getMedicineById(medicineId);
            product.setMedicine(medicine);
        }

        product.setIsReserved(false);
        productService.saveProduct(product);

        return "redirect:/pharmacy";
    }

    @GetMapping("/edit-product/{productId}")
    public String showEditProductForm(@PathVariable("productId") UUID productId, Model model) {
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "pharmacy/edit-product";
    }

    @PostMapping("/edit-product/{productId}")
    public String editProduct(@PathVariable("productId") UUID productId,
                              @RequestParam("expirationDate") LocalDate expirationDate,
                              @RequestParam("isReserved") String isReserved,
                              @RequestParam("basePrice") Double basePrice,
                              @RequestParam("price") Double price) {
        Product product = productService.getProductById(productId);

        product.setExpirationDate(expirationDate);
        product.setIsReserved(Boolean.parseBoolean(isReserved));
        product.setBasePrice(basePrice);
        product.setPrice(price);

        productService.saveProduct(product);

        return "redirect:/pharmacy";
    }


    @PostMapping("/delete-product/{productId}")
    public String deleteProduct(@PathVariable("productId") UUID productId) {
        productService.deleteProductById(productId);
        return "redirect:/pharmacy";
    }
}
