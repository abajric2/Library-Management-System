package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Member;

import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MemberDaoSQLImpl implements MemberDao {
    private Connection connection;

    public MemberDaoSQLImpl() {
        try {
            FileReader reader = new FileReader("db.properties");
            Properties p = new Properties();
            p.load(reader);
            this.connection = DriverManager.getConnection("jdbc:mysql://sql.freedb.tech:3306/freedb_RPRBaza123321", p.getProperty("username"), p.getProperty("password"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Member add(Member item) {
        String insert = "INSERT INTO MEMBERS(FIRST_NAME, LAST_NAME) VALUES(?, ?)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, item.getFirstName());
            stmt.setString(2, item.getLastName());
            stmt.executeUpdate();
            ResultSet r = stmt.getGeneratedKeys();
            r.next();
            item.setMemberID(r.getInt(1));
            return item;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Member update(Member item) {
        String updt = "UPDATE MEMBERS SET FIRST_NAME = ?, LAST_NAME = ? WHERE MEMBER_ID = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(updt, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, item.getFirstName());
            stmt.setString(2, item.getLastName());
            stmt.setInt(3, item.getMemberID());
            stmt.executeUpdate();
            return item;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(Member item) {
        String dlt = "DELETE FROM MEMBERS WHERE MEMBER_ID = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(dlt, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, item.getMemberID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Member searchById(int id) {
        String query = "SELECT * FROM MEMBERS WHERE MEMBER_ID = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet r = stmt.executeQuery();
            if(r.next()) {
                Member member = new Member();
                member.setMemberID(r.getInt("MEMBER_ID"));
                member.setFirstName(r.getString("FIRST_NAME"));
                member.setLastName(r.getString("LAST_NAME"));
                r.close();
                return member;
            }
            else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Member> getAll() {
        String query = "SELECT * FROM MEMBERS";
        List<Member> members = new ArrayList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            ResultSet r = stmt.executeQuery();
            while(r.next()) {
                Member member = new Member(r.getInt(1), r.getString(2), r.getString(3));
                members.add(member);
            }
            r.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    @Override
    public void viewAll() {
        List<Member> l = new ArrayList<>();
        l = getAll();
        for(Member m : l) System.out.println(m);
    }

    @Override
    public List<Member> searchByName(String name) {
        String query = "SELECT * FROM MEMBERS WHERE CONCAT(CONCAT(FIRST_NAME, ' '), LAST_NAME) = ?";
        List<Member> members = new ArrayList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet r = stmt.executeQuery();
            while(r.next()) {
                Member member = new Member();
                member.setMemberID(r.getInt("MEMBER_ID"));
                member.setFirstName(r.getString("FIRST_NAME"));
                member.setLastName(r.getString("LAST_NAME"));
                members.add(member);
            }
            r.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }
}
