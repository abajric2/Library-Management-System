package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.exceptions.LibraryException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
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
    }

    @Override
    public boolean isAvailable(int id) throws LibraryException {
        try {
            Book book = executeQueryUnique("SELECT * FROM Books WHERE BOOK_ID = ?", new Object[]{id});
            return book.getAvailableNumber()>0;
        } catch (LibraryException e) {
            return false;
        }
    }
    /*
    the following constraints on the attributes that make up a book are
    set by exploring some general rules for constraining these types.
     */
    public void validateBook(Book item) throws LibraryException {
        if (!item.getYearOfPublication().matches("\\d*")) {
            throw new LibraryException("The year of publication can only contain numbers!");
        }
        if(Integer.parseInt(item.getYearOfPublication()) < 1 || Integer.parseInt(item.getYearOfPublication()) > LocalDate.now().getYear()) {
            throw new LibraryException("Year can't be negative or greater than current year!");
        }
        if(item.getTotalNumber() < 0 || item.getTotalNumber() > 100) {
            throw new LibraryException("Total number of books can't be negative or greater than 100!");
        }
        if(item.getAvailableNumber() < 0 || item.getAvailableNumber() > 100) {
            throw new LibraryException("Available number of books can't be negative or greater than 100!");
        }
        if(!item.getTitle().matches("^[\\S]+(\\s[\\S]+)*$")) {
            throw new LibraryException("Space can only be located between 2 sets of characters.");
        }
        if(item.getTitle().length() < 1 || item.getTitle().length() > 200) {
            throw new LibraryException("Title can't be empty of longer than 200 characters!");
        }
        if(!item.getAuthor().matches("^[\\S]+(\\s[\\S]+)*$")) {
            throw new LibraryException("Space can only be located between 2 sets of characters.");
        }
        if(item.getAuthor().length() < 1 || item.getAuthor().length() > 100) {
            throw new LibraryException("Authors name can't be empty of longer than 100 characters!");
        }
        if(!item.getGenre().matches("^[\\S]+(\\s[\\S]+)*$")) {
            throw new LibraryException("Space can only be located between 2 sets of characters.");
        }
        if(item.getGenre().length() < 3 || item.getGenre().length() > 50) {
            throw new LibraryException("Genre length must be between 3 and 50 characters!");
        }
        if(item.getAvailableNumber() > item.getTotalNumber()) {
            throw new LibraryException("The number of available books cannot be greater than the total number of books!");
        }
    }
    @Override
    public Book add(Book item) throws LibraryException {
        validateBook(item);
        Map<String, Object> row = object2row(item);
        Map.Entry<String, String> columns = prepareInsertParts(row, "BOOK_ID");
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ").append("Books");
        // appending names of columns
        builder.append(" (").append(columns.getKey()).append(") ");
        // appending as many question marks as there are columns
        builder.append("VALUES (").append(columns.getValue()).append(")");
        try{
            PreparedStatement stmt = getConnection().prepareStatement(builder.toString(), Statement.RETURN_GENERATED_KEYS);
            int counter = 1;
            for (Map.Entry<String, Object> entry: row.entrySet()) {
                // id is automatically generated
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
    }

    @Override
    public Book update(Book item) throws LibraryException {
        validateBook(item);
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
                // id is automatically generated
                if (entry.getKey().equals("BOOK_ID")) continue;
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
    public void delete(Book item) throws LibraryException {
        String sql = "DELETE FROM Books WHERE BOOK_ID = ?";
        try{
            PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setObject(1, item.getId());
            stmt.executeUpdate();
        }catch (SQLException e){
            throw new LibraryException(e.getMessage(), e);
        }
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
            book.setAvailableNumber(rs.getInt("AVAILABLE_NUMBER"));
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
        row.put("AVAILABLE_NUMBER", object.getAvailableNumber());
        return row;
    }
    @Override
    public Book searchById(int id) throws LibraryException {
        return executeQueryUnique("SELECT * FROM Books WHERE BOOK_ID = ?", new Object[]{id});
    }
    @Override
    public List<Book> removeAll() throws LibraryException {
        List<Book> oldRows = executeQuery("SELECT * FROM Books", null);
        /*
        In order not to change the safe update mode option, the condition is set for id > 0
        because this will delete all rows, since I know that id is not < 0 anywhere
         */
        try {
            PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM Books WHERE BOOK_ID > 0");
            stmt.executeUpdate();
            return oldRows;
        } catch (SQLException e) {
            throw new LibraryException("Unable to delete all books!");
        }
    }

    @Override
    public List<Book> insertAll(List<Book> items) {
        List<Book> insertedBooks = new ArrayList<>();
        for(Book book : items) {
            try {
                Book addedBook = add(book);
                insertedBooks.add(addedBook);
            } catch (LibraryException e) {}
        }
        return insertedBooks;
    }
}