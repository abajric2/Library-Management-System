package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.exceptions.LibraryException;

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
    List<Member> searchByName (String name) throws LibraryException;

    /**
     * method that returns a list of all members with specified username
     * @param username library member username
     * @return list of members
     */
    List<Member> searchByUsername(String username) throws LibraryException;

    /**
     * method that returns a member with specified username and password
     * @param username
     * @param password
     * @return library member
     * @throws LibraryException
     */
    Member searchByUsernameAndPassword(String username, String password) throws LibraryException;

    /**
     * checks that all members attributes are in the correct format and of the correct types,
     * and throws exception if not
     * @param item member
     * @throws LibraryException
     */
    void validateMember(Member item) throws LibraryException;
}
