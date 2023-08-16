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
    @Test
    public void testSearchByUsernameAndPassword() throws LibraryException {
        Member addedMember = memberManager.add(testMember);
        Member foundMember = memberManager.searchByUsernameAndPassword(addedMember.getUsername(), addedMember.getPassword());
        assertEquals(addedMember, foundMember, "User not found by username and password!");
        memberManager.delete(addedMember);
    }
}
