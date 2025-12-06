package com.bookmart.bookmart_backend.model.entity;

import com.bookmart.bookmart_backend.model.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private OrderStatus status;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private ArrayList<OrderItem> orderItems = new ArrayList<>();


}

//id
//        (Long, PK)
//user (ManyToOne relationship with
//        User
//)
//orderDate (LocalDateTime)
//totalAmount (BigDecimal)
//status (Enum: OrderStatus)
//orderItems (OneToMany relationship with OrderItem, mappedBy="order", CascadeType.ALL)