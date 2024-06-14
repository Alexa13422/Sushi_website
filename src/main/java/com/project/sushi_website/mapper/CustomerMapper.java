package com.project.sushi_website.mapper;

import com.project.sushi_website.model.Customer;
import com.project.sushi_website.model.DTO.CustomerDTO;
import com.project.sushi_website.model.Order;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerMapper {

    public static CustomerDTO toCustomerDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setUsername(customer.getEmail());
        dto.setEmail(customer.getUsername());
        dto.setCity(customer.getCity());
        dto.setAddress(customer.getAddress());
        dto.setPassword(customer.getPassword());
        dto.setAdmin(customer.isAdmin());
        dto.setOrders(customer.getOrders().stream()
                .map(CustomerMapper::toOrderDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private static Order toOrderDTO(Order order) {
        Order dto = new Order();
        dto.setId(order.getId());
        dto.setOrderTime(order.getOrderTime());
        dto.setPrice(order.getFinalPrice());
        return dto;
    }
}

