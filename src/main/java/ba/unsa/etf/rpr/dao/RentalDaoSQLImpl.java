package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Book;
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
            this.connection = DriverManager.getConnection("jdbc:mysql://sql.freedb.tech:3306/freedb_RPRBaza123321", p.getProperty("username"), p.getProperty("password"));
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
            /*
            When we add an entity related to book rentals,
            it is necessary to reduce the number of available
            books for the book that is rented
             */
            String check = "SELECT * FROM Books b, RENTALS r WHERE b.BOOK_ID = r.BOOK_ID AND r.BOOK_ID = ?";
            PreparedStatement checkstmt = this.connection.prepareStatement(check);
            checkstmt.setInt(1, item.getBookID());
            ResultSet cr = checkstmt.executeQuery();
            /*
            It can return multiple rows, but the data we need will be the same in each row
            (because it refers to the book_id), so it is enough to consider only one
             */
            if(cr.next() && cr.getInt(7) > 0) {
                BookDaoSQLImpl b = new BookDaoSQLImpl();
                b.update(new Book(cr.getInt(1), cr.getString(2), cr.getString(3), cr.getString(4),
                        cr.getString(5), cr.getInt(6), cr.getInt(7) - 1));
            }
            else {
                Rental rental = new Rental(item.getRentalID(), item.getBookID(), item.getMemberID(), item.getRentDate(), item.getReturnDeadline());
                delete(rental);
                throw new SQLException("The book is not available in the library");
            }
            return item;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            /*
            When we delete an entity related to book rentals,
            we need to increase the number of available books
            for the book that was returned
             */
            String availableNumberUpdate = "SELECT * FROM Books b, RENTALS r WHERE b.BOOK_ID = r.BOOK_ID AND r.BOOK_ID = ?";
            PreparedStatement updatestmt = this.connection.prepareStatement(availableNumberUpdate);
            updatestmt.setInt(1, item.getBookID());
            ResultSet cr = updatestmt.executeQuery();
            if(cr.next()) {
                BookDaoSQLImpl b = new BookDaoSQLImpl();
                b.update(new Book(cr.getInt(1), cr.getString(2), cr.getString(3), cr.getString(4),
                        cr.getString(5), cr.getInt(6), cr.getInt(7) + 1));
            }

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
    Member getMember (Rental r) {
        String query = "SELECT * FROM MEMBERS m, RENTALS r WHERE m.MEMBER_ID = r.MEMBER_ID AND r.RENTAL_ID = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, r.getRentalID());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Member m = new Member(rs.getInt(1), rs.getString(2), rs.getString(3));
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    Book getBook (Rental r) {
        String query = "SELECT * FROM Books b, RENTALS r WHERE b.BOOK_ID = r.BOOK_ID AND r.RENTAL_ID = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, r.getRentalID());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Book b = new Book(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getInt(6), rs.getInt(7));
                return b;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void viewAll() {
        List<Rental> l = new ArrayList<>();
        l = getAll();
        for(int i = 0; i < l.size(); i++) {
            System.out.println("\"" + getBook(l.get(i)) + "\" is rented by " + getMember(l.get(i)) + ". " + l.get(i));
        }
    }

    @Override
    public List<Rental> getDeadlineExceedings() {
        String query = "SELECT * FROM RENTALS WHERE CURDATE()>RETURN_DEADLINE";
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
    public List<Rental> searchByReturnDeadline(Date returnDeadline) {
        String query = "SELECT * FROM RENTALS WHERE RETURN_DEADLINE = ?";
        List<Rental> rentals = new ArrayList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setDate(1, (java.sql.Date) returnDeadline);
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
}
