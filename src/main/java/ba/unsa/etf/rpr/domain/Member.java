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

    public Member(int memberID, String firstName, String lastName) {
        this.memberID = memberID;
        this.firstName = firstName;
        this.lastName = lastName;
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
