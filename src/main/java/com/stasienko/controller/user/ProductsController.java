package com.stasienko.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/product")
public class ProductsController {

    @GetMapping("/sortedByLocation")
    public String getProductsSortedByLocation(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude) {
        System.out.println(latitude);
        System.out.println(longitude);
        return "redirect:/";
    }
}
