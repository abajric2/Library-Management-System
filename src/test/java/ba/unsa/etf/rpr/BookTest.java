package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.business.BookManager;
import ba.unsa.etf.rpr.business.RentalManager;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Rental;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

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
    /*
    The test checks the methods that perform the basic operations
    of interacting with the database. As part of that, the search
    by id is also being tested.
     */
    @Test
    public void testCRUDAndSearchById() throws LibraryException {
        // testing add
        Book addedBook = bookManager.add(testBook);
        Book foundBook = bookManager.searchById(addedBook.getId());
        assertEquals(addedBook, foundBook, "Book not found by id!");

        /*
        TESTING UPDATE
        Since the id of the book has not been changed, the title of the
        corresponding book will be updated based on it.
         */
        addedBook.setTitle("Updated book title");
        bookManager.update(addedBook);
        Book updatedBook = bookManager.searchById(addedBook.getId());
        assertEquals(updatedBook.getTitle(), "Updated book title", "Update failed!");

        // Invalid data case
        addedBook.setYearOfPublication("ab");
        LibraryException exceptionInvalidYear = assertThrows(
                LibraryException.class,
                () -> bookManager.update(addedBook),
                "Expected update to throw LibraryException, but it didn't"
        );
        assertEquals("The year of publication can only contain numbers!", exceptionInvalidYear.getMessage(), "Unexpected exception message");
        addedBook.setYearOfPublication("2023");

        /*
        TESTING DELETE
        After the book is added and data about it is saved, it is deleted.
        The searchById method throws an exception if the search by id does
        not return any books.
         */
        bookManager.delete(addedBook);
        LibraryException exception = assertThrows(
                LibraryException.class,
                () -> bookManager.searchById(addedBook.getId()),
                "Expected searchById to throw LibraryException, but it didn't"
        );
        assertEquals("Object not found", exception.getMessage(), "Unexpected exception message");
    }
    /*
    We add the predefined book twice, so the sortByTitle
    method should return at least 2 rows and each of the returned books must
    have that corresponding title.
     */
    @Test
    public void testSearchByTitle() throws LibraryException {
        Book addedBook = bookManager.add(testBook);
        List<Book> foundBooks = bookManager.searchByTitle("Test Book");
        assertTrue(foundBooks.contains(addedBook), "The list should contain recently added book!");
        for(Book b : foundBooks) assertEquals(b.getTitle(), "Test Book", "Invalid title!");
        bookManager.delete(addedBook);
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
    /*
    We first perform a trivial search by genre after adding a book that has
    exactly the genre we are looking for. Then we delete all the books that
    are of a certain genre, after which the search by that genre should
    return an empty list.
     */
    @Test
    public void testSearchByGenre() throws LibraryException {
        Book addedBook = bookManager.add(testBook);
        List<Book> foundBooks = bookManager.searchByGenre("Test Genre");
        assertTrue(foundBooks.contains(addedBook), "Book not found by genre!");
        List<Book> allBooks = bookManager.getAll();
        RentalManager rentalManager = new RentalManager();
        List<Rental> allRentals = rentalManager.getAll();
        for(Book book : allBooks) {
            if(book.getGenre().equals("Test Genre")) {
                /*
                Deleting a book that is currently rented will throw an exception.
                Therefore, it is necessary to first delete such a rental if it exists,
                and then delete the book. We are doing this because a book with this
                genre could have been added unrelated to the tests.
                 */
                for(Rental rental : allRentals) {
                    if(rental.getBookID() == book.getId()) rentalManager.delete(rental);
                }
                bookManager.delete(book);
            }
        }
        List<Book> foundBooksAfterDeletion = bookManager.searchByGenre("Test Genre");
        assertTrue(foundBooksAfterDeletion.isEmpty(), "List should be empty!");
    }
}
