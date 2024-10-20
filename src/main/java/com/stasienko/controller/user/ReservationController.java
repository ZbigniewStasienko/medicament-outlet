package com.stasienko.controller.user;

import com.stasienko.model.Product;
import com.stasienko.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String showCart(Model model, HttpSession session) {
        List<Product> productsInCart = (List<Product>) session.getAttribute("inCart");
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
    public String purchaseCart(HttpSession session) {
        List<Product> productsInCart = (List<Product>) session.getAttribute("inCart");
        session.removeAttribute("inCart");
        return "redirect:/";
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
