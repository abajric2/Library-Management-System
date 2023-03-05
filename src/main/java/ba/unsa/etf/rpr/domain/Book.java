package ba.unsa.etf.rpr.domain;


/**
 * Class for basic information and methods for managing books in a library
 * It follows the POJO specification
 * @author Amina BAjric
 */
public class Book implements Idable {
    private int bookID;
    private String title;
    private String author;
    private String yearOfPublication;
    private String genre;
    private int totalNumber;
    private int avilableNumber;

    public Book(int bookID, String title, String author, String yearOfPublication, String genre, int totalNumber, int avilableNumber) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.genre = genre;
        this.totalNumber = totalNumber;
        this.avilableNumber = avilableNumber;
    }

    public Book() {
    }

    public int getId() {
        return bookID;
    }

    public void setId(int bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(String yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getAvilableNumber() {
        return avilableNumber;
    }

    public void setAvilableNumber(int avilableNumber) {
        this.avilableNumber = avilableNumber;
    }

    /**
     * method for printing a book in the form "book title, author, year of publication"
     * @return string containing basic information about the book
     */
    @Override
    public String toString() {
        return title + ", " + author + " (" + genre + ")";
    }




}
