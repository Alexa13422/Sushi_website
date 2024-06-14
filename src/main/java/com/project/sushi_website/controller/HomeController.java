package com.project.sushi_website.controller;

import com.project.sushi_website.model.Customer;
import com.project.sushi_website.model.DTO.ItemDTO;
import com.project.sushi_website.service.CustomerService;
import com.project.sushi_website.service.ItemService;
import com.project.sushi_website.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final CustomerService customerService;
    private final ItemService itemService;

    public HomeController(CustomerService customerService, ItemService itemService) {
        this.customerService = customerService;
        this.itemService = itemService;
    }

    @GetMapping("/mainPage")
    public String showMainPage(Model model, HttpServletRequest request) {
        List<ItemDTO> items = itemService.get4PopularItems();
        model.addAttribute("items", items);
        model.addAttribute("request", request);
        Customer customer = getCustomerFromAuth();
        if (customer.getUsername() != null){
            model.addAttribute("customer", customer);
        }
        return "mainPage";
    }
    @GetMapping("/about")
    public String aboutUs(Model model) {
        Customer customer = getCustomerFromAuth();
        if (customer.getUsername() != null){
            model.addAttribute("customer", customer);
        }
        return "about";
    }

    @GetMapping("/contact")
    public String contactUs(Model model) {
        Customer customer = getCustomerFromAuth();
        if (customer.getUsername() != null){
            model.addAttribute("customer", customer);
        }
        return "contact";
    }

    private Customer getCustomerFromAuth() {
        Customer customer = new Customer();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (!username.equals("anonymousUser")) {
            customer = customerService.getCustomerByEmail(username);
        }
        return customer;
    }
}
