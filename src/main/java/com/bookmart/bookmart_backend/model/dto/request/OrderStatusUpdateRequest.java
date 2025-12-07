package com.bookmart.bookmart_backend.model.dto.request;

import com.bookmart.bookmart_backend.model.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequest {
    @NotNull(message = "Order status cannot be null")
    private OrderStatus status;
}
