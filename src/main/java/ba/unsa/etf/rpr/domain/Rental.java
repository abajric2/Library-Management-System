package ba.unsa.etf.rpr.domain;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Calendar;
import java.util.Objects;

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

    public Rental(int bookID, int memberID, Date rentDate, Date returnDeadline) {
        this.rentalID = 0;
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rental rental = (Rental) o;
        /*
        If I just use the equals method to compare dates, even if the day, month,
        and year are the same, the dates will be treated as different because of the
        time component. Therefore, it is necessary to compare day, month and year separately
        and treat dates as equal if these components are equal.
         */
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(this.rentDate);
        cal2.setTime(rental.rentDate);
        boolean sameRentDate = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
        cal1.setTime(this.returnDeadline);
        cal2.setTime(rental.returnDeadline);
        boolean sameReturnDeadline = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
        return rentalID == rental.rentalID &&
                bookID == rental.bookID &&
                memberID == rental.memberID &&
                sameRentDate &&
                sameReturnDeadline;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rentalID, bookID, memberID, rentDate, returnDeadline);
    }
}
