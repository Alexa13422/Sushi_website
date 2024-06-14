package com.project.sushi_website.controller;

import com.project.sushi_website.model.Customer;
import com.project.sushi_website.model.DTO.ItemDTO;
import com.project.sushi_website.service.CustomerService;
import com.project.sushi_website.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {

    private final ItemService itemService;
    private final CustomerService customerService;

    public MenuController(ItemService itemService, CustomerService customerService) {
        this.itemService = itemService;
        this.customerService = customerService;
    }

    @GetMapping
    public String getAllMenuItems(Model model, HttpServletRequest request) {
        List<ItemDTO> items = itemService.getAllActiveItems();
        model.addAttribute("items", items);
        model.addAttribute("request", request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (!username.equals("anonymousUser")) {
            Customer customer = customerService.getCustomerByEmail(username);
            model.addAttribute("customer", customer);
        }
        return "menu";
    }
}
