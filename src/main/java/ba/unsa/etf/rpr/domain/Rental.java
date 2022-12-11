package ba.unsa.etf.rpr.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that contains basic information about borrowing a book from the library
 * It follows the POJO specification
 * @author Amina Bajric
 */
public class Rental {
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

    public int getRentalID() {
        return rentalID;
    }

    public void setRentalID(int rentalID) {
        this.rentalID = rentalID;
    }

    public Rental() {
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
        return "Date of renting: " + rent + System.lineSeparator()
                + "Deadline for return: " + returndl;
    }
}
