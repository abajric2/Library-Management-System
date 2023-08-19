package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.business.BookManager;
import ba.unsa.etf.rpr.domain.Book;
import org.apache.commons.cli.*;

import java.io.PrintWriter;

public class App {
    private static final Option addBook = new Option("b", "add-book", false, "Adding new book to Books database");
    private static final Option addMember = new Option("m", "add-member", false, "Adding new member to MEMBERS database");
    private static final Option getBooks = new Option("getB", "get-books", false, "Printing all books from Books database");
    private static final Option getMembers = new Option("getM", "get-members", false, "Printing all members from MEMBERS database");
    private static final Option titleDefinition = new Option(null, "title", false, "Defining title for next added book");
    private static final Option authorDefinition = new Option(null, "author", false, "Defining author for next added book");

    /**
     *
     * @param options
     *
     */
    public static void printFormattedOptions(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        PrintWriter printWriter = new PrintWriter(System.out);
        helpFormatter.printUsage(printWriter, 150, "java -jar rpr-projekat.jar [option] 'something else if needed' ");
        helpFormatter.printOptions(printWriter, 150, options, 2, 7);
        printWriter.close();
    }
    public static Options addOptions() {
        Options options = new Options();
        options.addOption(addBook);
        options.addOption(addMember);
        options.addOption(getBooks);
        options.addOption(getMembers);
        options.addOption(titleDefinition);
        options.addOption(authorDefinition);
        return options;
    }
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Options options = addOptions();
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine cl = commandLineParser.parse(options, args);
        if((cl.hasOption(addBook.getOpt()) || cl.hasOption(addBook.getLongOpt()))) {
            BookManager bookManager = new BookManager();
            Book book = new Book();
            book.setTitle("proba");
            book.setAuthor("proba");
            book.setGenre("genre");
            book.setYearOfPublication("2023");
            book.setAvailableNumber(5);
            book.setTotalNumber(5);
            bookManager.add(book);
            System.out.println("You successfully added book to database!");
        } else if(cl.hasOption(getBooks.getOpt()) || cl.hasOption(getBooks.getLongOpt())) {
            BookManager bookManager = new BookManager();
            bookManager.viewAll();
        } else {
            printFormattedOptions(options);
         //   System.exit(1);
        }
    }
}
