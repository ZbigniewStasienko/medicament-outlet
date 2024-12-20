package com.stasienko.controller.pharmacy;

import com.stasienko.model.Product;
import com.stasienko.model.Reservation;
import com.stasienko.model.ReservedProduct;
import com.stasienko.service.ProductService;
import com.stasienko.service.ReservationService;
import com.stasienko.service.ReservedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/pharmacy")
public class ReservationsController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReservedProductService reservedProductService;

    @GetMapping("/listReservations")
    public String viewPharmacyReservations(@RequestParam("pharmacyId") UUID pharmacyId, Model model) {
        List<Reservation> reservations = reservationService.getReservationByPharmacyId(pharmacyId);
        Map<UUID, List<Product>> products = new HashMap<>();
        for(Reservation reservation : reservations) {
            List<ReservedProduct> reservedProducts = reservedProductService.getProductsAssignedForReservation(reservation.getId());
            List<Product> productsReservation = productService.getProductsForReservation(reservedProducts);
            products.put(reservation.getId(), productsReservation);
        }
        model.addAttribute("reservations", reservations);
        model.addAttribute("products", products);
        model.addAttribute("pharmacyId", pharmacyId);
        model.addAttribute("todaysDate", LocalDate.now());
        return "pharmacy/list-reservations";
    }

    @PostMapping("/updateReservationStatus")
    public String updateStatus(@RequestParam("reservationId") UUID reservationId, @RequestParam("status") String status) {
        Reservation reservation = reservationService.updateReservationStatus(reservationId, Integer.parseInt(status));
        return "redirect:/pharmacy/listReservations?pharmacyId=" + reservation.getPharmacy().getId();
    }

    @PostMapping("/reservationCollected")
    public String collectedReservation(@RequestParam("reservationId") UUID reservationId) {
        UUID pharmacyId = reservationService.getPharmacyId(reservationId);
        reservationService.reservationCollected(reservationId);
        return "redirect:/pharmacy/listReservations?pharmacyId=" + pharmacyId;
    }

    @PostMapping("/deleteReservation")
    public String deleteReservation(@RequestParam("reservationId") UUID reservationId) {
        UUID pharmacyId = reservationService.getPharmacyId(reservationId);
        reservationService.deleteReservation(reservationId);
        return "redirect:/pharmacy/listReservations?pharmacyId=" + pharmacyId;
    }
}