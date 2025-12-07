package com.bookmart.bookmart_backend.service.implement;

import com.bookmart.bookmart_backend.Mapper.OrderMapper;
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
import com.bookmart.bookmart_backend.repository.BookRepository;
import com.bookmart.bookmart_backend.repository.OrderRepository;
import com.bookmart.bookmart_backend.repository.UserRepository;
import com.bookmart.bookmart_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrderMapper orderMapper;
    
    @Override
    public OrderResponse placeOrder(String userEmail, OrderRequest orderRequest) {
        // Validate order request
        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            throw new InvalidOrderException("Order must contain at least one item");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderRequest.OrderItemRequest itemRequest : orderRequest.getItems()) {
            Book book = bookRepository.findById(itemRequest.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
            if (book.getStockQuantity() < itemRequest.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for book: " + book.getTitle() + ". Available: " + book.getStockQuantity() + ", Requested: " + itemRequest.getQuantity());
            }
            book.setStockQuantity(book.getStockQuantity() - itemRequest.getQuantity());
            bookRepository.save(book);
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setOrderPrice(book.getPrice());

            orderItems.add(orderItem);

            BigDecimal itemTotal = book.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            order.setTotalAmount(order.getTotalAmount().add(itemTotal));

        }
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }
    private OrderResponse mapToResponse(Order order) {
        return orderMapper.toOrderResponse(order);
    }
    @Override
    public List<OrderResponse> getUserOrders(String userEmail) {
        List<Order> orders=orderRepository.findAllByUser_Email(userEmail);
        return orders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getAllOrders() {
            List<Order> orders = orderRepository.findAll();
            return orders.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }


    @Override
    public OrderResponse updateOrderStatus(UUID orderId, OrderStatus status) {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

            order.setStatus(status);
            Order updatedOrder = orderRepository.save(order);

            return mapToResponse(updatedOrder);
        }
}
