package com.bookmart.bookmart_backend.repository;

import com.bookmart.bookmart_backend.model.entity.Order;
import com.bookmart.bookmart_backend.model.entity.User;
import com.bookmart.bookmart_backend.model.enums.OrderStatus;
import com.bookmart.bookmart_backend.model.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private User testUser;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setPassword("encodedPassword");
        testUser.setRole(UserRole.USER);
        testUser.setEnabled(true);
        testUser = entityManager.persistAndFlush(testUser);

        testOrder = new Order();
        testOrder.setUser(testUser);
        testOrder.setOrderDate(LocalDateTime.now());
        testOrder.setStatus(OrderStatus.PENDING);
        testOrder.setTotalAmount(BigDecimal.valueOf(99.99));
        testOrder.setOrderItems(new ArrayList<>());
    }

    @Test
    void save_ShouldPersistOrder() {
        // Act
        Order savedOrder = orderRepository.save(testOrder);

        // Assert
        assertNotNull(savedOrder.getId());
        assertEquals(testOrder.getTotalAmount(), savedOrder.getTotalAmount());
        assertEquals(testOrder.getStatus(), savedOrder.getStatus());
    }

    @Test
    void findById_WhenOrderExists_ShouldReturnOrder() {
        // Arrange
        Order savedOrder = entityManager.persistAndFlush(testOrder);

        // Act
        Optional<Order> found = orderRepository.findById(savedOrder.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(savedOrder.getTotalAmount(), found.get().getTotalAmount());
    }

    @Test
    void findById_WhenOrderDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<Order> found = orderRepository.findById(UUID.randomUUID());

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllOrders() {
        // Arrange
        entityManager.persistAndFlush(testOrder);

        Order anotherOrder = new Order();
        anotherOrder.setUser(testUser);
        anotherOrder.setOrderDate(LocalDateTime.now());
        anotherOrder.setStatus(OrderStatus.COMPLETED);
        anotherOrder.setTotalAmount(BigDecimal.valueOf(149.99));
        anotherOrder.setOrderItems(new ArrayList<>());
        entityManager.persistAndFlush(anotherOrder);

        // Act
        List<Order> orders = orderRepository.findAll();

        // Assert
        assertTrue(orders.size() >= 2);
    }

    @Test
    void findAllByUser_Email_ShouldReturnUserOrders() {
        // Arrange
        entityManager.persistAndFlush(testOrder);

        // Create another user and order
        User anotherUser = new User();
        anotherUser.setEmail("another@example.com");
        anotherUser.setName("Another User");
        anotherUser.setPassword("password");
        anotherUser.setRole(UserRole.USER);
        anotherUser.setEnabled(true);
        anotherUser = entityManager.persistAndFlush(anotherUser);

        Order anotherOrder = new Order();
        anotherOrder.setUser(anotherUser);
        anotherOrder.setOrderDate(LocalDateTime.now());
        anotherOrder.setStatus(OrderStatus.PENDING);
        anotherOrder.setTotalAmount(BigDecimal.valueOf(199.99));
        anotherOrder.setOrderItems(new ArrayList<>());
        entityManager.persistAndFlush(anotherOrder);

        // Act
        List<Order> userOrders = orderRepository.findAllByUser_Email(testUser.getEmail());

        // Assert
        assertFalse(userOrders.isEmpty());
        assertTrue(userOrders.stream().allMatch(o -> o.getUser().getEmail().equals(testUser.getEmail())));
    }

    @Test
    void findAllByUser_Email_WhenNoOrders_ShouldReturnEmptyList() {
        // Act
        List<Order> orders = orderRepository.findAllByUser_Email("nonexistent@example.com");

        // Assert
        assertTrue(orders.isEmpty());
    }
}
