package com.bookmart.bookmart_backend.repository;

import com.bookmart.bookmart_backend.model.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setTitle("Clean Code");
        testBook.setAuthor("Robert C. Martin");
        testBook.setIsbn("978-0132350884");
        testBook.setPrice(BigDecimal.valueOf(39.99));
        testBook.setStockQuantity(50);
        testBook.setCategory("Programming");
        testBook.setDescription("A handbook of agile software craftsmanship");
        testBook.setImageUrl("https://example.com/clean-code.jpg");
    }

    @Test
    void save_ShouldPersistBook() {
        // Act
        Book savedBook = bookRepository.save(testBook);

        // Assert
        assertNotNull(savedBook.getId());
        assertEquals(testBook.getTitle(), savedBook.getTitle());
        assertEquals(testBook.getIsbn(), savedBook.getIsbn());
    }

    @Test
    void findById_WhenBookExists_ShouldReturnBook() {
        // Arrange
        Book savedBook = entityManager.persistAndFlush(testBook);

        // Act
        Optional<Book> found = bookRepository.findById(savedBook.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(savedBook.getTitle(), found.get().getTitle());
    }

    @Test
    void findById_WhenBookDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<Book> found = bookRepository.findById(UUID.randomUUID());

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllBooks() {
        // Arrange
        entityManager.persistAndFlush(testBook);

        Book anotherBook = new Book();
        anotherBook.setTitle("Design Patterns");
        anotherBook.setAuthor("Gang of Four");
        anotherBook.setIsbn("978-0201633610");
        anotherBook.setPrice(BigDecimal.valueOf(49.99));
        anotherBook.setStockQuantity(30);
        anotherBook.setCategory("Programming");
        entityManager.persistAndFlush(anotherBook);

        // Act
        List<Book> books = bookRepository.findAll();

        // Assert
        assertEquals(2, books.size());
    }

    @Test
    void existsByIsbn_WhenIsbnExists_ShouldReturnTrue() {
        // Arrange
        entityManager.persistAndFlush(testBook);

        // Act
        boolean exists = bookRepository.existsByIsbn(testBook.getIsbn());

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByIsbn_WhenIsbnDoesNotExist_ShouldReturnFalse() {
        // Act
        boolean exists = bookRepository.existsByIsbn("999-9999999999");

        // Assert
        assertFalse(exists);
    }

    @Test
    void searchBooks_ShouldFindBooksByKeyword() {
        // Arrange
        entityManager.persistAndFlush(testBook);

        // Act
        List<Book> results = bookRepository.serachBooks("Clean");

        // Assert
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(b -> b.getTitle().contains("Clean")));
    }

    @Test
    void deleteById_ShouldRemoveBook() {
        // Arrange
        Book savedBook = entityManager.persistAndFlush(testBook);
        UUID bookId = savedBook.getId();

        // Act
        bookRepository.deleteById(bookId);
        entityManager.flush();

        // Assert
        Optional<Book> found = bookRepository.findById(bookId);
        assertFalse(found.isPresent());
    }
}
