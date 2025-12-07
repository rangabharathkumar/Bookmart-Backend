package com.bookmart.bookmart_backend.repository;

import com.bookmart.bookmart_backend.model.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    
    @EntityGraph(attributePaths = {"orderItems", "orderItems.book", "user"})
    List<Order> findAllByUser_Email(String email);
    
    @EntityGraph(attributePaths = {"orderItems", "orderItems.book", "user"})
    List<Order> findAll();
}
