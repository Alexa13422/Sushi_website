package com.project.sushi_website.service;

import com.project.sushi_website.model.DTO.ItemDTO;
import com.project.sushi_website.model.Item;
import com.project.sushi_website.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<ItemDTO> getAllItems() {
        List<Item> items = new ArrayList<>();
        itemRepository.findAll().forEach(items::add);
        return items.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ItemDTO> get4PopularItems() {
        List<Item> items = new ArrayList<>();
        itemRepository.findAll().forEach(items::add);
        return items.stream()
                .sorted((item1, item2) -> item2.getNumOfOrders().compareTo(item1.getNumOfOrders()))
                .limit(4)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public Item getItemById(Integer id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Item not found with id: " + id));
    }
    public List<Item> getItemsByOrderId(Integer orderId) {
        return itemRepository.findItemsByOrderId(orderId);
    }

    public Item save(Item menuItem) {
        return itemRepository.save(menuItem);
    }

    public void deleteItem(Integer id) {
        itemRepository.deleteById(id);
    }

    public ItemDTO convertToDTO(Item menuItem) {
        ItemDTO dto = new ItemDTO();
        dto.setId(menuItem.getId());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setPrice(menuItem.getPrice());
        return dto;
    }

}
