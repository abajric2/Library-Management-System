package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Book;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BookDaoSQLImpl implements BookDao {
    private Connection connection;

    public BookDaoSQLImpl() {
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
    public List<Book> searchByAuthor(String author) {
        String query = "SELECT * FROM BOOKS WHERE AUTHOR = ?";
        List<Book> books = new ArrayList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, author);
            ResultSet r = stmt.executeQuery();
            while(r.next()) {
                Book book = new Book();
                book.setBookID(r.getInt("BOOK_ID"));
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
        return books;
    }

    @Override
    public List<Book> searchByGenre(String genre) {
        String query = "SELECT * FROM BOOKS WHERE GENRE = ?";
        List<Book> books = new ArrayList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, genre);
            ResultSet r = stmt.executeQuery();
            while(r.next()) {
                Book book = new Book();
                book.setBookID(r.getInt("BOOK_ID"));
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
        return books;
    }

    @Override
    public List<Book> searchByTitle(String title) {
        String query = "SELECT * FROM BOOKS WHERE TITLE = ?";
        List<Book> books = new ArrayList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, title);
            ResultSet r = stmt.executeQuery();
            while(r.next()) {
                Book book = new Book();
                book.setBookID(r.getInt("BOOK_ID"));
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
        return books;
    }

    @Override
    public boolean isAvailable(String title, String author) {
        String query = "SELECT DISTINCT AVAILABLE_NUMBER FROM BOOKS WHERE TITLE = ? AND AUTHOR = ?";
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
        return available;
    }
    @Override
    public Book add(Book item) {
        String insert = "INSERT INTO BOOKS(TITLE, AUTHOR, YEAR_OF_PUBLICATION, GENRE, TOTAL_NUMBER, AVAILABLE_NUMBER) VALUES(?, ?, ?, ?, ?, ?)";
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
            item.setBookID(r.getInt(1));
            return item;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Book update(Book item) {
        String updt = "UPDATE BOOKS SET TITLE = ?, AUTHOR = ?, YEAR_OF_PUBLICATION = ?, GENRE = ?, TOTAL_NUMBER = ?, AVAILABLE_NUMBER = ? WHERE BOOK_ID = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(updt, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, item.getTitle());
            stmt.setString(2, item.getAuthor());
            stmt.setString(3, item.getYearOfPublication());
            stmt.setString(4, item.getGenre());
            stmt.setInt(5, item.getTotalNumber());
            stmt.setInt(6, item.getAvilableNumber());
            stmt.setInt(7, item.getBookID());
            stmt.executeUpdate();
            return item;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(Book item) {
        String dlt = "DELETE FROM BOOKS WHERE BOOK_ID = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(dlt, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, item.getBookID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Book searchById(int id) {
        String query = "SELECT * FROM BOOKS WHERE BOOK_ID = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet r = stmt.executeQuery();
            if(r.next()) {
                Book book = new Book();
                book.setBookID(r.getInt("BOOK_ID"));
                book.setTitle(r.getString("TITLE"));
                book.setAuthor(r.getString("AUTHOR"));
                book.setYearOfPublication(r.getString("YEAR_OF_PUBLICATION"));
                book.setGenre(r.getString("GENRE"));
                book.setTotalNumber(r.getInt("TOTAL_NUMBER"));
                book.setAvilableNumber(r.getInt("AVAILABLE_NUMBER"));
                r.close();
                return book;
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
    public List<Book> getAll() {
        String query = "SELECT * FROM BOOKS";
        List<Book> books = new ArrayList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            ResultSet r = stmt.executeQuery();
            while(r.next()) {
                Book book = new Book(r.getInt(1), r.getString(2), r.getString(3),
                                     r.getString(4), r.getString(5), r.getInt(6), r.getInt(7));
                books.add(book);
            }
            r.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}
