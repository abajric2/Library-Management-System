package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.business.BookManager;
import ba.unsa.etf.rpr.business.MemberManager;
import ba.unsa.etf.rpr.business.RentalManager;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.domain.Rental;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RentalTest {
    private RentalManager rentalManager;
    private Rental testRental;

    @BeforeEach
    public void setup() throws LibraryException {
        BookManager bookManager;
        bookManager = new BookManager();
        Book testBook;
        testBook = new Book();
        testBook.setId(1);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setYearOfPublication("2023");
        testBook.setGenre("Test Genre");
        testBook.setTotalNumber(100);
        testBook.setAvailableNumber(90);
        Book addedBook = bookManager.add(testBook);
        MemberManager memberManager;
        memberManager = new MemberManager();
        Member testMember;
        testMember = new Member();
        testMember.setId(1);
        testMember.setFirstName("Test First Name");
        testMember.setLastName("Test Last Name");
        testMember.setUsername("Test Username");
        testMember.setPassword("Test Password");
        testMember.setAdmin(false);
        /*
        In case there is a user with the username of the user
        we are trying to add, we will delete it because adding
        it would cause us an exception, and we don't want that here.
         */
        List<Member> allMembers = memberManager.getAll();
        rentalManager = new RentalManager();
        List<Rental> allRentals = rentalManager.getAll();
        for(Member member : allMembers) {
            if(member.getUsername().equals(testMember.getUsername())) {
                /*
                If there is a rental for a user we want to delete,
                we won't be able to delete it until we delete that rental.
                 */
                for(Rental rental : allRentals) {
                    if(rental.getMemberID() == member.getId()) rentalManager.delete(rental);
                }
                memberManager.delete(member);
                break;
            }
        }
        Member addedMember = memberManager.add(testMember);
        testRental = new Rental();
        testRental.setId(1);
        testRental.setBookID(addedBook.getId());
        testRental.setMemberID(addedMember.getId());
        LocalDate currentLocalDate = LocalDate.now();
        LocalDate threeMonthsLater = currentLocalDate.plusMonths(3);
        Date sqlCurrentDate = Date.valueOf(currentLocalDate);
        Date sqlDateThreeMonthsLater = Date.valueOf(threeMonthsLater);
        testRental.setRentDate(sqlCurrentDate);
        testRental.setReturnDeadline(sqlDateThreeMonthsLater);
    }
}
