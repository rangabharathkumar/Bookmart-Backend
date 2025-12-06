package com.bookmart.bookmart_backend.service.implement;

import com.bookmart.bookmart_backend.exception.InsufficientStockException;
import com.bookmart.bookmart_backend.exception.InvalidOrderException;
import com.bookmart.bookmart_backend.exception.ResourceNotFoundException;
import com.bookmart.bookmart_backend.model.dto.request.OrderRequest;
import com.bookmart.bookmart_backend.model.dto.response.OrderResponse;
import com.bookmart.bookmart_backend.model.entity.Book;
import com.bookmart.bookmart_backend.model.entity.Order;
import com.bookmart.bookmart_backend.model.entity.OrderItem;
import com.bookmart.bookmart_backend.model.entity.User;
import com.bookmart.bookmart_backend.model.enums.OrderStatus;
import com.bookmart.bookmart_backend.model.enums.UserRole;
import com.bookmart.bookmart_backend.repository.BookRepository;
import com.bookmart.bookmart_backend.repository.OrderRepository;
import com.bookmart.bookmart_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User testUser;
    private Book testBook;
    private Order testOrder;
    private OrderRequest testOrderRequest;
    private UUID userId;
    private UUID bookId;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        bookId = UUID.randomUUID();
        orderId = UUID.randomUUID();

        testUser = new User();
        testUser.setId(userId);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setRole(UserRole.USER);

        testBook = new Book();
        testBook.setId(bookId);
        testBook.setTitle("Clean Code");
        testBook.setAuthor("Robert C. Martin");
        testBook.setIsbn("978-0132350884");
        testBook.setPrice(BigDecimal.valueOf(39.99));
        testBook.setStockQuantity(50);

        testOrder = new Order();
        testOrder.setId(orderId);
        testOrder.setUser(testUser);
        testOrder.setOrderDate(LocalDateTime.now());
        testOrder.setStatus(OrderStatus.PENDING);
        testOrder.setTotalAmount(BigDecimal.valueOf(79.98));
        testOrder.setOrderItems(new ArrayList<>());

        OrderRequest.OrderItemRequest itemRequest = new OrderRequest.OrderItemRequest();
        itemRequest.setBookId(bookId);
        itemRequest.setQuantity(2);

        testOrderRequest = new OrderRequest();
        testOrderRequest.setItems(Arrays.asList(itemRequest));
    }

    @Test
    void placeOrder_WhenValidRequest_ShouldCreateOrder() {
        // Arrange
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        OrderResponse result = orderService.placeOrder(testUser.getEmail(), testOrderRequest);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(OrderStatus.PENDING, result.getStatus());
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void placeOrder_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> orderService.placeOrder(testUser.getEmail(), testOrderRequest));
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_WhenBookNotFound_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> orderService.placeOrder(testUser.getEmail(), testOrderRequest));
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(bookRepository, times(1)).findById(bookId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_WhenInsufficientStock_ShouldThrowInsufficientStockException() {
        // Arrange
        testBook.setStockQuantity(1); // Less than requested quantity (2)
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));

        // Act & Assert
        assertThrows(InsufficientStockException.class, 
            () -> orderService.placeOrder(testUser.getEmail(), testOrderRequest));
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(bookRepository, times(1)).findById(bookId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_WhenEmptyItems_ShouldThrowInvalidOrderException() {
        // Arrange
        OrderRequest emptyOrderRequest = new OrderRequest();
        emptyOrderRequest.setItems(new ArrayList<>());

        // Act & Assert
        assertThrows(InvalidOrderException.class, 
            () -> orderService.placeOrder(testUser.getEmail(), emptyOrderRequest));
        verify(userRepository, never()).findByEmail(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_WhenNullItems_ShouldThrowInvalidOrderException() {
        // Arrange
        OrderRequest nullItemsRequest = new OrderRequest();
        nullItemsRequest.setItems(null);

        // Act & Assert
        assertThrows(InvalidOrderException.class, 
            () -> orderService.placeOrder(testUser.getEmail(), nullItemsRequest));
        verify(userRepository, never()).findByEmail(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getUserOrders_ShouldReturnUserOrders() {
        // Arrange
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findAllByUser_Email(testUser.getEmail())).thenReturn(orders);

        // Act
        List<OrderResponse> result = orderService.getUserOrders(testUser.getEmail());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderId, result.get(0).getId());
        verify(orderRepository, times(1)).findAllByUser_Email(testUser.getEmail());
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders() {
        // Arrange
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findAll()).thenReturn(orders);

        // Act
        List<OrderResponse> result = orderService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderId, result.get(0).getId());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void updateOrderStatus_WhenOrderExists_ShouldUpdateStatus() {
        // Arrange
        OrderStatus newStatus = OrderStatus.COMPLETED;
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        OrderResponse result = orderService.updateOrderStatus(orderId, newStatus);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void updateOrderStatus_WhenOrderNotFound_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> orderService.updateOrderStatus(orderId, OrderStatus.COMPLETED));
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any());
    }
}
