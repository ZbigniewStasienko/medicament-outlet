package com.stasienko.controller.pharmacy;

import com.stasienko.model.Medicine;
import com.stasienko.model.Product;
import com.stasienko.service.MedicineService;
import com.stasienko.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@Controller
@RequestMapping("/pharmacy")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/{id}/add-product")
    public String showAddProductForm(@PathVariable("id") String pharmacyId, Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("pharmacyId", pharmacyId);
        model.addAttribute("medicines", medicineService.getMedicinesByPharmacy(pharmacyId));
        return "pharmacy/add-product";
    }

    @PostMapping("/{id}/add-product")
    public String addProduct(@PathVariable("id") UUID pharmacyId,
                             @RequestParam("medicineId") UUID medicineId,
                             @ModelAttribute Product product) {
        if (medicineId != null) {
            Medicine medicine = medicineService.getMedicineById(medicineId);
            product.setMedicine(medicine);
        }

        product.setIsReserved(false);
        productService.saveProduct(product);

        return "redirect:/pharmacy/" + pharmacyId;
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

        return "redirect:/pharmacy/" + product.getMedicine().getPharmacy().getId();
    }


    @PostMapping("/delete-product/{productId}")
    public String deleteProduct(@PathVariable("productId") UUID productId) {
        String pharmacyID = productService.getProductById(productId).getMedicine().getPharmacy().getId();
        productService.deleteProductById(productId);
        return "redirect:/pharmacy/" + pharmacyID;
    }
}
