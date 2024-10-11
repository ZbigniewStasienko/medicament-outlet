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
        System.out.println(products.size());
        System.out.println(productService.getSizeOfAllProductsDataset());
        /*List<Product> all = productService.getProducts();
        for(int i = 0; i < productService.getSizeOfAllProductsDataset(); i++) {
            System.out.println(all.get(0).getMedicine().getPharmacy().getId());
        }*/
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

        product.setReserved(false);
        productService.saveProduct(product);

        return "redirect:/pharmacy/" + pharmacyId;
    }

}
