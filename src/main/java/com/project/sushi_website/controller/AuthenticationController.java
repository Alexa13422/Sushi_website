package com.project.sushi_website.controller;

import com.project.sushi_website.model.Customer;
import com.project.sushi_website.model.DTO.CustomerDTO;
import com.project.sushi_website.service.AuthenticationService;
import com.project.sushi_website.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/registration")
    public ModelAndView showRegistrationForm(Model model) {
        model.addAttribute("customerRequest", new CustomerDTO());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("registration");
        return mav;
    }

    @PostMapping("/registration")
    public ModelAndView registerUserAccount(
            @ModelAttribute("customerRequest") @Valid CustomerDTO customerDTO,
            RedirectAttributes redirectAttributes,
            Errors errors) {
        ModelAndView mav = new ModelAndView();

        if (errors.hasErrors()) {
            mav.setViewName("registration");
            return mav;
        }
        try {
            Customer registeredUser = authenticationService.signup(customerDTO);
            redirectAttributes.addFlashAttribute("customer", customerDTO);
            mav.setViewName("redirect:/mainPage");
            return mav;
        } catch (Exception ex) {
            mav.setViewName("registration");
            mav.addObject("message", "An error occurred during registration.");
            return mav;
        }
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm(Model model) {
        model.addAttribute("customerRequest", new CustomerDTO());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }

    @PostMapping("/login")
    public ModelAndView loginUserAccount(
            @ModelAttribute("customerRequest") @Valid CustomerDTO customerDTO,
            HttpServletResponse response,
            Errors errors) {
        ModelAndView mav = new ModelAndView();
        if (errors.hasErrors()) {
            mav.setViewName("login");
            return mav;
        }
        try {
            Customer authenticatedUser = authenticationService.authenticate(customerDTO);
            if (authenticatedUser == null) {
                mav.setViewName("login");
                mav.addObject("message", "Incorrect password.");
                return mav;
            }
            String jwtToken = jwtService.generateToken(authenticatedUser);

            // Set the JWT token in a cookie
            Cookie cookie = new Cookie("jwtToken", jwtToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) jwtService.getExpirationTime() / 1000);
            response.addCookie(cookie);

            mav.setViewName("redirect:/mainPage");
            return mav;
        } catch (Exception ex) {
            mav.setViewName("login");
            mav.addObject("message", "An error occurred during login.");
            return mav;
        }
    }

    @PostMapping("/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        String token = jwtService.extractJwtTokenFromCookies(request);

        if (token != null) {
            String username = jwtService.extractUsername(token);
            if (jwtService.isTokenValid(token, username)) {
                jwtService.invalidateToken(token);
            }
        }

        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        redirectAttributes.addFlashAttribute("message", "You have been successfully logged out.");
        mav.setViewName("redirect:/mainPage");
        return mav;
    }
}
