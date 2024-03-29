package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.domain.Rental;
import ba.unsa.etf.rpr.exceptions.LibraryException;

import java.sql.*;
import java.util.*;

public class RentalDaoSQLImpl extends AbstractDao<Rental> implements RentalDao {
    private static RentalDaoSQLImpl instance = null;
    private RentalDaoSQLImpl() {
        super("RENTALS");
    }

    public static RentalDaoSQLImpl getInstance(){
        if(instance==null)
            instance = new RentalDaoSQLImpl();
        return instance;
    }

    public static void removeInstance(){
        if(instance!=null)
            instance=null;
    }
    @Override
    public Rental add(Rental item) throws LibraryException {
        Rental r = checkUsersRental(item.getMemberID());
        if(r != null) {
            throw new LibraryException("You can't rent a book because you haven't returned the one you rented earlier.");
        }
        BookDaoSQLImpl bimpl = BookDaoSQLImpl.getInstance();
        Book b = new Book();
        try {
            b = bimpl.searchById(item.getBookID());
        } catch (LibraryException e) {
            throw new LibraryException("No books found");
        }
        if(!bimpl.isAvailable(b.getId())) throw new LibraryException("The requested book is currently unavailable!");
        Map<String, Object> row = object2row(item);
        Map.Entry<String, String> columns = prepareInsertParts(row, "RENTAL_ID");
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ").append("RENTALS");
        builder.append(" (").append(columns.getKey()).append(") ");
        builder.append("VALUES (").append(columns.getValue()).append(")");
        try{
            PreparedStatement stmt = getConnection().prepareStatement(builder.toString(), Statement.RETURN_GENERATED_KEYS);
            int counter = 1;
            for (Map.Entry<String, Object> entry: row.entrySet()) {
                // id is automatically generated
                if (entry.getKey().equals("RENTAL_ID")) continue;
                stmt.setObject(counter, entry.getValue());
                counter++;
            }
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            item.setId(rs.getInt(1));
            /*
            When we add an entity related to book rentals,
            it is necessary to reduce the number of available
            books for the book that is rented;
             */
            b.setAvailableNumber(b.getAvailableNumber() - 1);
            bimpl.update(b);
            return item;
        }catch (SQLException e){
            throw new LibraryException(e.getMessage(), e);
        }
    }

    @Override
    public Rental update(Rental item) throws LibraryException {
        Map<String, Object> row = object2row(item);
        String updateColumns = prepareUpdateParts(row, "RENTAL_ID");
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ")
                .append("RENTALS")
                .append(" SET ")
                .append(updateColumns)
                .append(" WHERE RENTAL_ID = ?");

        try{
            PreparedStatement stmt = getConnection().prepareStatement(builder.toString());
            int counter = 1;
            for (Map.Entry<String, Object> entry: row.entrySet()) {
                if (entry.getKey().equals("RENTAL_ID")) continue; // skip id
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
    public Rental checkUsersRental (int memberID) throws LibraryException {
        String query = "SELECT * FROM RENTALS WHERE MEMBER_ID = ?";
        try {
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setInt(1, memberID);
            ResultSet r = stmt.executeQuery();
            /*
            one user can have only one rent, so it will return either only one line or nothing
             */
            if(r.next()) {
                Rental rental = new Rental(r.getInt(2), r.getInt(3), r.getDate(4), r.getDate(5));
                rental.setId(r.getInt(1));
                return rental;
            }
            else return null;
        } catch (SQLException e) {
            throw new LibraryException("No rentals found");
        }
    }
    @Override
    public void returnRentedBook (int memberID) throws LibraryException {
        Rental r = checkUsersRental(memberID);
        if(r != null) delete(r);
        else throw new LibraryException("You have not rented any books");
    }

    @Override
    public Rental row2object(ResultSet rs) throws LibraryException {
        try {
            Rental rental = new Rental();
            rental.setId(rs.getInt("RENTAL_ID"));
            rental.setBookID(rs.getInt("BOOK_ID"));
            rental.setMemberID(rs.getInt("MEMBER_ID"));
            rental.setRentDate(rs.getDate("RENT_DATE"));
            rental.setReturnDeadline(rs.getDate("RETURN_DEADLINE"));
            return rental;
        } catch (SQLException e) {
            throw new LibraryException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> object2row(Rental object) {
        Map<String, Object> row = new TreeMap<>();
        row.put("RENTAL_ID", object.getId());
        row.put("BOOK_ID", object.getBookID());
        row.put("MEMBER_ID", object.getMemberID());
        row.put("RENT_DATE", object.getRentDate());
        row.put("RETURN_DEADLINE", object.getReturnDeadline());
        return row;
    }

    @Override
    public void delete(Rental item) throws LibraryException {
        String dlt = "DELETE FROM RENTALS WHERE RENTAL_ID = ?";
        try {
            PreparedStatement stmt = getConnection().prepareStatement(dlt, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, item.getId());
            stmt.executeUpdate();
            /*
            When we delete an entity related to book rentals,
            we need to increase the number of available books
            for the book that was returned
             */
            BookDaoSQLImpl bimpl = BookDaoSQLImpl.getInstance();
            Book returnedBook = bimpl.searchById(item.getBookID());
            returnedBook.setAvailableNumber(returnedBook.getAvailableNumber() + 1);
            bimpl.update(returnedBook);
        } catch (SQLException e) {
            throw new LibraryException(e.getMessage(), e);
        }
    }

    @Override
    public Rental searchById(int id) throws LibraryException {
        return executeQueryUnique("SELECT * FROM RENTALS WHERE RENTAL_ID = ?", new Object[]{id});
    }

    @Override
    public Member getMember(Rental r) throws LibraryException {
        String query = "SELECT * FROM MEMBERS m, RENTALS r WHERE m.MEMBER_ID = r.MEMBER_ID AND r.RENTAL_ID = ?";
        try {
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setInt(1, r.getId());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Member m = new Member(rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5),rs.getBoolean(6));
                m.setId(rs.getInt(1));
                return m;
            }
        } catch (SQLException e) {
            throw new LibraryException(e.getMessage(), e);
        }
        return null;
    }
    @Override
    public Book getBook (Rental r) throws LibraryException {
        String query = "SELECT * FROM Books b, RENTALS r WHERE b.BOOK_ID = r.BOOK_ID AND r.RENTAL_ID = ?";
        try {
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setInt(1, r.getId());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Book b = new Book(rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getInt(6), rs.getInt(7));
                b.setId(rs.getInt(1));
                return b;
            }
        } catch (SQLException e) {
            throw new LibraryException(e.getMessage(), e);
        }
        return null;
    }

    /**
     * adds n months to the date passed as a parameter
     * @param date
     * @param n
     * @return Date
     */
    private java.sql.Date addMonths(java.sql.Date date, int n) throws LibraryException {
        String query = "SELECT DATE_ADD(?, INTERVAL ? MONTH)";
        try {
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setDate(1, date);
            stmt.setInt(2, n);
            ResultSet r = stmt.executeQuery();
            r.next();
            return r.getDate(1);
        } catch (SQLException e) {
            throw new LibraryException(e.getMessage(), e);
        }
    }
    @Override
    public Rental rentABook(int memberID, int bookId) throws LibraryException {
        Rental r = checkUsersRental(memberID);
        if(r != null) {
            throw new LibraryException("You can't rent a book because you haven't returned the one you rented earlier.");
        }
        BookDaoSQLImpl bimpl = BookDaoSQLImpl.getInstance();
        Book b = new Book();
        try {
            b = bimpl.searchById(bookId);
        } catch (LibraryException e) {
            throw new LibraryException("No books found");
        }
        if(!bimpl.isAvailable(b.getId())) throw new LibraryException("The requested book is currently unavailable!");
        long millis=System.currentTimeMillis();
        java.sql.Date currDate = new java.sql.Date(millis);
        /*
        As id we send anything because it will generate itself.
        The rental date is the current date and the return deadline is 3 months after the current date
         */
        Rental newRent = add(new Rental(b.getId(), memberID, currDate, addMonths(currDate, 3)));
        return newRent;
    }


    @Override
    public List<Rental> getDeadlineExceedings() throws LibraryException {
        String query = "SELECT * FROM RENTALS WHERE CURDATE()>RETURN_DEADLINE";
        List<Rental> rentals = new ArrayList<>();
        try {
            PreparedStatement stmt = getConnection().prepareStatement(query);
            ResultSet r = stmt.executeQuery();
            while(r.next()) {
                Rental rental = new Rental(r.getInt(2), r.getInt(3), r.getDate(4), r.getDate(5));
                rental.setId(r.getInt(1));
                rentals.add(rental);
            }
            r.close();
        } catch (SQLException e) {
            throw new LibraryException("No deadline exceedings found!");
        }
        return rentals;
    }

    @Override
    public List<Rental> searchByReturnDeadline(java.sql.Date returnDeadline) throws LibraryException {
        String query = "SELECT * FROM RENTALS WHERE RETURN_DEADLINE = ?";
        List<Rental> rentals = new ArrayList<>();
        try {
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setDate(1, (java.sql.Date) returnDeadline);
            ResultSet r = stmt.executeQuery();
            while(r.next()) {
                Rental rental = new Rental(r.getInt(2), r.getInt(3), r.getDate(4), r.getDate(5));
                rental.setId(r.getInt(1));
                rentals.add(rental);
            }
            r.close();
        } catch (SQLException e) {
            throw new LibraryException("No return deadlines found!");
        }
        return rentals;
    }
    @Override
    public List<Rental> removeAll() throws LibraryException {
        List<Rental> oldRows = executeQuery("SELECT * FROM RENTALS", null);
        /*
        In order not to change the safe update mode option, the condition is set for id > 0
        because this will delete all rows, since I know that id is not < 0 anywhere
         */
        try {
            PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM RENTALS WHERE RENTAL_ID > 0");
            stmt.executeUpdate();
            return oldRows;
        } catch (SQLException e) {
            throw new LibraryException("Unable to delete all rentals!");
        }
    }
    @Override
    public List<Rental> insertAll(List<Rental> items) {
        List<Rental> insertedRentals = new ArrayList<>();
        for(Rental rental : items) {
            try {
                Rental addedRental = add(rental);
                insertedRentals.add(addedRental);
            } catch (LibraryException e) {}
        }
        return insertedRentals;
    }
}
