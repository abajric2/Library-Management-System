package ba.unsa.etf.rpr.exceptions;

public class LibraryException extends Exception {
    public LibraryException(String message, Exception reason){
        super(message, reason);
    }

    public LibraryException(String message){
        super(message);
    }
}
