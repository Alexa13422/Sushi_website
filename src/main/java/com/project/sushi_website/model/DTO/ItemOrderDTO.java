package com.project.sushi_website.model.DTO;

import com.project.sushi_website.model.Item;
import com.project.sushi_website.model.Order;
import jakarta.persistence.*;

import java.math.BigDecimal;

public class ItemOrderDTO {
    private Integer id;
    private Order order;
    private Item item;
    private int quantity;
    private BigDecimal itemPrice;
    private BigDecimal price;
    public BigDecimal getItemPrice() {
        return itemPrice;
    }
    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
