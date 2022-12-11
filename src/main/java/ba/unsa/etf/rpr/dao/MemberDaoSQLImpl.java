package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Member;

import java.io.FileReader;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class MemberDaoSQLImpl implements MemberDao {
    private Connection connection;

    public MemberDaoSQLImpl() {
        try {
            FileReader reader = new FileReader("db.properties");
            Properties p = new Properties();
            p.load(reader);
            this.connection = DriverManager.getConnection("jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7583880", p.getProperty("username"), p.getProperty("password"));
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
        return null;
    }

    @Override
    public void delete(Member item) {

    }

    @Override
    public Member searchById(int id) {
        return null;
    }

    @Override
    public List<Member> getAll() {
        return null;
    }

    @Override
    public List<Member> searchByName(String name) {
        return null;
    }
}
