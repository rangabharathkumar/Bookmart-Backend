package com.bookmart.bookmart_backend.service;

import com.bookmart.bookmart_backend.model.dto.request.BookRequest;
import com.bookmart.bookmart_backend.model.dto.response.BookResponse;

import java.util.List;
import java.util.UUID;

public interface BookService {
    List<BookResponse> getAllBooks();

    BookResponse getBookById(UUID id);

    List<BookResponse> searchBooks(String keyword);

    String addBook(BookRequest bookRequest);

    String updateBook(UUID id, BookRequest bookRequest);

    String deleteBook(UUID id);
}
