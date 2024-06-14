package com.project.sushi_website.repository;

import com.project.sushi_website.model.Order;
import com.project.sushi_website.model.StatusHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
    List<Order> findByCustomerEmail(String customerEmail);

    @Query("SELECT sh FROM status_history sh WHERE sh.order = :order")
    List<StatusHistory> findAllStatusesByOrder(Order order);
}