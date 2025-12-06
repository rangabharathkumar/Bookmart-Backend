package com.bookmart.bookmart_backend.service;

import com.bookmart.bookmart_backend.model.dto.request.OrderRequest;
import com.bookmart.bookmart_backend.model.dto.response.OrderResponse;
import com.bookmart.bookmart_backend.model.enums.OrderStatus;

import java.util.List;
import java.util.UUID;


public interface OrderService {

    public OrderResponse  placeOrder(String userEmail, OrderRequest orderRequest);
    public List<OrderResponse> getUserOrders(String userEmail);
    public List<OrderResponse> getAllOrders();
    public OrderResponse updateOrderStatus(UUID orderId, OrderStatus status);
}
