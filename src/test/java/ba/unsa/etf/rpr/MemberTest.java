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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MemberTest {

    private MemberManager memberManager;
    private Member testMember;

    @BeforeEach
    public void setup() throws LibraryException {
        memberManager = new MemberManager();
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
        RentalManager rentalManager = new RentalManager();
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
    }
    /*
    The test checks the methods that perform the basic operations
    of interacting with the database. As part of that, the search
    by id is also being tested.
     */
    @Test
    public void CRUDAndSearchByIdTest() throws LibraryException {
        Member addedMember = memberManager.add(testMember);
        Member foundMember = memberManager.searchById(addedMember.getId());
        assertEquals(foundMember, addedMember, "User just added, but search by his id does not find him!");
        addedMember.setFirstName("Second test First Name");
        memberManager.update(addedMember);
        Member foundUpdatedMember = memberManager.searchById(addedMember.getId());
        // When updating the user's name, his id should not be changed.
        assertEquals(foundMember.getId(), foundUpdatedMember.getId(), "Update failure!");
        memberManager.delete(addedMember);
        LibraryException exception = assertThrows(
                LibraryException.class,
                () -> memberManager.searchById(addedMember.getId()),
                "Expected searchById to throw LibraryException, but it didn't"
        );
        assertEquals("Object not found", exception.getMessage(), "Unexpected exception message");
    }
    /*
    We add a user, and after that we update some data for him except the username.
    Adding such a user should throw an exception because the username is unique.
     */
    @Test
    public void testDuplicateUsernameAddition() throws LibraryException {
        Member addedMember = memberManager.add(testMember);
        testMember.setFirstName("Second test first name");
        testMember.setLastName("Second test last name");
        testMember.setPassword("Second test password");
        LibraryException exception = assertThrows(
                LibraryException.class,
                () -> memberManager.add(testMember),
                "Expected adding user with existing username to throw LibraryException, but it didn't"
        );
        assertEquals("Someone is already using this username", exception.getMessage(), "Unexpected exception message");
        memberManager.delete(addedMember);
    }
    /*
    Testing multiple member search methods by various criteria.
     */
    @Test
    public void testMultiCriteriaSearch() throws LibraryException {
        Member addedMember = memberManager.add(testMember);
        // Testing search by username and password
        Member memberByUsernameAndPassword = memberManager.searchByUsernameAndPassword(addedMember.getUsername(), addedMember.getPassword());
        assertEquals(addedMember, memberByUsernameAndPassword, "User not found by username and password!");
        assertEquals(addedMember.getUsername(), memberByUsernameAndPassword.getUsername(), "Username does not match!");
        assertEquals(addedMember.getPassword(), memberByUsernameAndPassword.getPassword(), "Password does not match!");
        // Testing search by username
        List<Member> memberByUsername = memberManager.searchByUsername(addedMember.getUsername());
        assertTrue(memberByUsername.contains(addedMember));
        // Testing search by first and last name using searchByName method
        List<Member> memberByFirstAndLastName = memberManager.searchByName(addedMember.getFirstName() + " " + addedMember.getLastName());
        assertTrue(memberByFirstAndLastName.contains(addedMember));
        // searchByName should also work when we specify only part of the name as a parameter (e.g. only the first name)
        List<Member> memberByFirstName = memberManager.searchByName(addedMember.getFirstName());
        assertTrue(memberByFirstName.contains(addedMember));
        // searchByName should also work when we specify only part of the name as a parameter (e.g. only the last name)
        List<Member> memberByLastName = memberManager.searchByName(addedMember.getLastName());
        assertTrue(memberByLastName.contains(addedMember));
        memberManager.delete(addedMember);
    }
}
