package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.exceptions.LibraryException;

import java.sql.*;
import java.util.*;

public class MemberDaoSQLImpl extends AbstractDao<Member> implements MemberDao {
    private static  MemberDaoSQLImpl instance = null;
    private MemberDaoSQLImpl() {
        super("MEMBERS");
    }

    public static MemberDaoSQLImpl getInstance(){
        if(instance==null)
            instance = new MemberDaoSQLImpl();
        return instance;
    }
    public static void removeInstance(){
        if(instance!=null)
            instance=null;
    }
    private Member checkUsername (Member m) throws LibraryException {
        Member member = new Member();
        try {
            member = executeQueryUnique("SELECT * FROM MEMBERS WHERE BINARY USERNAME = ?", new Object[]{m.getUsername()});
        } catch (LibraryException e) {
            // executeQueryUnique throws an exception if object is not found
            member = null;
        }
        return member;
    }
    /*
    the following constraints on the attributes of a member are
    set by exploring some general rules for constraining these types.
     */
    public void validateMember(Member item) throws LibraryException {
        if (!item.getFirstName().matches("^[a-zA-Z-]+(\\s[a-zA-Z-]+)*$")) {
            throw new LibraryException("Only letters, dashes and spaces. Spaces can only be between two sets of characters.");
        }
        if(item.getFirstName().length() > 30) {
            throw new LibraryException("First name can't be longer than 30 characters!");
        }
        if (!item.getLastName().matches("^[a-zA-Z-]+(\\s[a-zA-Z-]+)*$")) {
            throw new LibraryException("Only letters, dashes and spaces. Spaces can only be between two sets of characters.");
        }
        if(item.getLastName().length() > 50) {
            throw new LibraryException("Last name can't be longer than 50 characters");
        }
        if (!item.getUsername().matches("^[a-zA-Z0-9_.-]+$")) {
            throw new LibraryException("Username can only contain letters, numbers, underscores, dots, and dashes.");
        }
        if(item.getUsername().length() < 3 || item.getUsername().length() > 30) {
            throw new LibraryException("Username length must be between 3 and 30 characters");
        }
        if(!item.getPassword().matches("^[^\\s]+$")) {
            throw new LibraryException("The password can't be empty, or contain spaces or other blank characters.");
        }
        if(item.getPassword().length() < 8 || item.getPassword().length() > 128) {
            throw new LibraryException("Password must be between 8 and 128 characters long!");
        }
    }
    @Override
    public Member add(Member item) throws LibraryException {
        validateMember(item);
        Member checkU = checkUsername(item);
        // username has to be unique
        if(checkU != null) throw new LibraryException("Someone is already using this username");
        Map<String, Object> row = object2row(item);
        Map.Entry<String, String> columns = prepareInsertParts(row, "MEMBER_ID");
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ").append("MEMBERS");
        builder.append(" (").append(columns.getKey()).append(") ");
        builder.append("VALUES (").append(columns.getValue()).append(")");
        try{
            PreparedStatement stmt = getConnection().prepareStatement(builder.toString(), Statement.RETURN_GENERATED_KEYS);
            int counter = 1;
            for (Map.Entry<String, Object> entry: row.entrySet()) {
                if (entry.getKey().equals("MEMBER_ID")) continue;
                stmt.setObject(counter, entry.getValue());
                counter++;
            }
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            item.setId(rs.getInt(1));
            return item;
        } catch (SQLException e){
            throw new LibraryException(e.getMessage(), e);
        }
    }

    @Override
    public Member update(Member item) throws LibraryException {
        validateMember(item);
        Member checkU = checkUsername(item);
        // username has to be unique, but if leaving the same username for user that we are updating, than it is ok
        if(checkU != null && checkU.getId() != item.getId()) throw new LibraryException("Someone is already using this username");
        Map<String, Object> row = object2row(item);
        String updateColumns = prepareUpdateParts(row, "MEMBER_ID");
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ")
                .append("MEMBERS")
                .append(" SET ")
                .append(updateColumns)
                .append(" WHERE MEMBER_ID = ?");

        try{
            PreparedStatement stmt = getConnection().prepareStatement(builder.toString());
            int counter = 1;
            for (Map.Entry<String, Object> entry: row.entrySet()) {
                if (entry.getKey().equals("MEMBER_ID")) continue; // skip id
                stmt.setObject(counter, entry.getValue());
                counter++;
            }
            stmt.setObject(counter, item.getId());
            stmt.executeUpdate();
            return item;
        }catch (SQLException e){
            throw new LibraryException(e.getMessage(), e);
        }
    }

    @Override
    public Member row2object(ResultSet rs) throws LibraryException {
        try {
            Member member = new Member();
            member.setId(rs.getInt("MEMBER_ID"));
            member.setFirstName(rs.getString("FIRST_NAME"));
            member.setLastName(rs.getString("LAST_NAME"));
            member.setUsername(rs.getString("USERNAME"));
            member.setPassword(rs.getString("PASSWORD"));
            member.setAdmin(rs.getBoolean("IS_ADMIN"));
            return member;
        } catch (SQLException e) {
            throw new LibraryException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> object2row(Member object) {
        Map<String, Object> row = new TreeMap<>();
        row.put("MEMBER_ID", object.getId());
        row.put("FIRST_NAME", object.getFirstName());
        row.put("LAST_NAME", object.getLastName());
        row.put("USERNAME", object.getUsername());
        row.put("PASSWORD", object.getPassword());
        row.put("IS_ADMIN", object.isAdmin());
        return row;
    }

    @Override
    public void delete(Member item) throws LibraryException {
        String sql = "DELETE FROM MEMBERS WHERE MEMBER_ID = ?";
        try{
            PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setObject(1, item.getId());
            stmt.executeUpdate();
        }catch (SQLException e){
            throw new LibraryException(e.getMessage(), e);
        }
    }
    @Override
    public List<Member> searchByUsername(String username) throws LibraryException {
        return executeQuery("SELECT * FROM MEMBERS WHERE USERNAME LIKE concat('%', ? ,'%')", new Object[]{username});
    }
    @Override
    public Member searchById(int id) throws LibraryException {
        return executeQueryUnique("SELECT * FROM MEMBERS WHERE MEMBER_ID = ?", new Object[]{id});
    }


    @Override
    public List<Member> searchByName(String name) throws LibraryException {
        return executeQuery("SELECT * FROM MEMBERS WHERE CONCAT(CONCAT(FIRST_NAME, ' '), LAST_NAME) LIKE concat('%', ? ,'%')", new Object[]{name});
    }

    @Override
    public Member searchByUsernameAndPassword(String username, String password) throws LibraryException {
        // binary is used for case-sensitive search
        return executeQueryUnique("SELECT * FROM MEMBERS WHERE BINARY USERNAME = ? AND BINARY PASSWORD = ?", new Object[]{username, password});
    }
    @Override
    public List<Member> removeAll() throws LibraryException {
        List<Member> oldRows = executeQuery("SELECT * FROM MEMBERS", null);
        /*
        In order not to change the safe update mode option, the condition is set for id > 0
        because this will delete all rows, since I know that id is not < 0 anywhere
         */
        try {
            PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM MEMBERS WHERE MEMBER_ID > 0");
            stmt.executeUpdate();
            return oldRows;
        } catch (SQLException e) {
            throw new LibraryException("Unable to delete all members!");
        }
    }
    @Override
    public List<Member> insertAll(List<Member> items) {
        List<Member> insertedMembers = new ArrayList<>();
        for(Member member : items) {
            try {
                Member addedMember = add(member);
                insertedMembers.add(addedMember);
            } catch (LibraryException e) {}
        }
        return insertedMembers;
    }
}
