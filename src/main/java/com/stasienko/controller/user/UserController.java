package com.stasienko.controller.user;

import com.stasienko.model.*;
import com.stasienko.security.AuthorizationService;
import com.stasienko.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class UserController {

    @Value("${PROFILE_LINK}")
    private String profileLink;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservedProductService reservedProductService;

    @Autowired
    PharmacyService pharmacyService;

    @Autowired
    RetrieveNameService retrieveNameService;

    @GetMapping()
    public String getProducts(Model model,
                              @AuthenticationPrincipal OAuth2User principal,
                              @RequestParam(value = "searchTerm", required = false) String searchTerm,
                              @RequestParam(value = "sortBy", required = false) String sortBy,
                              @RequestParam(value = "latitude", required = false) Double latitude,
                              @RequestParam(value = "longitude", required = false) Double longitude) {

        ensureUserExists(principal);

        List<Product> products = productService.getProducts(searchTerm, sortBy, latitude, longitude);

        model.addAttribute("products", products);
        model.addAttribute("isUser", AuthorizationService.isUser(principal));
        model.addAttribute("searchTerm", searchTerm);

        HashMap<UUID, Double> distances = new HashMap<>();

        if(latitude != null && longitude != null) {
            distances = pharmacyService.calculateDistances(products, latitude, longitude);
        }
        model.addAttribute("distances", distances);
        model.addAttribute("areDistancesEmpty", distances.isEmpty());
        return "user/default-view";
    }

    @GetMapping("viewPharmacy")
    public String getProductsForPharmacy(Model model,
                              @AuthenticationPrincipal OAuth2User principal,
                              @RequestParam("pharmacyId") UUID pharmacyId,
                              @RequestParam(value = "searchTerm", required = false) String searchTerm,
                              @RequestParam(value = "sortBy", required = false) String sortBy) {

        ensureUserExists(principal);
        Pharmacy pharmacy = pharmacyService.getPharmacyById(pharmacyId);
        List<Product> products = productService.getProducts(searchTerm, sortBy, null, null);
        products = productService.filterProductsBasedOnPharmacyId(products, pharmacyId);
        model.addAttribute("pharmacy", pharmacy);
        model.addAttribute("products", products);
        model.addAttribute("isUser", AuthorizationService.isUser(principal));
        model.addAttribute("searchTerm", searchTerm);
        return "user/pharmacy-view";
    }

    @GetMapping("userProfile")
    public String viewProfile(Model model, @AuthenticationPrincipal OAuth2User principal) {
        List<String> userNameAndAddress = retrieveNameService.retrieveNameAndMail();
        UUID userId = UUIDConverter.convertToUUID(principal);
        List<Reservation> reservations = reservationService.getReservationByUserId(userId);
        Map<UUID, List<Product>> products = new HashMap<>();
        for(Reservation reservation : reservations) {
            List<ReservedProduct> reservedProducts = reservedProductService.getProductsAssignedForReservation(reservation.getId());
            List<Product> productsReservation = productService.getProductsForReservation(reservedProducts);
            products.put(reservation.getId(), productsReservation);
        }
        model.addAttribute("profileLink", profileLink);
        model.addAttribute("name", userNameAndAddress.get(0));
        model.addAttribute("dateToCompare", LocalDate.now().plusDays(2));
        model.addAttribute("reservations", reservations);
        model.addAttribute("products", products);
        return "user/profile";
    }

    private void ensureUserExists(OAuth2User principal) {
        if (AuthorizationService.isUser(principal)) {
            UUID userId = UUIDConverter.convertToUUID(principal);
            if (userService.findUserById(userId) == null) {
                User user = new User();
                user.setId(userId);
                userService.saveUser(user);
            }
        }
    }

}
