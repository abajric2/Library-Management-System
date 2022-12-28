package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Member;

import java.util.List;

/**
 * Interface that extends the generic interface "Dao"
 * Implements methods specific to working with members of the library
 * @author Amina Bajric
 */
public interface MemberDao extends Dao<Member> {
    /**
     * method that returns a list of all members with specified first and last name
     * @param name library member full name
     * @return list of members
     */
    List<Member> searchByName (String name);
    Member searchByUserameandPassword(String username, String password);
}
