package ba.unsa.etf.rpr.domain;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Calendar;

/**
 * Class that contains basic information about borrowing a book from the library
 * It follows the POJO specification
 * @author Amina Bajric
 */
public class Rental implements Idable {
    private int rentalID;
    private int bookID;
    private int memberID;
    private Date rentDate;
    private Date returnDeadline;

    public Rental(int rentalID, int bookID, int memberID, Date rentDate, Date returnDeadline) {
        this.rentalID = rentalID;
        this.bookID = bookID;
        this.memberID = memberID;
        this.rentDate = rentDate;
        this.returnDeadline = returnDeadline;
    }

    public int getId() {
        return rentalID;
    }

    public void setId(int rentalID) {
        this.rentalID = rentalID;
    }

    public Rental() {
        this.rentDate = new java.sql.Date(System.currentTimeMillis());
        this.returnDeadline = new java.sql.Date(System.currentTimeMillis());
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public Date getRentDate() {
        return rentDate;
    }

    public void setRentDate(Date rentDate) {
        this.rentDate = rentDate;
    }

    public Date getReturnDeadline() {
        return returnDeadline;
    }

    public void setReturnDeadline(Date returnDeadline) {
        this.returnDeadline = returnDeadline;
    }
    /**
     * method for printing date of renting the book, and deadline for returning it to the library
     * form for date printing is "dd-mm-yyyy"
     * @return string that contains date of renting, and deadline for returning the book
     */
    @Override
    public String toString() {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String rent = simpleDateFormat.format(rentDate);
        String returndl = simpleDateFormat.format(returnDeadline);
        return "The book was rented on " + rent + " and should be returned until " + returndl;
    }
}
