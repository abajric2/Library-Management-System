package ba.unsa.etf.rpr.business;

import ba.unsa.etf.rpr.dao.BookDaoSQLImpl;
import ba.unsa.etf.rpr.dao.DaoFactory;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.domain.Rental;
import ba.unsa.etf.rpr.exceptions.LibraryException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RentalDao {
    public List<Rental> getAll() throws LibraryException {
        return DaoFactory.rentalDao().getAll();
    }
    public void viewAll() throws LibraryException {
        DaoFactory.rentalDao().viewAll();
    }
    public Rental add(Rental item) throws LibraryException {
        return DaoFactory.rentalDao().add(item);
    }

    public Rental update(Rental item) throws LibraryException {
        return DaoFactory.rentalDao().update(item);
    }

    public Rental checkUsersRental (int memberID) throws LibraryException {
        return DaoFactory.rentalDao().checkUsersRental(memberID);
    }

    public void returnRentedBook (int memberID) throws LibraryException {
        DaoFactory.rentalDao().returnRentedBook(memberID);
    }

 /*   @Override
    public Rental getById(int id) throws LibraryException {
        return executeQueryUnique("SELECT * FROM RENTALS WHERE RENTAL_ID = ?", new Object[]{id});
    }*/

    public void delete(Rental item) throws LibraryException {
        DaoFactory.rentalDao().delete(item);
    }

    public Rental searchById(int id) throws LibraryException {
        return DaoFactory.rentalDao().searchById(id);
    }

    public Member getMember(Rental r) throws LibraryException {
        return DaoFactory.rentalDao().getMember(r);
    }

    public Book getBook (Rental r) throws LibraryException {
        return DaoFactory.rentalDao().getBook(r);
    }

    public Rental rentABook(int memberID, String bookTitle, String author) throws LibraryException {
        return DaoFactory.rentalDao().rentABook(memberID, bookTitle, author);
    }

    public List<Rental> getDeadlineExceedings() throws LibraryException {
        return DaoFactory.rentalDao().getDeadlineExceedings();
    }

    public List<Rental> searchByReturnDeadline(java.sql.Date returnDeadline) throws LibraryException {
        return DaoFactory.rentalDao().searchByReturnDeadline(returnDeadline);
    }
}