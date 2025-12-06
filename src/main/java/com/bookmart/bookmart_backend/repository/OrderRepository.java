package com.bookmart.bookmart_backend.repository;

import com.bookmart.bookmart_backend.model.entity.Order;
import com.bookmart.bookmart_backend.model.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
     List<Order> findAllByUser_Email(String email);


}
