package ba.unsa.etf.rpr.domain;


import java.util.Objects;

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
    private int availableNumber;

    public Book(String title, String author, String yearOfPublication, String genre, int totalNumber, int availableNumber) {
        this.bookID = 0;
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.genre = genre;
        this.totalNumber = totalNumber;
        this.availableNumber = availableNumber;
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

    public int getAvailableNumber() {
        return availableNumber;
    }

    public void setAvailableNumber(int availableNumber) {
        this.availableNumber = availableNumber;
    }

    /**
     * method for printing a book in the form "book title" - author (genre, year of publication)
     * @return string containing basic information about the book
     */
    @Override
    public String toString() {
        return "\"" + title + "\" - " + author + " (" + genre + ", " + yearOfPublication + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookID == book.bookID &&
                totalNumber == book.totalNumber &&
                availableNumber == book.availableNumber &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(yearOfPublication, book.yearOfPublication) &&
                Objects.equals(genre, book.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookID, title, author, yearOfPublication, genre, totalNumber, availableNumber);
    }


}
