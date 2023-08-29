package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.business.BookManager;
import ba.unsa.etf.rpr.business.MemberManager;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import org.apache.commons.cli.*;

import java.io.PrintWriter;

/**
 * @author Amina Bajric
 * CLI (Command Line Interface) implementation
 */
public class App {
    private static final Option addBook = new Option("b", "add-book", false, "Adding new book to Books database");
    private static final Option addMember = new Option("m", "add-member", false, "Adding new member to MEMBERS database");
    private static final Option getBooks = new Option("getB", "get-books", false, "Printing all books from Books database");
    private static final Option getMembers = new Option("getM", "get-members", false, "Printing all members from MEMBERS database");
    private static final Option titleDefinition = new Option(null, "title", false, "Defining title for next added book");
    private static final Option authorDefinition = new Option(null, "author", false, "Defining author for next added book");
    private static final Option yearOfPublicationDefinition = new Option(null, "year-of-publication", false, "Defining year of publication for next added book");
    private static final Option genreDefinition = new Option(null, "genre", false, "Defining genre for next added book");
    private static final Option totalNumberDefinition = new Option(null, "total-number", false, "Defining total number of books in library for next added book");
    private static final Option availableNumberDefinition = new Option(null, "available-number", false, "Defining available number of books in library for next added book");
    private static final Option firstNameDefinition = new Option(null, "first-name", false, "Defining first name for next added member");
    private static final Option lastNameDefinition = new Option(null, "last-name", false, "Defining last name for next added member");
    private static final Option usernameDefinition = new Option(null, "username", false, "Defining username for next added member");
    private static final Option passwordDefinition = new Option(null, "password", false, "Defining password for next added member");

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
        options.addOption(yearOfPublicationDefinition);
        options.addOption(genreDefinition);
        options.addOption(totalNumberDefinition);
        options.addOption(availableNumberDefinition);
        options.addOption(firstNameDefinition);
        options.addOption(lastNameDefinition);
        options.addOption(usernameDefinition);
        options.addOption(passwordDefinition);
        return options;
    }

    public static void main(String[] args) throws Exception {
        Options options = addOptions();
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine cl = commandLineParser.parse(options, args);
        if((cl.hasOption(addBook.getOpt()) || cl.hasOption(addBook.getLongOpt())) && cl.hasOption(titleDefinition.getLongOpt())
            && cl.hasOption(authorDefinition.getLongOpt()) && cl.hasOption(yearOfPublicationDefinition.getLongOpt()) && cl.hasOption(genreDefinition.getLongOpt())
            && cl.hasOption(totalNumberDefinition.getLongOpt()) && cl.hasOption(availableNumberDefinition.getLongOpt())) {
            BookManager bookManager = new BookManager();
            Book book = new Book();
            book.setTitle(cl.getArgList().get(0));
            book.setAuthor(cl.getArgList().get(1));
            book.setYearOfPublication(cl.getArgList().get(2));
            book.setGenre(cl.getArgList().get(3));
            try {
                book.setAvailableNumber(Integer.parseInt(cl.getArgList().get(4)));
                book.setTotalNumber(Integer.parseInt(cl.getArgList().get(5)));
            } catch(NumberFormatException e) {
                System.out.println("Invalid number format");
                System.exit(1);
            }
            try {
                bookManager.add(book);
                System.out.println("Book successfully added!");
            } catch (LibraryException e) {
                System.out.println("An error occurred while adding book");
                System.exit(1);
            }
        } else if((cl.hasOption(addMember.getOpt()) || cl.hasOption(addMember.getLongOpt())) && cl.hasOption(firstNameDefinition.getLongOpt())
                    && cl.hasOption(lastNameDefinition.getLongOpt()) && cl.hasOption(usernameDefinition.getLongOpt()) && cl.hasOption(passwordDefinition.getLongOpt())) {
            MemberManager memberManager = new MemberManager();
            Member member = new Member();
            member.setFirstName(cl.getArgList().get(0));
            member.setLastName(cl.getArgList().get(1));
            member.setUsername(cl.getArgList().get(2));
            member.setPassword(cl.getArgList().get(3));
            /*
            Since we implement trivial options, without login,
            we assume that not everyone using this interface will
            have the authority to add admin, so we set the value to false
             */
            member.setAdmin(false);
            try {
                memberManager.add(member);
                System.out.println("Member successfully added!");
            } catch(LibraryException e) {
                System.out.println("An error occurred while adding member");
                System.exit(1);
            }
        } else if(cl.hasOption(getBooks.getOpt()) || cl.hasOption(getBooks.getLongOpt())) {
            BookManager bookManager = new BookManager();
            bookManager.viewAll();
        } else if(cl.hasOption(getMembers.getOpt()) || cl.hasOption(getMembers.getLongOpt())){
            MemberManager memberManager = new MemberManager();
            memberManager.viewAll();
        } else {
            printFormattedOptions(options);
            System.exit(1);
        }
    }
}
