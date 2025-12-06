package com.bookmart.bookmart_backend.service.implement;

import com.bookmart.bookmart_backend.Mapper.BookMapper;
import com.bookmart.bookmart_backend.exception.DuplicateResourceException;
import com.bookmart.bookmart_backend.exception.ResourceNotFoundException;
import com.bookmart.bookmart_backend.model.dto.request.BookRequest;
import com.bookmart.bookmart_backend.model.dto.response.BookResponse;
import com.bookmart.bookmart_backend.model.entity.Book;
import com.bookmart.bookmart_backend.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook;
    private BookRequest testBookRequest;
    private BookResponse testBookResponse;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        
        testBook = new Book();
        testBook.setId(testId);
        testBook.setTitle("Clean Code");
        testBook.setAuthor("Robert C. Martin");
        testBook.setIsbn("978-0132350884");
        testBook.setPrice(BigDecimal.valueOf(39.99));
        testBook.setStockQuantity(50);
        testBook.setCategory("Programming");
        testBook.setDescription("A handbook of agile software craftsmanship");
        testBook.setImageUrl("https://example.com/clean-code.jpg");

        testBookRequest = new BookRequest();
        testBookRequest.setTitle("Clean Code");
        testBookRequest.setAuthor("Robert C. Martin");
        testBookRequest.setIsbn("978-0132350884");
        testBookRequest.setPrice(BigDecimal.valueOf(39.99));
        testBookRequest.setStockQuantity(50);
        testBookRequest.setCategory("Programming");
        testBookRequest.setDescription("A handbook of agile software craftsmanship");
        testBookRequest.setImageUrl("https://example.com/clean-code.jpg");

        testBookResponse = new BookResponse();
        testBookResponse.setId(testId);
        testBookResponse.setTitle("Clean Code");
        testBookResponse.setAuthor("Robert C. Martin");
        testBookResponse.setIsbn("978-0132350884");
        testBookResponse.setPrice(BigDecimal.valueOf(39.99));
        testBookResponse.setStockQuantity(50);
        testBookResponse.setCategory("Programming");
        testBookResponse.setDescription("A handbook of agile software craftsmanship");
        testBookResponse.setImageUrl("https://example.com/clean-code.jpg");
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() {
        // Arrange
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toDto(testBook)).thenReturn(testBookResponse);

        // Act
        List<BookResponse> result = bookService.getAllBooks();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testBookResponse.getTitle(), result.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(1)).toDto(testBook);
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() {
        // Arrange
        when(bookRepository.findById(testId)).thenReturn(Optional.of(testBook));
        when(bookMapper.toDto(testBook)).thenReturn(testBookResponse);

        // Act
        BookResponse result = bookService.getBookById(testId);

        // Assert
        assertNotNull(result);
        assertEquals(testBookResponse.getTitle(), result.getTitle());
        verify(bookRepository, times(1)).findById(testId);
        verify(bookMapper, times(1)).toDto(testBook);
    }

    @Test
    void getBookById_WhenBookDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(bookRepository.findById(testId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(testId));
        verify(bookRepository, times(1)).findById(testId);
        verify(bookMapper, never()).toDto(any());
    }

    @Test
    void addBook_WhenIsbnDoesNotExist_ShouldAddBook() {
        // Arrange
        when(bookRepository.existsByIsbn(testBookRequest.getIsbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // Act
        String result = bookService.addBook(testBookRequest);

        // Assert
        assertEquals("Book added successfully!", result);
        verify(bookRepository, times(1)).existsByIsbn(testBookRequest.getIsbn());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void addBook_WhenIsbnExists_ShouldThrowDuplicateResourceException() {
        // Arrange
        when(bookRepository.existsByIsbn(testBookRequest.getIsbn())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> bookService.addBook(testBookRequest));
        verify(bookRepository, times(1)).existsByIsbn(testBookRequest.getIsbn());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_WhenBookExists_ShouldUpdateBook() {
        // Arrange
        when(bookRepository.existsById(testId)).thenReturn(true);
        when(bookRepository.findById(testId)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // Act
        String result = bookService.updateBook(testId, testBookRequest);

        // Assert
        assertEquals("Book updated successfully!", result);
        verify(bookRepository, times(1)).existsById(testId);
        verify(bookRepository, times(1)).findById(testId);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_WhenBookDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(bookRepository.existsById(testId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(testId, testBookRequest));
        verify(bookRepository, times(1)).existsById(testId);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void deleteBook_ShouldDeleteBook() {
        // Arrange
        doNothing().when(bookRepository).deleteById(testId);

        // Act
        String result = bookService.deleteBook(testId);

        // Assert
        assertEquals("Book deleted successfully!", result);
        verify(bookRepository, times(1)).deleteById(testId);
    }

    @Test
    void searchBooks_ShouldReturnFilteredBooks() {
        // Arrange
        String keyword = "Clean";
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.serachBooks(keyword)).thenReturn(books);
        when(bookMapper.toDto(testBook)).thenReturn(testBookResponse);

        // Act
        List<BookResponse> result = bookService.searchBooks(keyword);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testBookResponse.getTitle(), result.get(0).getTitle());
        verify(bookRepository, times(1)).serachBooks(keyword);
        verify(bookMapper, times(1)).toDto(testBook);
    }
}
