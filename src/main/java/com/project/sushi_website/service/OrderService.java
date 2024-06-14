package com.project.sushi_website.service;

import com.project.sushi_website.model.Customer;
import com.project.sushi_website.model.Order;
import com.project.sushi_website.model.PromoCode;
import com.project.sushi_website.model.StatusHistory;
import com.project.sushi_website.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrdersByCustomerEmail(String customerEmail) {
        return orderRepository.findByCustomerEmail(customerEmail);
    }
    public boolean isOrderActive(Order order){
       List<StatusHistory> statuses = orderRepository.findAllStatusesByOrder(order);
        statuses.sort(Comparator.comparing(StatusHistory::getTime).reversed());
        return statuses.get(0).getStatus().getStatus().equals("active");

    }
    public Order getActiveOrder(Customer customer) {
        List<Order> orders = getOrdersByCustomerEmail(customer.getUsername());
        for (Order order : orders) {
            if (isOrderActive(order)) {
                return order;
            }
        }
        return null;
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void delete(Order order){
        orderRepository.delete(order);
    }

    public Order proceedeOrder(Customer customer, String deliveryAddress, PromoCode... promoCode) {
        Order activeOrder = getActiveOrder(customer);
        activeOrder.setDeliveryAddress(deliveryAddress);
        activeOrder.setOrderTime(LocalDateTime.now());
        activeOrder.setEstimatedDeliveryTime(LocalDateTime.now().plusHours(1));
        if (promoCode.length != 0){
            activeOrder.setPromoCode(promoCode[0]);
            activeOrder.setPrice(activeOrder.getPrice().multiply(BigDecimal.valueOf(promoCode[0].getPercentage() / 100)));
        }
        activeOrder.setDeliveryPrice(BigDecimal.valueOf(9.50));
        activeOrder.setFinalPrice(activeOrder.getPrice().add(activeOrder.getDeliveryPrice()));
        return activeOrder;

    }
}