package com.stasienko.controller.user;

import com.stasienko.model.Product;
import com.stasienko.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private ProductService productService;

    @GetMapping()
    public String viewAllProducts(Model model, @AuthenticationPrincipal OAuth2User principal) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        if (principal != null) {
            System.out.println("User display name: " + principal.getName());
            System.out.println("User Unique ID: " + principal.getAttribute("sub"));
            System.out.println("User roles: " + principal.getAttribute("roles"));
            System.out.println("User type: " + principal.getAttribute("type"));
            System.out.println("All available data: " + principal);
            System.out.println(Collections.singletonMap("name", principal.getName()));
        }
        return "user/default-view";
    }
}