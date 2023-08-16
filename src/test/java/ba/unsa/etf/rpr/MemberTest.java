package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.business.BookManager;
import ba.unsa.etf.rpr.business.MemberManager;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Member;
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
        testMember.setFirstName("Test Fist Name");
        testMember.setLastName("Test Last Name");
        testMember.setUsername("Test Username");
        testMember.setPassword("Test Password");
        testMember.setAdmin(false);
    }
}
