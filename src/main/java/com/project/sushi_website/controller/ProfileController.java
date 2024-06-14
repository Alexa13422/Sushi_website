package com.project.sushi_website.controller;

import com.project.sushi_website.mapper.CustomerMapper;
import com.project.sushi_website.model.City;
import com.project.sushi_website.model.DTO.CustomerDTO;
import com.project.sushi_website.model.Order;
import com.project.sushi_website.service.CityService;
import com.project.sushi_website.service.CustomerService;
import com.project.sushi_website.service.JwtService;
import com.project.sushi_website.service.OrderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final CustomerService customerService;
    private final CityService cityService;
    private final OrderService orderService;
    private final JwtService jwtService;

    public ProfileController(CustomerService customerService, CityService cityService, OrderService orderService, JwtService jwtService) {
        this.customerService = customerService;
        this.cityService = cityService;
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    @ModelAttribute("cities")
    public List<City> populateCities() {
        return cityService.getAllCities();
    }

    @GetMapping()
    public String showProfile(Model model, HttpServletRequest request) {
        String jwtToken = jwtService.extractJwtTokenFromCookies(request);

        CustomerDTO customerDTO = validateAndRetrieveCustomer(jwtToken);
        if (customerDTO == null) {
            return "redirect:/auth/login";
        }

        List<Order> orders = orderService.getOrdersByCustomerEmail(customerDTO.getEmail())
                .stream()
                .filter(order -> !orderService.isOrderActive(order))
                .collect(Collectors.toList());

        model.addAttribute("customer",customerDTO);
        model.addAttribute("orders", orders);
        return "profile";
    }


    @PostMapping
    public String updateProfile(
            @ModelAttribute("customer") @Valid CustomerDTO customerDTO,
            Errors errors,
            RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return "profile";
        }
        try {
            customerService.updateCustomer(customerDTO);
        } catch (Exception e) {
            return e.getMessage(); // Consider handling the exception appropriately
        }
        redirectAttributes.addFlashAttribute("message", "Profile updated successfully.");
        return "redirect:/profile";
    }
    private CustomerDTO validateAndRetrieveCustomer(String jwtToken) {
        if (jwtToken != null && jwtService.isTokenValid(jwtToken,jwtService.extractUsername(jwtToken))) {
            String username = jwtService.extractUsername(jwtToken);
            return CustomerMapper.toCustomerDTO(customerService.getCustomerByEmail(username));
        }
        return null;
    }
}

