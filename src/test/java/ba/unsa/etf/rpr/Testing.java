package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.business.BookManager;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Testing {

    private BookManager bookManager = new BookManager();
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
    /*
    The add method should return the newly added book with all correct data including an
    auto-generated id. That id is used to search on it and check to see if the corresponding
    book is returned.
     */
    @Test
    public void testAddBookAndSearchById() throws LibraryException {
        Book addedBook = bookManager.add(testBook);
        Book foundBook = bookManager.searchById(addedBook.getId());
        assertEquals(addedBook, foundBook, "Book not found by id!");
        bookManager.delete(addedBook);
    }
    /*
    After the book is added and data about it is saved, it is deleted.
    The searchById method throws an exception if the search by id does
    not return any books.
     */
    @Test
    public void testDeleteBook() throws LibraryException {
        Book addedBook = bookManager.add(testBook);
        bookManager.delete(addedBook);
        LibraryException exception = assertThrows(
                LibraryException.class,
                () -> bookManager.searchById(addedBook.getId()),
                "Expected searchById to throw LibraryException, but it didn't"
        );
        assertEquals("Object not found", exception.getMessage(), "Unexpected exception message");
    }
    /*
    Since the id of the book has not been changed, the title of the
    corresponding book will be updated based on it.
     */
    @Test
    public void testUpdateBook() throws LibraryException {
        Book addedBook = bookManager.add(testBook);
        addedBook.setTitle("Updated book title");
        bookManager.update(addedBook);
        Book updatedBook = bookManager.searchById(addedBook.getId());
        assertEquals(updatedBook.getTitle(), "Updated book title", "Update failed!");
        bookManager.delete(addedBook);
    }
}
