package com.project.sushi_website.controller;

import com.project.sushi_website.model.*;
import com.project.sushi_website.model.DTO.ItemOrderDTO;
import com.project.sushi_website.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@SessionAttributes("customer")
public class OrderController {
    private final JwtService jwtService;

    private final OrderService orderService;
    private final CustomerService customerService;
    private final ItemService itemService;
    private final ItemOrderService itemOrderService;
    private final StatusService statusService;
    private final StatusHistoryService statusHistoryService;
    private final PromoCodeService promoCodeService;

    public OrderController(OrderService orderService, CustomerService customerService, ItemService itemService, ItemOrderService itemOrderService, StatusService statusService, StatusHistoryService statusHistoryService, JwtService jwtService, PromoCodeService promoCodeService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.itemService = itemService;
        this.itemOrderService = itemOrderService;
        this.statusService = statusService;
        this.statusHistoryService = statusHistoryService;
        this.jwtService = jwtService;
        this.promoCodeService = promoCodeService;
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam Integer itemId,
                            @RequestParam(defaultValue = "1") int quantity,
                            @RequestParam String currentUrl,
                            Model model,
                            HttpServletRequest request,
                            RedirectAttributes redirectAttributes) {
        String jwtToken = jwtService.extractJwtTokenFromCookies(request);
        if (jwtToken == null || !jwtService.isTokenValid(jwtToken, jwtService.extractUsername(jwtToken))) {
            return "redirect:/auth/login";
        }

        String username = jwtService.extractUsername(jwtToken);
        Customer customer = customerService.getCustomerByEmail(username);
        try {
            Item item = itemService.getItemById(itemId);
            Order activeOrder = orderService.getActiveOrder(customer);
            if (activeOrder != null) {
                ItemOrder itemOrder = itemOrderService.getItemOrderByOrderIdAndItemId(activeOrder.getId(), itemId);
                if (itemOrder != null) {
                    item.setNumOfOrders(item.getNumOfOrders()+(quantity-itemOrder.getQuantity()));
                    itemOrder.setQuantity(itemOrder.getQuantity() + quantity);
                    itemOrder.calculatePrice();
                    itemOrderService.save(itemOrder);
                } else {
                    ItemOrder newItemOrder = new ItemOrder();
                    item.setNumOfOrders(item.getNumOfOrders()+quantity);
                    newItemOrder.setOrder(activeOrder);
                    newItemOrder.setItem(item);
                    newItemOrder.setQuantity(quantity);
                    newItemOrder.calculatePrice();
                    itemOrderService.save(newItemOrder);
                }
                activeOrder.calculatePrice();
                orderService.save(activeOrder);
            } else {
                Order newOrder = new Order();
                newOrder.setCustomer(customer);
                newOrder.setPrice(item.getPrice().multiply(BigDecimal.valueOf(quantity)));
                orderService.save(newOrder);

                item.setNumOfOrders(item.getNumOfOrders()+quantity);

                ItemOrder newItemOrder = new ItemOrder();
                newItemOrder.setOrder(newOrder);
                newItemOrder.setItem(item);
                newItemOrder.setQuantity(quantity);
                newItemOrder.setItemPrice(item.getPrice());
                newItemOrder.calculatePrice();
                itemOrderService.save(newItemOrder);

                Status activeStatus = statusService.findByName("active");
                StatusHistory statusHistory = new StatusHistory();
                statusHistory.setOrder(newOrder);
                statusHistory.setStatus(activeStatus);
                statusHistory.setTime(LocalDateTime.now());
                statusHistoryService.save(statusHistory);
            }
        } catch (NoSuchElementException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:" + currentUrl;
        }
        redirectAttributes.addFlashAttribute("message", "Item added to cart");
        return "redirect:" + currentUrl;
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request) {
        String jwtToken = jwtService.extractJwtTokenFromCookies(request);
        if (jwtToken == null || !jwtService.isTokenValid(jwtToken, jwtService.extractUsername(jwtToken))) {
            return "redirect:/auth/login";
        }

        String username = jwtService.extractUsername(jwtToken);
        Customer customer = customerService.getCustomerByEmail(username);
        model.addAttribute("customer", customer);
        Order activeOrder = orderService.getActiveOrder(customer);

        if (activeOrder != null) {
            List<ItemOrder> itemOrders = itemOrderService.getAllItemsByOrderId(activeOrder.getId());
            List<ItemOrderDTO> itemOrderDTOs = ItemOrderService.toDtoList(itemOrders);
            model.addAttribute("itemOrders", itemOrderDTOs);
            BigDecimal totalPrice = itemOrders.stream()
                    .map(itemOrder -> itemOrder.getItem().getPrice().multiply(BigDecimal.valueOf(itemOrder.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            model.addAttribute("totalPrice", totalPrice);
        }

        return "cart";
    }

    @PostMapping("/updateQuantity/{itemOrderId}")
    public String updateQuantity(@PathVariable Integer itemOrderId,
                                 @RequestParam int quantity,
                                 Model model,
                                 HttpServletRequest request,
                                 RedirectAttributes redirectAttributes) {
        String jwtToken = jwtService.extractJwtTokenFromCookies(request);
        if (jwtToken == null || !jwtService.isTokenValid(jwtToken, jwtService.extractUsername(jwtToken))) {
            return "redirect:/auth/login";
        }

        String username = jwtService.extractUsername(jwtToken);
        Customer customer = customerService.getCustomerByEmail(username);
        try {
        ItemOrder itemOrder = itemOrderService.getItemOrderById(itemOrderId);
            Item item = itemOrder.getItem();
            item.setNumOfOrders(item.getNumOfOrders()+(quantity-itemOrder.getQuantity()));
            itemOrder.setItem(item);
            itemOrder.setQuantity(quantity);
            itemOrder.calculatePrice();
            itemOrderService.save(itemOrder);
            Order tempOrder = itemOrder.getOrder();
            tempOrder.calculatePrice();
            orderService.save(tempOrder);
        } catch (NoSuchElementException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/removeItem/{itemOrderId}")
    public String removeItem(@PathVariable Integer itemOrderId,
                             Model model,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {
        String jwtToken = jwtService.extractJwtTokenFromCookies(request);
        if (jwtToken == null || !jwtService.isTokenValid(jwtToken, jwtService.extractUsername(jwtToken))) {
            return "redirect:/auth/login";
        }

        String username = jwtService.extractUsername(jwtToken);
        Customer customer = customerService.getCustomerByEmail(username);

        try {
            ItemOrder itemOrder = itemOrderService.getItemOrderById(itemOrderId);
            Item item = itemOrder.getItem();
            item.setNumOfOrders(item.getNumOfOrders() - itemOrder.getQuantity());
            itemService.save(item);
            Order tempOrder = itemOrder.getOrder();
            orderService.delete(tempOrder);
            itemOrderService.delete(itemOrder);
        }  catch(NoSuchElementException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String proceedToCheckout(@RequestParam String deliveryAddress,
                                    @RequestParam(required = false) String promoCode,
                                    Model model,
                                    HttpServletRequest request,
                                    RedirectAttributes redirectAttributes) {
        String jwtToken = jwtService.extractJwtTokenFromCookies(request);
        if (jwtToken == null || !jwtService.isTokenValid(jwtToken, jwtService.extractUsername(jwtToken))) {
            return "redirect:/auth/login";
        }
        String username = jwtService.extractUsername(jwtToken);
        Customer customer = customerService.getCustomerByEmail(username);
        PromoCode code = promoCodeService.getPromoCodeByCode(promoCode);
        Order order;
        if (code != null) {
            order = orderService.proceedeOrder(customer, deliveryAddress, code);
        } else {
            order = orderService.proceedeOrder(customer, deliveryAddress);
        }

        Status activeStatus = statusService.findByName("preparing");
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setOrder(order);
        statusHistory.setStatus(activeStatus);
        statusHistory.setTime(LocalDateTime.now());
        statusHistoryService.save(statusHistory);
        orderService.save(order);

        OrderStatusUpdater updater = new OrderStatusUpdater(order, statusService, statusHistoryService, orderService);
        new Thread(updater).start();

        redirectAttributes.addFlashAttribute("message", "Order placed successfully!");
        return "redirect:/mainPage";
    }
}
