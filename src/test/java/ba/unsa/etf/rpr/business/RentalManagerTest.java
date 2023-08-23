package ba.unsa.etf.rpr.business;

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

/*
The tests in this class are written to check operations that actually
add/edit/delete data from the database, so they cannot pass without
connecting to the database. It is necessary that the tests pass without
a connection to the database, therefore these tests are temporarily commented,
but if we know that we have a connection, they can be used to test the mentioned operations.
 */
public class RentalManagerTest {
  /*  private RentalManager rentalManager;
    private BookManager bookManager;
    private MemberManager memberManager;
    private Book testBook;
    private Book addedBook;
    private Member testMember;
    private Member addedMember;
    private Rental testRental;


    @BeforeEach
    public void setup() throws LibraryException {
        // Creating and adding a test book to be rented.
        bookManager = new BookManager();
        testBook = new Book();
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setYearOfPublication("2023");
        testBook.setGenre("Test Genre");
        testBook.setTotalNumber(100);
        testBook.setAvailableNumber(90);
        addedBook = bookManager.add(testBook);
        // Creating and adding a test article to which the book will be rented.
        memberManager = new MemberManager();
        testMember = new Member();
        testMember.setFirstName("Test First Name");
        testMember.setLastName("Test Last Name");
        testMember.setUsername("TestUsername");
        testMember.setPassword("TestPassword");
        testMember.setAdmin(false);
//
//        In case there is a user with the username of the user
//        we are trying to add, we will delete it because adding
//        it would cause us an exception, and we don't want that here.
//
        List<Member> allMembers = memberManager.getAll();
        rentalManager = new RentalManager();
        List<Rental> allRentals = rentalManager.getAll();
        for(Member member : allMembers) {
            if(member.getUsername().equals(testMember.getUsername())) {
//
//                If there is a rental for a user we want to delete,
//                we won't be able to delete it until we delete that rental.
//
                for(Rental rental : allRentals) {
                    if(rental.getMemberID() == member.getId()) rentalManager.delete(rental);
                }
                memberManager.delete(member);
                break;
            }
        }
        addedMember = memberManager.add(testMember);
        // Creating a test rental and using the ids of the newly added test member and book.
        testRental = new Rental();
        testRental.setBookID(addedBook.getId());
        testRental.setMemberID(addedMember.getId());
        LocalDate currentLocalDate = LocalDate.now();
        LocalDate threeMonthsLater = currentLocalDate.plusMonths(3);
        Date sqlCurrentDate = Date.valueOf(currentLocalDate);
        Date sqlDateThreeMonthsLater = Date.valueOf(threeMonthsLater);
        testRental.setRentDate(sqlCurrentDate);
        testRental.setReturnDeadline(sqlDateThreeMonthsLater);
    }
    @Test
    public void testAdd() throws LibraryException {
        Rental addedRental = rentalManager.add(testRental);
        Rental foundRental = rentalManager.searchById(addedRental.getId());
        assertEquals(foundRental, addedRental, "Rental just added, but search by its id does not find it!");
        rentalManager.delete(addedRental);
        bookManager.delete(addedBook);
        memberManager.delete(addedMember);
    }
    @Test
    public void testUpdate() throws LibraryException {
        Rental addedRental = rentalManager.add(testRental);
        Rental foundRental = rentalManager.searchById(addedRental.getId());
        LocalDate updateReturnDeadline = LocalDate.now().plusMonths(4);
        Date sqlUpdateReturnDeadline = Date.valueOf(updateReturnDeadline);
        addedRental.setReturnDeadline(sqlUpdateReturnDeadline);
        rentalManager.update(addedRental);
        Rental foundUpdatedRental = rentalManager.searchById(addedRental.getId());
        // When updating the return deadline of rental, rentals id should not be changed.
        assertEquals(foundRental.getId(), foundUpdatedRental.getId(), "Update failure!");
        rentalManager.delete(addedRental);
        bookManager.delete(addedBook);
        memberManager.delete(addedMember);
    }
    @Test
    public void testDelete() throws LibraryException {
        Rental addedRental = rentalManager.add(testRental);
        rentalManager.delete(addedRental);
        LibraryException exception = assertThrows(
                LibraryException.class,
                () -> rentalManager.searchById(addedRental.getId()),
                "Expected searchById to throw LibraryException, but it didn't"
        );
        assertEquals("Object not found", exception.getMessage(), "Unexpected exception message");
        bookManager.delete(addedBook);
        memberManager.delete(addedMember);
    }
    @Test
    public void testSearchByReturnDeadline() throws LibraryException {
        Rental addedRental = rentalManager.add(testRental);
        List<Rental> foundRentals = rentalManager.searchByReturnDeadline(addedRental.getReturnDeadline());
        assertTrue(foundRentals.contains(addedRental), "Rental not found by return deadline!");
        rentalManager.delete(addedRental);
        bookManager.delete(addedBook);
        memberManager.delete(addedMember);
    }
    @Test
    public void testGetRentalExceedings() throws LibraryException {
        // Setting the rent date to be two months ago
        LocalDate updateRentDate = LocalDate.now().minusMonths(2);
        Date sqlUpdateRentDate = Date.valueOf(updateRentDate);
        testRental.setRentDate(sqlUpdateRentDate);
        // Setting the return deadline to be a month ago (to be expired)
        LocalDate updateReturnDeadline = LocalDate.now().minusMonths(1);
        Date sqlUpdateReturnDeadline = Date.valueOf(updateReturnDeadline);
        testRental.setReturnDeadline(sqlUpdateReturnDeadline);
        Rental addedRental = rentalManager.add(testRental);
        List<Rental> foundRentals = rentalManager.getDeadlineExceedings();
        assertTrue(foundRentals.contains(addedRental), "The rental should have been returned because it expired, but it didn't!");
        rentalManager.delete(addedRental);
        bookManager.delete(addedBook);
        memberManager.delete(addedMember);
    }
    @Test
    public void testRentABook() throws LibraryException {
        Rental addedRental = rentalManager.rentABook(addedMember.getId(), addedBook.getId());
        Rental foundRental = rentalManager.searchById(addedRental.getId());
        assertEquals(addedRental, foundRental);
        // Attempting to rent a book to a user who currently owns the book throws an exception!
        Book testBookForRent = new Book("b", "b", "2023", "bbb", 2, 2);
        Book addedBookForRent = bookManager.add(testBookForRent);
        LibraryException exceptionById = assertThrows(
                LibraryException.class,
                () -> rentalManager.rentABook(addedMember.getId(), addedBookForRent.getId()),
                "Expected rentABook to throw LibraryException, but it didn't"
        );
        assertEquals("You can't rent a book because you haven't returned the one you rented earlier.", exceptionById.getMessage(), "Unexpected exception message");
        rentalManager.delete(addedRental);
        bookManager.delete(addedBook);
        bookManager.delete(addedBookForRent);
        memberManager.delete(addedMember);
    }
    @Test
    public void testReturnRentedBook() throws LibraryException {
        Rental addedRental = rentalManager.rentABook(addedMember.getId(), addedBook.getId());
        rentalManager.returnRentedBook(addedRental.getMemberID());
        LibraryException exceptionById = assertThrows(
                LibraryException.class,
                () -> rentalManager.searchById(addedRental.getId()),
                "Expected searchById to throw LibraryException, but it didn't"
        );
        assertEquals("Object not found", exceptionById.getMessage(), "Unexpected exception message");
        // Attempting to return a book if the user currently has no rented books throws an exception
        LibraryException exceptionReturn = assertThrows(
                LibraryException.class,
                () -> rentalManager.returnRentedBook(addedRental.getMemberID()),
                "Expected returnRentedBook to throw LibraryException, but it didn't"
        );
        assertEquals("You have not rented any books", exceptionReturn.getMessage(), "Unexpected exception message");
        bookManager.delete(addedBook);
        memberManager.delete(addedMember);
    }
    @Test
    public void testCheckUsersRental() throws LibraryException {
//
//        The user whose rentals we are checking was added for test purposes only
//        and at the end of each test his rentals are deleted, so he should not
//        have any rentals at this time.
//
        Rental checkRental = rentalManager.checkUsersRental(addedMember.getId());
        assertNull(checkRental, "checkUsersRental should return null but it didn't!");
        // After we add a rental to a user, checkUsersRental should return that rental.
        Rental addedRental = rentalManager.rentABook(addedMember.getId(), addedBook.getId());
        Rental checkAfterRenting = rentalManager.checkUsersRental(addedMember.getId());
        assertEquals(addedRental, checkAfterRenting, "checkUsersRental should return recently added rental but it didn't");
        // After returning the book, checkUsersRental should return null
        rentalManager.returnRentedBook(addedMember.getId());
        Rental checkAfterReturn = rentalManager.checkUsersRental(addedMember.getId());
        assertNull(checkAfterReturn, "checkUsersRental should return null but it didn't!");
        bookManager.delete(addedBook);
        memberManager.delete(addedMember);
    }
//
//    If an attempt is made to delete a book while it is rented to a user, or to
//    delete a user who currently has a rented book, an exception should be thrown.
//
    @Test
    public void testDeleteBookOrMemberBeforeRentalRemoval() throws LibraryException {
        Rental addedRental = rentalManager.rentABook(addedMember.getId(), addedBook.getId());
        assertThrows(
                LibraryException.class,
                () -> bookManager.delete(addedBook),
                "Expected delete to throw LibraryException, but it didn't"
        );
        assertThrows(
                LibraryException.class,
                () -> memberManager.delete(addedMember),
                "Expected delete to throw LibraryException, but it didn't"
        );
        rentalManager.delete(addedRental);
        bookManager.delete(addedBook);
        memberManager.delete(addedMember);
    }*/
}
