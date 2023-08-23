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
           // System.out.println(m.getUsername());
            member = executeQueryUnique("SELECT * FROM MEMBERS WHERE USERNAME = ?", new Object[]{m.getUsername()});
         //   System.out.println(member.getUsername());
        } catch (LibraryException e) {
            member = null;
        }
        return member;
        /*String query = "SELECT * FROM MEMBERS WHERE USERNAME = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, m.getUsername());
            ResultSet r = stmt.executeQuery();
            /*
            username is unique so it will return one or no rows
             */
          /*  if(r.next()) {
                return new Member(r.getInt(1), r.getString(2), r.getString(3), r.getString(4),
                        r.getString(5), r.getBoolean(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;*/
    }
    private Member checkPassword (Member m) {
        Member member = new Member();
        try {
            member = executeQueryUnique("SELECT * FROM MEMBERS WHERE PASSWORD = ?", new Object[]{m.getPassword()});
        } catch (LibraryException e) {
            member = null;
        }
        return member;
       /* String query = "SELECT * FROM MEMBERS WHERE PASSWORD = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, m.getPassword());
            ResultSet r = stmt.executeQuery();
            /*
            username is unique so it will return one or no rows
             */
        /*    if(r.next()) return new Member(r.getInt(1), r.getString(2), r.getString(3), r.getString(4),
                    r.getString(5), r.getBoolean(6));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;*/
    }
    public void validateMember(Member item) throws LibraryException {
        if (!item.getFirstName().matches("^[a-zA-Z-]+(\\s[a-zA-Z-]+)*$")) {
            throw new LibraryException("Only letters, dashes and spaces. Spaces can only be between two sets of characters.");
        }
       /* if(!item.getFirstName().matches("^[\\S]+(\\s[\\S]+)*$")) {
            throw new LibraryException("Spaces can only be located between 2 sets of characters.");
        }*/
        if(item.getFirstName().length() > 30) {
            throw new LibraryException("First name can't be longer than 30 characters!");
        }
        if (!item.getLastName().matches("^[a-zA-Z-]+(\\s[a-zA-Z-]+)*$")) {
            throw new LibraryException("Only letters, dashes and spaces. Spaces can only be between two sets of characters.");
        }
       /* if(!item.getLastName().matches("^[\\S]+(\\s[\\S]+)*$")) {
            throw new LibraryException("Space can only be located between 2 sets of characters.");
        }*/
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
      /*  Member checkP = checkPassword(item);
        if(checkP != null) {
            throw new LibraryException("Someone is already using this password");
        }*/
        Member checkU = checkUsername(item);
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
        }catch (SQLException e){
            throw new LibraryException(e.getMessage(), e);
        }
       /* String insert = "INSERT INTO MEMBERS(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD, IS_ADMIN) VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, item.getFirstName());
            stmt.setString(2, item.getLastName());
            stmt.setString(3, item.getUsername());
            stmt.setString(4, item.getPassword());
            stmt.setBoolean(5, item.isAdmin());
            stmt.executeUpdate();
            ResultSet r = stmt.getGeneratedKeys();
            r.next();
            item.setId(r.getInt(1));
            return item;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;*/
    }

    @Override
    public Member update(Member item) throws LibraryException {
        validateMember(item);
        Member checkU = checkUsername(item);
        if(checkU != null && checkU.getId() != item.getId()) throw new LibraryException("Someone is already using this username");
     //   Member checkP = checkPassword(item);
       /* if(checkP != null) {
            if(checkP.getId() == item.getId()) throw new LibraryException("You are already using this password");
           // else throw new LibraryException("Someone is already using this password");
        }*/
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
                if (entry.getKey().equals("MEMBER_ID")) continue; // skip ID
                stmt.setObject(counter, entry.getValue());
                counter++;
            }
            stmt.setObject(counter, item.getId());
            stmt.executeUpdate();
            return item;
        }catch (SQLException e){
            throw new LibraryException(e.getMessage(), e);
        }
        /*String updt = "UPDATE MEMBERS SET FIRST_NAME = ?, LAST_NAME = ?, USERNAME = ?, PASSWORD = ?, IS_ADMIN = ? WHERE MEMBER_ID = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(updt, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, item.getFirstName());
            stmt.setString(2, item.getLastName());
            stmt.setString(3, item.getUsername());
            stmt.setString(4, item.getPassword());
            stmt.setBoolean(5, item.isAdmin());
            stmt.setInt(6, item.getId());
            stmt.executeUpdate();
            return item;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;*/
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

   /* @Override
    public Member getById(int id) throws LibraryException {
        return executeQueryUnique("SELECT * FROM MEMBERS WHERE MEMBER_ID = ?", new Object[]{id});
    }*/

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
     /*   String query = "SELECT * FROM MEMBERS WHERE CONCAT(CONCAT(FIRST_NAME, ' '), LAST_NAME) = ?";
        List<Member> members = new ArrayList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet r = stmt.executeQuery();
            while(r.next()) {
                Member member = new Member();
                member.setId(r.getInt("MEMBER_ID"));
                member.setFirstName(r.getString("FIRST_NAME"));
                member.setLastName(r.getString("LAST_NAME"));
                member.setUsername(r.getString("USERNAME"));
                member.setPassword(r.getString("PASSWORD"));
                member.setAdmin(r.getBoolean("IS_ADMIN"));
                members.add(member);
            }
            r.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;*/
    }

    @Override
    public Member searchByUsernameAndPassword(String username, String password) throws LibraryException {
        return executeQueryUnique("SELECT * FROM MEMBERS WHERE BINARY USERNAME = ? AND BINARY PASSWORD = ?", new Object[]{username, password});
        //return executeQueryUnique("SELECT * FROM MEMBERS WHERE USERNAME = ? AND PASSWORD = ?", new Object[]{username, password});
       /* String query = "SELECT * FROM MEMBERS WHERE USERNAME = ? AND PASSWORD = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet r = stmt.executeQuery();
            if(r.next()) {
                Member member = new Member();
                member.setId(r.getInt("MEMBER_ID"));
                member.setFirstName(r.getString("FIRST_NAME"));
                member.setLastName(r.getString("LAST_NAME"));
                member.setUsername(r.getString("USERNAME"));
                member.setPassword(r.getString("PASSWORD"));
                member.setAdmin(r.getBoolean("IS_ADMIN"));
                r.close();
                return member;
            }
            else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;*/
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
