package ba.unsa.etf.rpr.domain;

import java.util.Objects;

/**
 * Class for basic information and methods for managing members of a library
 * It follows the POJO specification
 * @author Amina Bajric
 */
public class Member implements Idable {
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

    public int getId() {
        return memberID;
    }

    public void setId(int memberID) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return memberID == member.memberID &&
                isAdmin == member.isAdmin &&
                Objects.equals(firstName, member.firstName) &&
                Objects.equals(lastName, member.lastName) &&
                Objects.equals(username, member.username) &&
                Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberID, firstName, lastName, username, password, isAdmin);
    }
}
