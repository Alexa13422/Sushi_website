package com.project.sushi_website.repository;

import com.project.sushi_website.model.ItemOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemOrderRepository extends CrudRepository<ItemOrder, Integer> {
    @Query("SELECT io FROM ItemOrder io WHERE io.order.id = :orderId AND io.item.id = :itemId")
    ItemOrder findByOrderIdAndItemId(@Param("orderId") Integer orderId, @Param("itemId") Integer itemId);

    @Query("SELECT io FROM ItemOrder io WHERE io.order.id = :orderId")
    List<ItemOrder> findAllByOrderId(@Param("orderId") Integer orderId);
}