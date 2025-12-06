package com.bookmart.bookmart_backend.controller;


import com.bookmart.bookmart_backend.model.dto.request.OrderRequest;
import com.bookmart.bookmart_backend.model.dto.response.OrderResponse;
import com.bookmart.bookmart_backend.model.enums.OrderStatus;
import com.bookmart.bookmart_backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Management", description = "Order placement and tracking APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class OrderController {
    private OrderService orderService;

    @PostMapping("/")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Place order", description = "Create a new order for books. Requires USER role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order data"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<OrderResponse> placeOrder(
            @Parameter(description = "User email") String userEmail,
            @RequestBody OrderRequest orderRequest ){
          OrderResponse orderResponse =orderService.placeOrder(userEmail,orderRequest);
          return ResponseEntity.ok(orderResponse);
    }
    @GetMapping("/my-orders")
    @Operation(summary = "Get user orders", description = "Retrieve all orders for the current user")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    public ResponseEntity<List<OrderResponse>>getUserOrders(
            @Parameter(description = "User email") @RequestBody String userEmail){
        List <OrderResponse> orderResponse =orderService.getUserOrders(userEmail);
        return ResponseEntity.ok(orderResponse);
    }
    @GetMapping("/orders")
    @Operation(summary = "Get all orders", description = "Retrieve all orders in the system. Admin access recommended.")
    @ApiResponse(responseCode = "200", description = "All orders retrieved successfully")
    public ResponseEntity<List<OrderResponse>>getAllOrders(){
        List <OrderResponse> orderResponse =orderService.getAllOrders();
        return ResponseEntity.ok(orderResponse);
    }
    @PatchMapping("/{orderId}/status")
    @Operation(summary = "Update order status", description = "Update the status of an existing order (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @Parameter(description = "Order UUID") @PathVariable UUID orderId,
            @Parameter(description = "New order status") @RequestBody OrderStatus status) {
        OrderResponse orderResponse =orderService.updateOrderStatus(orderId,status);
        return ResponseEntity.ok(orderResponse);
    }


}
