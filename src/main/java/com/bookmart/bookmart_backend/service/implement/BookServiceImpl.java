package com.bookmart.bookmart_backend.service.implement;

import com.bookmart.bookmart_backend.Mapper.BookMapper;
import com.bookmart.bookmart_backend.exception.DuplicateResourceException;
import com.bookmart.bookmart_backend.exception.ResourceNotFoundException;
import com.bookmart.bookmart_backend.model.dto.request.BookRequest;
import com.bookmart.bookmart_backend.model.dto.response.BookResponse;
import com.bookmart.bookmart_backend.model.entity.Book;
import com.bookmart.bookmart_backend.repository.BookRepository;
import com.bookmart.bookmart_backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public List<BookResponse> getAllBooks() {
        List<Book> allBooks=bookRepository.findAll();
        return allBooks.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponse getBookById(UUID id) {
        Book book=bookRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Book not found"));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookResponse> searchBooks(String keyword) {
        List<Book> allBooks=bookRepository.serachBooks(keyword);
        return allBooks.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public String addBook(BookRequest bookRequest) {
        if (bookRepository.existsByIsbn(bookRequest.getIsbn())) {
            throw new DuplicateResourceException("Book with ISBN " + bookRequest.getIsbn() + " already exists");
        }
        Book book=new Book();
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setDescription(bookRequest.getDescription());
        book.setPrice(bookRequest.getPrice());
        book.setIsbn(bookRequest.getIsbn());
        book.setStockQuantity(bookRequest.getStockQuantity());
        book.setCategory(bookRequest.getCategory());
        book.setImageUrl(bookRequest.getImageUrl());
        bookRepository.save(book);
        return "Book added successfully!";
    }

    @Override
    public String updateBook(UUID id, BookRequest bookRequest) {
        Optional<Book> existingBookOptional = bookRepository.findById(id);
        if(bookRepository.existsById(id)){
            Book existingBook=existingBookOptional.get();
            existingBook.setTitle(bookRequest.getTitle());
            existingBook.setAuthor(bookRequest.getAuthor());
            existingBook.setDescription(bookRequest.getDescription());
            existingBook.setCategory(bookRequest.getCategory());
            existingBook.setIsbn(bookRequest.getIsbn());
            existingBook.setPrice(bookRequest.getPrice());
            existingBook.setImageUrl(bookRequest.getImageUrl());
            existingBook.setStockQuantity(bookRequest.getStockQuantity());
            bookRepository.save(existingBook);
            return "Book updated successfully!";
        }
        else {
            // Handle case where book with given ID is not found (e.g., throw an exception)
            throw new ResourceNotFoundException("Book with ID " + id + " not found.");
        }
    }

    @Override
    public String deleteBook(UUID id) {
        bookRepository.deleteById(id);
        return "Book deleted successfully!";
    }

}
