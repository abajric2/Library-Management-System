package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Rental;

import java.util.Date;
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
    List<Rental> deadlineExceedings ();

    /**
     * returns a list of all rentals based on the specified return deadline date
     * (for example, to be able to notify members whose return deadline is about to expire)
     * @param returnDeadline date of retrun deadline
     * @return list of rentals
     */
    List<Rental> searchByReturnDeadline(Date returnDeadline);
}
