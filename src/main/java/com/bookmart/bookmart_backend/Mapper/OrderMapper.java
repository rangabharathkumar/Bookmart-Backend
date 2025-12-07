package com.bookmart.bookmart_backend.Mapper;

import com.bookmart.bookmart_backend.model.dto.response.OrderItemResponse;
import com.bookmart.bookmart_backend.model.dto.response.OrderResponse;
import com.bookmart.bookmart_backend.model.entity.Order;
import com.bookmart.bookmart_backend.model.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponse toOrderResponse(Order order) {
        if (order == null) {
            return null;
        }
        
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderDate(order.getOrderDate());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setItems(toOrderItemResponseList(order.getOrderItems()));
        
        return response;
    }

    public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        
        OrderItemResponse response = new OrderItemResponse();
        response.setId(orderItem.getId());
        response.setBookId(orderItem.getBook().getId());
        response.setBookTitle(orderItem.getBook().getTitle());
        response.setBookAuthor(orderItem.getBook().getAuthor());
        response.setQuantity(orderItem.getQuantity());
        response.setOrderPrice(orderItem.getOrderPrice());
        response.setSubtotal(calculateSubtotal(orderItem));
        
        return response;
    }

    private BigDecimal calculateSubtotal(OrderItem orderItem) {
        if (orderItem.getOrderPrice() == null) {
            return BigDecimal.ZERO;
        }
        return orderItem.getOrderPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
    }

    private List<OrderItemResponse> toOrderItemResponseList(List<OrderItem> orderItems) {
        if (orderItems == null) {
            return null;
        }
        return orderItems.stream()
                .map(this::toOrderItemResponse)
                .collect(Collectors.toList());
    }
}
