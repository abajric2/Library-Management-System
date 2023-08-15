package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.business.BookManager;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Testing {

    private BookManager bookManager;
    private Book testBook;

    @BeforeEach
    public void setup() {
        bookManager = new BookManager();
        testBook = new Book();
        testBook.setId(1);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setYearOfPublication("2023");
        testBook.setGenre("Test Genre");
        testBook.setTotalNumber(100);
        testBook.setAvailableNumber(90);
    }

    @Test
    public void testAddAndSearchById() throws LibraryException {
        Book addedBook = bookManager.add(testBook);
        Book foundBook = bookManager.searchById(addedBook.getId());
        assertEquals(addedBook, foundBook, "Book not found by id!");
        bookManager.delete(addedBook);
    }
}
