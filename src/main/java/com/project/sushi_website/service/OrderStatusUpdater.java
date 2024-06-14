package com.project.sushi_website.service;

import com.project.sushi_website.model.Order;
import com.project.sushi_website.model.Status;
import com.project.sushi_website.model.StatusHistory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class OrderStatusUpdater implements Runnable {

    private final Order order;
    private final StatusService statusService;
    private final StatusHistoryService statusHistoryService;
    private final OrderService orderService;

    public OrderStatusUpdater(Order order, StatusService statusService, StatusHistoryService statusHistoryService, OrderService orderService) {
        this.order = order;
        this.statusService = statusService;
        this.statusHistoryService = statusHistoryService;
        this.orderService = orderService;
    }

    @Override
    public void run() {
        try {
            updateOrderStatusPeriodically();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateOrderStatusPeriodically() throws InterruptedException {
        String[] statuses = {"ready", "delivered"};
        for (String statusName : statuses) {
            Thread.sleep(30000); // Sleep for 1 second
            Status status = statusService.findByName(statusName);
            StatusHistory statusHistory = new StatusHistory();
            statusHistory.setOrder(order);
            statusHistory.setStatus(status);
            statusHistory.setTime(LocalDateTime.now());
            if (statusName.equals("delivered")){
                order.setActualDeliveryTime(LocalDateTime.now());
            }
            statusHistoryService.save(statusHistory);
            orderService.save(order);
        }
    }
}
