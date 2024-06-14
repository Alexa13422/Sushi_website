package com.project.sushi_website.service;

import com.project.sushi_website.model.DTO.ItemOrderDTO;
import com.project.sushi_website.model.ItemOrder;
import com.project.sushi_website.repository.ItemOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemOrderService {

    private final ItemOrderRepository itemOrderRepository;

    public ItemOrderService(ItemOrderRepository itemOrderRepository) {
        this.itemOrderRepository = itemOrderRepository;
    }

    public void save(ItemOrder itemOrder) {
        itemOrderRepository.save(itemOrder);
    }

    public ItemOrder getItemOrderByOrderIdAndItemId(Integer orderId, Integer itemId) {
        return itemOrderRepository.findByOrderIdAndItemId(orderId, itemId);
    }
    public List<ItemOrder> getAllItemsByOrderId(Integer orderId) {
        return itemOrderRepository.findAllByOrderId(orderId);
    }
    public ItemOrder getItemOrderById(Integer itemOrder){
       return itemOrderRepository.findById(itemOrder)
               .orElseThrow(() -> new NoSuchElementException("ItemOrder not found with id: " + itemOrder));
    }
    public static ItemOrderDTO toDto(ItemOrder itemOrder) {
        ItemOrderDTO dto = new ItemOrderDTO();
        dto.setId(itemOrder.getId());
        dto.setItem(itemOrder.getItem());
        dto.setItemPrice(itemOrder.getItemPrice());
        dto.setOrder(itemOrder.getOrder());
        dto.setQuantity(itemOrder.getQuantity());
        dto.setPrice(itemOrder.getPrice());
        return dto;
    }

    public static List<ItemOrderDTO> toDtoList(List<ItemOrder> itemOrders) {
        return itemOrders.stream().map(ItemOrderService::toDto).collect(Collectors.toList());
    }

    public void delete(ItemOrder itemOrder) {
        itemOrderRepository.delete(itemOrder);
    }
}
