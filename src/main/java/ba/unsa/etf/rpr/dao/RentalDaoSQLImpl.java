package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.domain.Rental;

import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
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
        String query = "SELECT * FROM RENTALS WHERE RENTAL_ID = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet r = stmt.executeQuery();
            if(r.next()) {
                Rental rental = new Rental();
                rental.setBookID(r.getInt("BOOK_ID"));
                rental.setMemberID(r.getInt("MEMBER_ID"));
                rental.setRentDate(r.getDate("RENT_DATE"));
                rental.setReturnDeadline(r.getDate("RETURN_DEADLINE"));
                r.close();
                return rental;
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
    public List<Rental> getAll() {
        String query = "SELECT * FROM RENTALS";
        List<Rental> rentals = new ArrayList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            ResultSet r = stmt.executeQuery();
            while(r.next()) {
                Rental rental = new Rental(r.getInt(1), r.getInt(2), r.getInt(3), r.getDate(4), r.getDate(5));
                rentals.add(rental);
            }
            r.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentals;
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
