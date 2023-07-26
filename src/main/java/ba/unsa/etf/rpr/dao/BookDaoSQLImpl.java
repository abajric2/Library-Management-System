package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.exceptions.LibraryException;

import java.sql.*;
import java.util.*;

public class BookDaoSQLImpl extends AbstractDao<Book> implements BookDao {

    private static  BookDaoSQLImpl instance = null;
    private BookDaoSQLImpl() {
        super("Books");
    }

    public static BookDaoSQLImpl getInstance(){
        if(instance==null)
            instance = new BookDaoSQLImpl();
        return instance;
    }
    public static void removeInstance(){
        if(instance!=null)
            instance=null;
    }

    @Override
    public List<Book> searchByAuthor(String author) throws LibraryException {
        return executeQuery("SELECT * FROM Books WHERE AUTHOR LIKE concat('%', ? ,'%')", new Object[]{author});
    }

    @Override
    public List<Book> searchByGenre(String genre) throws LibraryException {
        return executeQuery("SELECT * FROM Books WHERE GENRE LIKE concat('%', ? ,'%')", new Object[]{genre});
    }

    @Override
    public List<Book> searchByTitle(String title) throws LibraryException {
        return executeQuery("SELECT * FROM Books WHERE TITLE LIKE concat('%', ? ,'%')", new Object[]{title});
     /*   String query = "SELECT * FROM Books WHERE TITLE = ?";
        List<Book> books = new ArrayList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, title);
            ResultSet r = stmt.executeQuery();
            while(r.next()) {
                Book book = new Book();
                book.setId(r.getInt("BOOK_ID"));
                book.setTitle(r.getString("TITLE"));
                book.setAuthor(r.getString("AUTHOR"));
                book.setYearOfPublication(r.getString("YEAR_OF_PUBLICATION"));
                book.setGenre(r.getString("GENRE"));
                book.setTotalNumber(r.getInt("TOTAL_NUMBER"));
                book.setAvilableNumber(r.getInt("AVAILABLE_NUMBER"));
                books.add(book);
            }
            r.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;*/
    }

    @Override
    public Book searchByTitleAndAuthor(String title, String author) throws LibraryException {
        /*
        we can expect that one or no rows will be returned,
        because in practice there is very little chance that
        there will be two different books that have the same
        title and the same author name
         */
        return executeQueryUnique("SELECT * FROM Books WHERE TITLE = ? AND AUTHOR = ?", new Object[]{title, author});
      /*  String query = "SELECT * FROM Books WHERE TITLE = ? AND AUTHOR = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, title);
            stmt.setString(2, author);
            ResultSet r = stmt.executeQuery();
            if(r.next()) {
                return new Book(r.getInt(1), r.getString(2), r.getString(3), r.getString(4),
                        r.getString(5), r.getInt(6), r.getInt(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;*/
    }

    @Override
    public boolean isAvailable(String title, String author) throws LibraryException {
        try {
            Book book = executeQueryUnique("SELECT DISTINCT AVAILABLE_NUMBER FROM Books WHERE TITLE = ? AND AUTHOR = ?", new Object[]{author});
            return book.getAvilableNumber()>0;
        } catch (LibraryException e) {
            return false;
        }
        /*String query = "SELECT DISTINCT AVAILABLE_NUMBER FROM Books WHERE TITLE = ? AND AUTHOR = ?";
        boolean available = false;
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, title);
            stmt.setString(2, author);
            ResultSet r = stmt.executeQuery();
            if(r.next() && r.getInt("AVAILABLE_NUMBER")>0) available = true;  // statement returns distinct values, so it will return only one row or none
            else available = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return available;*/
    }

    @Override
    public Book add(Book item) throws LibraryException {
        if (!item.getYearOfPublication().matches("\\d*")) {
            throw new LibraryException("The year of publication can only contain numbers!");
        }
        if(item.getAvilableNumber() > item.getTotalNumber()) {
            throw new LibraryException("The number of available books cannot be greater than the total number of books!");
        }
        Map<String, Object> row = object2row(item);
        Map.Entry<String, String> columns = prepareInsertParts(row, "BOOK_ID");
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ").append("Books");
        builder.append(" (").append(columns.getKey()).append(") ");
        builder.append("VALUES (").append(columns.getValue()).append(")");
        try{
            PreparedStatement stmt = getConnection().prepareStatement(builder.toString(), Statement.RETURN_GENERATED_KEYS);
            int counter = 1;
            for (Map.Entry<String, Object> entry: row.entrySet()) {
                if (entry.getKey().equals("BOOK_ID")) continue;
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
       /* String insert = "INSERT INTO Books(TITLE, AUTHOR, YEAR_OF_PUBLICATION, GENRE, TOTAL_NUMBER, AVAILABLE_NUMBER) VALUES(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, item.getTitle());
            stmt.setString(2, item.getAuthor());
            stmt.setString(3, item.getYearOfPublication());
            stmt.setString(4, item.getGenre());
            stmt.setInt(5, item.getTotalNumber());
            stmt.setInt(6, item.getAvilableNumber());
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
    public Book update(Book item) throws LibraryException {
        if (!item.getYearOfPublication().matches("\\d*")) {
            throw new LibraryException("The year of publication can only contain numbers!");
        }
        if(item.getAvilableNumber() > item.getTotalNumber()) {
            throw new LibraryException("The number of available books cannot be greater than the total number of books!");
        }
        Map<String, Object> row = object2row(item);
        String updateColumns = prepareUpdateParts(row, "BOOK_ID");
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ")
                .append("Books")
                .append(" SET ")
                .append(updateColumns)
                .append(" WHERE BOOK_ID = ?");

        try{
            PreparedStatement stmt = getConnection().prepareStatement(builder.toString());
            int counter = 1;
            for (Map.Entry<String, Object> entry: row.entrySet()) {
                if (entry.getKey().equals("BOOK_ID")) continue; // skip ID
                stmt.setObject(counter, entry.getValue());
                counter++;
            }
            stmt.setObject(counter, item.getId());
            stmt.executeUpdate();
            return item;
        }catch (SQLException e){
            throw new LibraryException(e.getMessage(), e);
        }
        /*String updt = "UPDATE Books SET TITLE = ?, AUTHOR = ?, YEAR_OF_PUBLICATION = ?, GENRE = ?, TOTAL_NUMBER = ?, AVAILABLE_NUMBER = ? WHERE BOOK_ID = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(updt, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, item.getTitle());
            stmt.setString(2, item.getAuthor());
            stmt.setString(3, item.getYearOfPublication());
            stmt.setString(4, item.getGenre());
            stmt.setInt(5, item.getTotalNumber());
            stmt.setInt(6, item.getAvilableNumber());
            stmt.setInt(7, item.getId());
            stmt.executeUpdate();
            return item;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;*/
    }

    @Override
    public void delete(Book item) throws LibraryException {
        String sql = "DELETE FROM Books WHERE BOOK_ID = ?";
        try{
            PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setObject(1, item.getId());
            stmt.executeUpdate();
        }catch (SQLException e){
            throw new LibraryException(e.getMessage(), e);
        }
       /* String dlt = "DELETE FROM Books WHERE BOOK_ID = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(dlt, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, item.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public Book row2object(ResultSet rs) throws LibraryException {
        try {
            Book book = new Book();
            book.setId(rs.getInt("BOOK_ID"));
            book.setTitle(rs.getString("TITLE"));
            book.setAuthor(rs.getString("AUTHOR"));
            book.setYearOfPublication(rs.getString("YEAR_OF_PUBLICATION"));
            book.setGenre(rs.getString("GENRE"));
            book.setTotalNumber(rs.getInt("TOTAL_NUMBER"));
            book.setAvilableNumber(rs.getInt("AVAILABLE_NUMBER"));
            return book;
        } catch (SQLException e) {
            throw new LibraryException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> object2row(Book object) {
        Map<String, Object> row = new TreeMap<>();
        row.put("BOOK_ID", object.getId());
        row.put("TITLE", object.getTitle());
        row.put("AUTHOR", object.getAuthor());
        row.put("YEAR_OF_PUBLICATION", object.getYearOfPublication());
        row.put("GENRE", object.getGenre());
        row.put("TOTAL_NUMBER", object.getTotalNumber());
        row.put("AVAILABLE_NUMBER", object.getAvilableNumber());
        return row;
    }

   /* @Override
    public Book getById(int id) throws LibraryException {
        return executeQueryUnique("SELECT * FROM Books WHERE BOOK_ID = ?", new Object[]{id});
    }*/

    @Override
    public Book searchById(int id) throws LibraryException {
        return executeQueryUnique("SELECT * FROM Books WHERE BOOK_ID = ?", new Object[]{id});
    }

}