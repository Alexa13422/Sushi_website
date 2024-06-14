package com.project.sushi_website.controller;

import com.project.sushi_website.model.Category;
import com.project.sushi_website.model.Customer;
import com.project.sushi_website.model.DTO.ItemDTO;
import com.project.sushi_website.model.Item;
import com.project.sushi_website.service.CategoryService;
import com.project.sushi_website.service.CustomerService;
import com.project.sushi_website.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static String UPLOAD_DIR = "src/main/resources/static/images/sushi";

    private final ItemService itemService;
    private final CategoryService categoryService;
    private final CustomerService customerService;

    public AdminController(ItemService itemService, CategoryService categoryService, CustomerService customerService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
        this.customerService = customerService;
    }

    @GetMapping("/new-item")
    public String showNewItemForm(Model model) {
        model.addAttribute("item", new Item());
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "admin/new-item";
    }

    @PostMapping("/new-item")
    public String createNewItem(@ModelAttribute Item item, @RequestParam("image") MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String fileName = imageFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath);

                item.setImageUrl(fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        itemService.save(item);
        return "redirect:/menu";
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<Customer> allUsers = customerService.getAllCustomers();
        List<ItemDTO> itemStatistics = itemService.getAllItems();

        model.addAttribute("allUsers", allUsers);
        model.addAttribute("itemStatistics", itemStatistics);

        return "admin/admin-dashboard";
    }
    @PostMapping("/deactivateItem/{itemId}")
    public String deleteItem(@PathVariable("itemId") Integer itemId, RedirectAttributes redirectAttributes) {
        itemService.deactivateItem(itemId);
        redirectAttributes.addFlashAttribute("successMessage", "Item deleted successfully.");
        return "redirect:/admin/dashboard";
    }
}