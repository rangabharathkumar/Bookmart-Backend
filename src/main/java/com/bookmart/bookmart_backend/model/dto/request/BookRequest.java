package com.bookmart.bookmart_backend.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    private String title;
    private String author;
    private String description;
    private BigDecimal price;
    private String isbn;
    private int stockQuantity;
    private String category;
    private String imageUrl;

}
