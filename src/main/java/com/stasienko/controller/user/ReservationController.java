package com.stasienko.controller.user;

import com.stasienko.model.Product;
import com.stasienko.security.AuthorizationService;
import com.stasienko.service.ProductService;
import com.stasienko.service.ReservationService;
import com.stasienko.service.UUIDConverter;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    ProductService productService;

    @Autowired
    ReservationService reservationService;

    @PostMapping("/addToCart")
    public String addProductToCart(@RequestParam("productId") UUID productId, HttpSession session) {
        List<Product> productsInCart = (List<Product>) session.getAttribute("inCart");
        if (productsInCart == null) {
            productsInCart = new ArrayList<>();
        }
        Product product = productService.updateProductAvailability(productId, true);
        productsInCart.add(product);
        session.setAttribute("inCart", productsInCart);
        return "redirect:/";
    }

    @GetMapping("/showCart")
    public String showCart(Model model, HttpSession session, @AuthenticationPrincipal OAuth2User principal) {
        List<Product> productsInCart = (List<Product>) session.getAttribute("inCart");
        model.addAttribute("isUser", AuthorizationService.isUser(principal));
        model.addAttribute("productsInCart", productsInCart);
        return "user/cart";
    }

    @PostMapping("/deleteProduct")
    public String deleteProductFromCart(@RequestParam("productId") UUID productId, HttpSession session) {
        List<Product> productsInCart = (List<Product>) session.getAttribute("inCart");
        if (productsInCart != null) {
            Product productToRemove = null;
            for (Product product : productsInCart) {
                if (product.getId().equals(productId)) {
                    productToRemove = product;
                    break;
                }
            }
            if (productToRemove != null) {
                productsInCart.remove(productToRemove);
                productService.updateProductAvailability(productId, false);
            }
        }
        session.setAttribute("inCart", productsInCart);
        return "redirect:/reservation/showCart";
    }


    @PostMapping("/purchase")
    public String purchaseCart(HttpSession session, @AuthenticationPrincipal OAuth2User principal, Model model) {
        if (!AuthorizationService.isUser(principal)) {
            List<Product> productsInCart = (List<Product>) session.getAttribute("inCart");
            model.addAttribute("productsInCart", productsInCart);
            model.addAttribute("error", "To purchase products you have to be logged in!");
            model.addAttribute("isUser", false);
            return "user/cart";
        }
        List<Product> productsInCart = (List<Product>) session.getAttribute("inCart");
        UUID userId = UUIDConverter.convertToUUID(principal);
        int numOfPharmacies = reservationService.saveProducts(userId, productsInCart);
        session.removeAttribute("inCart");
        model.addAttribute("numberOfPharmacies", numOfPharmacies);
        return "user/successful-purchase";
    }

    @PostMapping("/deleteProducts")
    public String deleteAllProductsFromCart(HttpSession session) {
        List<Product> productsInCart = (List<Product>) session.getAttribute("inCart");
        if (productsInCart != null) {
            for (Product product : productsInCart) {
                productService.updateProductAvailability(product.getId(), false);
            }
        }
        session.removeAttribute("inCart");
        return "redirect:/reservation/showCart";
    }

}
