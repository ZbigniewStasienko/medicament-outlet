package com.stasienko.controller.user;

import com.stasienko.model.Product;
import com.stasienko.model.User;
import com.stasienko.security.AuthorizationService;
import com.stasienko.service.ProductService;
import com.stasienko.service.UUIDConverter;
import com.stasienko.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/product")
public class ProductsController {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @GetMapping("/sortedByLocation")
    public String getProductsSortedByLocation(Model model, @AuthenticationPrincipal OAuth2User principal,
                                              @RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude) {
        if (AuthorizationService.isUser(principal)) {
            UUID userId = UUIDConverter.convertToUUID(principal);
            if (userService.findUserById(userId) == null) {
                User user = new User();
                user.setId(userId);
                user.setName("test");
                user.setSurname("surname");
                userService.saveUser(user);
            }
        }
        List<Product> products = productService.getSortedByDistance(latitude, longitude);
        model.addAttribute("products", products);
        model.addAttribute("isUser", AuthorizationService.isUser(principal));
        return "user/default-view";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("searchTerm") String searchTerm, @AuthenticationPrincipal OAuth2User principal,
                                 Model model) {
        if (AuthorizationService.isUser(principal)) {
            UUID userId = UUIDConverter.convertToUUID(principal);
            if (userService.findUserById(userId) == null) {
                User user = new User();
                user.setId(userId);
                user.setName("test");
                user.setSurname("surname");
                userService.saveUser(user);
            }
        }
        List<Product> products = productService.searchProducts(searchTerm);
        model.addAttribute("products", products);
        model.addAttribute("isUser", AuthorizationService.isUser(principal));
        return "user/default-view";
    }
}
