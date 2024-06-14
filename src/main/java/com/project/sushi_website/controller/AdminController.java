package com.project.sushi_website.controller;

import com.project.sushi_website.model.Item;
import com.project.sushi_website.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    ItemService itemService;

    @GetMapping("/new-item")
    public String showNewItemForm(Model model) {
        model.addAttribute("item", new Item());
        return "admin/new-item";
    }

    @PostMapping("/new-item")
    public String createNewItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        // Add logic to save the item to the database
        itemService.save(item);
        redirectAttributes.addFlashAttribute("message", "Item created successfully!");
        return "redirect:/menu";
    }
}