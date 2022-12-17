package ba.unsa.etf.rpr.domain;

/**
 * Class for basic information and methods for managing members of a library
 * It follows the POJO specification
 * @author Amina Bajric
 */
public class Member {
    private int memberID;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isAdmin;

    public Member(int memberID, String firstName, String lastName, String username, String password, boolean isAdmin) {
        this.memberID = memberID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Member() {
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * method for printing a member in the form "first name last name"
     * @return string that contains the name of the library member
     */
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
