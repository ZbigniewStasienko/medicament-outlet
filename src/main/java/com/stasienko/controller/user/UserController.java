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
@RequestMapping("/")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

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
        return "user/default-view";
    }

    private void ensureUserExists(OAuth2User principal) {
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
    }

}
