package com.stasienko.controller;

import com.stasienko.model.Medicine;
import com.stasienko.model.Pharmacy;
import com.stasienko.model.Picture;
import com.stasienko.model.Product;
import com.stasienko.service.MedicineService;
import com.stasienko.service.PictureService;
import com.stasienko.service.ProductService;
import com.stasienko.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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

    @Autowired
    private PictureService pictureService;

    @GetMapping("/{id}")
    public String viewPharmacyProducts(@PathVariable("id") UUID pharmacyId, Model model) {
        List<Product> products = productService.getProductsBasedOnPharmacyId(pharmacyId);
        model.addAttribute("pharmacy", pharmacyService.getPharmacyById(pharmacyId));
        model.addAttribute("products", products);
        return "pharmacy/pharmacy-info";
    }

    @GetMapping("/{id}/add-product")
    public String showAddProductForm(@PathVariable("id") UUID pharmacyId, Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("pharmacyId", pharmacyId);
        model.addAttribute("medicines", medicineService.getMedicinesByPharmacy(pharmacyId));
        return "pharmacy/add-product";
    }

    @GetMapping("/{id}/add-medicine")
    public String showAddMedicineForm(@PathVariable("id") UUID pharmacyId, Model model) {
        model.addAttribute("medicine", new Medicine());
        model.addAttribute("pharmacyId", pharmacyId);
        return "pharmacy/add-medicine";
    }

    @GetMapping("/edit-product/{productId}")
    public String showEditProductForm(@PathVariable("productId") UUID productId, Model model) {
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "pharmacy/edit-product";
    }

    @PostMapping("/{id}/save-medicine")
    public String saveMedicine(@PathVariable("id") UUID pharmacyId, @ModelAttribute Medicine medicine,
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

    @PostMapping("/edit-product/{productId}")
    public String editProduct(@PathVariable("productId") UUID productId,
                              @RequestParam("expirationDate") LocalDate expirationDate,
                              @RequestParam("isReserved") Boolean isReserved,
                              @RequestParam("basePrice") Double basePrice,
                              @RequestParam("price") Double price) {
        Product product = productService.getProductById(productId);

        product.setExpirationDate(expirationDate);
        product.setIsReserved(isReserved);
        product.setBasePrice(basePrice);
        product.setPrice(price);

        productService.saveProduct(product);

        return "redirect:/pharmacy/" + product.getMedicine().getPharmacy().getId();
    }

    @PostMapping("/delete-product/{productId}")
    public String deleteProduct(@PathVariable("productId") UUID productId) {
        UUID pharmacyID = productService.getProductById(productId).getMedicine().getPharmacy().getId();
        productService.deleteProductById(productId);
        return "redirect:/pharmacy/" + pharmacyID;
    }

    @GetMapping("/{id}/modify-medicine")
    public String showEditMedicineForm(@PathVariable("id") UUID pharmacyId, Model model) {
        List<Medicine> medicines = medicineService.getMedicinesByPharmacy(pharmacyId);
        model.addAttribute("medicines", medicines);
        model.addAttribute("pharmacyId", pharmacyId);
        return "pharmacy/edit-medicine";
    }

    @PostMapping("/{id}/edit-medicine")
    public String editMedicine(@PathVariable("id") UUID pharmacyId,
                               @RequestParam("medicineId") UUID medicineId,
                               @RequestParam("name") String name,
                               @RequestParam("description") String description,
                               @RequestParam("size") String size) {
        Medicine medicine = medicineService.getMedicineById(medicineId);
        if (medicine.getPharmacy().getId().equals(pharmacyId)) {
            medicine.setName(name);
            medicine.setDescription(description);
            medicine.setSize(size);
            medicineService.saveMedicine(medicine);
        }
        return "redirect:/pharmacy/" + pharmacyId + "/modify-medicine";
    }

    @PostMapping("/{id}/delete-medicine")
    public String deleteMedicine(@PathVariable("id") UUID pharmacyId, @RequestParam("medicineId") UUID medicineId) {
        Medicine medicine = medicineService.getMedicineById(medicineId);
        if (medicine.getPharmacy().getId().equals(pharmacyId)) {
            medicineService.deleteMedicineById(medicineId);
        }
        return "redirect:/pharmacy/" + pharmacyId + "/modify-medicine";
    }
}
