package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.business.BookManager;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

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
    public void testAddAndSearchById() throws LibraryException {
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
    public void testDelete() throws LibraryException {
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
    public void testUpdate() throws LibraryException {
        Book addedBook = bookManager.add(testBook);
        addedBook.setTitle("Updated book title");
        bookManager.update(addedBook);
        Book updatedBook = bookManager.searchById(addedBook.getId());
        assertEquals(updatedBook.getTitle(), "Updated book title", "Update failed!");
        bookManager.delete(addedBook);
    }
    /*
    We add the predefined book twice, so the sortByTitle
    method should return at least 2 rows and each of the returned books must
    have that corresponding title.
     */
    @Test
    public void testSearchByTitle() throws LibraryException {
        Book addedBook1 = bookManager.add(testBook);
        Book addedBook2 = bookManager.add(testBook);
        List<Book> foundBooks = bookManager.searchByTitle("Test Book");
        assertTrue(foundBooks.size() >= 2, "The list should contain 2 or more elements.");
        for(Book b : foundBooks) assertEquals(b.getTitle(), "Test Book", "Invalid title!");
        bookManager.delete(addedBook1);
        bookManager.delete(addedBook2);
    }
    @Test
    public void testSearchByAuthor() throws LibraryException {
        Book addedBook = bookManager.add(testBook);
        List<Book> foundBooks = bookManager.searchByAuthor("Test Author");
        assertTrue(foundBooks.contains(addedBook), "Book not found by author!");
        addedBook.setAuthor("Second test Author");
        bookManager.update(addedBook);
        List<Book> foundUpdatedBooks = bookManager.searchByAuthor("Second test Author");
        assertTrue(foundUpdatedBooks.contains(addedBook), "Updated book not found by author!");
        bookManager.delete(addedBook);
    }
}
