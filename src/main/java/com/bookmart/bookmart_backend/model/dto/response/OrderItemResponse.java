package com.bookmart.bookmart_backend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private UUID id;
    private UUID bookId;
    private String bookTitle;
    private String bookAuthor;
    private int quantity;
    private BigDecimal orderPrice;
    private BigDecimal subtotal;
}
