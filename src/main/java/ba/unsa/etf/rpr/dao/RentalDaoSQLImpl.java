package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Rental;

import java.io.FileReader;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class RentalDaoSQLImpl implements RentalDao {
    private Connection connection;

    public RentalDaoSQLImpl() {
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
    public Rental add(Rental item) {
        String insert = "INSERT INTO RENTALS(BOOK_ID, MEMBER_ID, RENT_DATE, RETURN_DEADLINE) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, item.getBookID());
            stmt.setInt(2, item.getMemberID());
            stmt.setDate(3, (java.sql.Date) item.getRentDate());
            stmt.setDate(4, (java.sql.Date) item.getReturnDeadline());
            stmt.executeUpdate();
            ResultSet r = stmt.getGeneratedKeys();
            r.next();
            item.setRentalID(r.getInt(1));
            return item;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Rental update(Rental item) {
        String updt = "UPDATE RENTALS SET BOOK_ID = ?, MEMBER_ID = ?, RENT_DATE = ?, RETURN_DEADLINE = ? WHERE RENTAL_ID = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(updt, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, item.getBookID());
            stmt.setInt(2, item.getMemberID());
            stmt.setDate(3, (java.sql.Date) item.getRentDate());
            stmt.setDate(4, (java.sql.Date) item.getReturnDeadline());
            stmt.setInt(5, item.getRentalID());
            stmt.executeUpdate();
            return item;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(Rental item) {
        String dlt = "DELETE FROM RENTALS WHERE RENTAL_ID = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(dlt, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, item.getRentalID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Rental searchById(int id) {
        return null;
    }

    @Override
    public List<Rental> getAll() {
        return null;
    }

    @Override
    public List<Rental> deadlineExceedings() {
        return null;
    }

    @Override
    public List<Rental> searchByReturnDeadline(Date returnDeadline) {
        return null;
    }
}
