package com.project.sushi_website.repository;

import com.project.sushi_website.model.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {
    @Query("SELECT io.item FROM ItemOrder io WHERE io.order.id = :orderId")
    List<Item> findItemsByOrderId(@Param("orderId") Integer orderId);
}
