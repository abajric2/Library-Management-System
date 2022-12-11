package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Book;

import java.util.List;

/**
 * Interface that extends the generic interface "Dao"
 * Implements methods specific to working with books in the library
 * @author Amina Bajric
 */
public interface BookDao extends Dao<Book> {
    /**
     * method that returns a list of all books in the library by the desired author
     * @param author name of the author
     * @return list of books
     */
    List<Book> searchByAuthor (String author);

    /**
     * method that returns a list of all books in the library of the desired genre
     * @param genre
     * @return list of books
     */
    List<Book> searchByGenre (String genre);

    /**
     * method that returns a list of all books in the library with the desired title
     * @param title
     * @return list of books
     */
    List<Book> searchByTitle(String title);

    /**
     * method that returns true if a book with a given title
     * and by a given author is currently available in the library,
     * and false otherwise
     * @param title
     * @param author
     */
    boolean isAvailable (String title, String author);
}