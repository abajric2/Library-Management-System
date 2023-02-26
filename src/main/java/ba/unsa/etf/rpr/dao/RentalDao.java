package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.domain.Rental;
import ba.unsa.etf.rpr.exceptions.LibraryException;

import java.sql.Date;
import java.util.List;

/**
 * Interface that extends the generic interface "Dao"
 * Implements methods specific to working with book rentals
 * @author Amina Bajric
 */
public interface RentalDao extends Dao<Rental> {
    /**
     * returns a list of all rentals where the return deadline has been exceeded
     * @return list of rentals
     */
    List<Rental> getDeadlineExceedings () throws LibraryException;

    /**
     * returns a list of all rentals based on the specified return deadline date
     * (for example, to be able to notify members whose return deadline is about to expire)
     * @param returnDeadline date of retrun deadline
     * @return list of rentals
     */
    List<Rental> searchByReturnDeadline(Date returnDeadline) throws LibraryException;

    /**
     * checks whether the user with the id sent as a parameter already has a rental,
     * if so it returns that rental, if not it returns null
     * @param memberID id from library member
     * @return Rental
     */
    Rental checkUsersRental (int memberID) throws LibraryException;

    /**
     * returns the book that was borrowed to the library
     * @param memberID member from user who rented the book
     */
    void returnRentedBook (int memberID) throws LibraryException;

    /**
     * returns the library member who rented the book
     * @param r Rental
     * @return library member
     */
    Member getMember (Rental r) throws LibraryException;

    /**
     * returns the book that is rented
     * @param r Rental
     * @return Book
     */
    Book getBook (Rental r) throws LibraryException;

    /**
     * rents the book whose name and author are given by the parameter
     * to the user whose id is also given by the parameter,
     * unless he already has the book rented, in which case it returns null
     * @param memberID id from a library member
     * @param bookTitle title from a book that member wants to rent
     * @param author author of the book given by the second parameter
     * @return new rental or null
     */
    Rental rentABook (int memberID, String bookTitle, String author) throws LibraryException;
}
