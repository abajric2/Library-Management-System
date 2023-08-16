package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.business.BookManager;
import ba.unsa.etf.rpr.business.MemberManager;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MemberTest {

    private MemberManager memberManager;
    private Member testMember;

    @BeforeEach
    public void setup() {
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
}
