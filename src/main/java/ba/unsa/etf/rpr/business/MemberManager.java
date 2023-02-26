package ba.unsa.etf.rpr.business;

import ba.unsa.etf.rpr.dao.DaoFactory;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.exceptions.LibraryException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MemberManager {
    public List<Member> getAll() throws LibraryException {
        return DaoFactory.memberDao().getAll();
    }
    public void viewAll() throws LibraryException {
        DaoFactory.memberDao().viewAll();
    }

    public Member add(Member item) throws LibraryException {
        return DaoFactory.memberDao().add(item);
    }

    public Member update(Member item) throws LibraryException {
        return DaoFactory.memberDao().update(item);
    }

/*    public Member getById(int id) throws LibraryException {
        return DaoFactory.memberDao().get
    }*/

    public void delete(Member item) throws LibraryException {
        DaoFactory.memberDao().delete(item);
    }

    public Member searchById(int id) throws LibraryException {
        return DaoFactory.memberDao().searchById(id);
    }

    public List<Member> searchByName(String name) throws LibraryException {
        return DaoFactory.memberDao().searchByName(name);
    }

    public Member searchByUserameandPassword(String username, String password) throws LibraryException {
        return DaoFactory.memberDao().searchByUserameandPassword(username, password);
    }
}