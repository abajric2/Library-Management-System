package ba.unsa.etf.rpr.dao;

/**
 * Exception for the case when the user has no rents
 * Extended from RuntimeException
 * @author Amina Bajric
 */
public class NoRentalException extends RuntimeException {
    public  NoRentalException (String e) {
        super(e);
    }
}
