package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.business.BookManager;
import ba.unsa.etf.rpr.business.RentalManager;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.domain.Rental;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainWindowController {
    public Button profileBttnId;
    public Button logOutId;
    public Label welcomeLabel;
    public Button rentId;
    public Button returnId;
    public Button allBooksId;
    public Button byAuthorId;
    public Button byTitleId;
    public Button helpId;
    public TextField authorNameId;
    public TextField bookTitleId;
    public TextField genreId;
    public TextField rentAuthorId;
    public TextField rentTitleId;
    public TextField returnAuthorId;
    public TextField returnTitleId;
    public ChoiceBox<Book> choiceBoxId;
    public Label labelId;
    public ListView listId;
    private Member memeber;
    MainWindowController(Member m) {
        this.memeber = m;
    }
    @FXML
    public void initialize() throws LibraryException {
        authorNameId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                BookManager b = new BookManager();
                try {
                    listId.setItems(FXCollections.observableList(b.searchByAuthor(n)));
                } catch (LibraryException e) {
                    e.printStackTrace();
                }
            }
        });
        bookTitleId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                BookManager b = new BookManager();
                try {
                    listId.setItems(FXCollections.observableList(b.searchByTitle(n)));
                } catch (LibraryException e) {
                    e.printStackTrace();
                }
            }
        });
        genreId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                BookManager b = new BookManager();
                try {
                    listId.setItems(FXCollections.observableList(b.searchByGenre(n)));
                } catch (LibraryException e) {
                    e.printStackTrace();
                }
            }
        });
        BookManager b = new BookManager();
        List<Book> books = new ArrayList<>(b.getAll());
        listId.setItems(FXCollections.observableList(books));
        welcomeLabel.setText(welcomeLabel.getText() + " " + memeber.getFirstName() + "!");
        RentalManager r = new RentalManager();
        Rental rent = r.checkUsersRental(memeber.getId());
        if(rent == null) {
            labelId.setText("According to the current record, you have no rented books.");
        }
        else {
            int id = rent.getBookID();
            b = new BookManager();
            Book book = b.searchById(id);
            labelId.setText("According to current records, you currently have book \"" + book.getTitle() + "\" by author "
                    + book.getAuthor() + ". To rent a new book, you need to return this one first.");
        }
        b = new BookManager();
        books = new ArrayList<>(b.getAll());
        choiceBoxId.setItems(FXCollections.observableList(books));
        choiceBoxId.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Book book = choiceBoxId.getValue();
                rentTitleId.setText(book.getTitle());
                rentAuthorId.setText(book.getAuthor());
            }
        });
        // choiceBoxId.setOnAction(this::getCurrentBook);
    }
    /* public void getCurrentBook(ActionEvent event) {
         Book book = choiceBoxId.getValue();
     }*/
    public void profileBttnAction(ActionEvent actionEvent) {
    }

    public void logOutAction(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }


    public void helpAction(ActionEvent actionEvent) {
    }

    public void rentAction(ActionEvent actionEvent) throws LibraryException {
        if(rentTitleId.getText().isEmpty() || rentAuthorId.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Fill in all fields!");
            alert.setContentText("You must fill in all the fields provided or select an option from the drop down menu!");
            alert.showAndWait();
            return;
        }
        BookManager b = new BookManager();
        Book book = new Book();
        try {
            book = b.searchByTitleAndAuthor(rentTitleId.getText(), rentAuthorId.getText());
        } catch(LibraryException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("No books found!");
                alert.setContentText("No book was found with the title and author you entered!");
                alert.showAndWait();
                return;
        }
        RentalManager r = new RentalManager();
        try {
            Rental rental = r.rentABook(memeber.getId(), rentTitleId.getText(), rentAuthorId.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("You have successfully rented a book!");
            alert.showAndWait();
            int id = rental.getBookID();
            book = b.searchById(id);
            labelId.setText("According to current records, you currently have book \"" + book.getTitle() + "\" by author "
                    + book.getAuthor() + ". To rent a new book, you need to return this one first.");
        } catch (LibraryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("You already have a rented book!");
            alert.setContentText("To rent a new book, you must first return the one you currently have!");
            alert.showAndWait();
            return;
        }
    }

    public void returnAction(ActionEvent actionEvent) throws LibraryException {
        /*if(rentTitleId.getText().isEmpty() || rentAuthorId.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Fill in all fields!");
            alert.setContentText("You must fill in all the fields provided or select an option from the drop down menu!");
            alert.showAndWait();
            return;
        }*/
        /*BookDaoSQLImpl b = new BookDaoSQLImpl();
        Book book = b.searchByTitleAndAuthor(rentTitleId.getText(), rentAuthorId.getText());
        if(book == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("No books found!");
            alert.setContentText("No book was found with the title and author you entered!");
            alert.showAndWait();
            return;
        }*/
        RentalManager r = new RentalManager();
        Rental rental = r.checkUsersRental(memeber.getId());
        if(rental == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("You have no rented books!");
            alert.setContentText("You can't return a book because you don't currently have any rented!");
            alert.showAndWait();
            return;
        }
        else {
            Book book = r.getBook(rental);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Are you sure you want to return the book?");
            alert.setContentText("You currently have book \"" + book.getTitle() + "\" by author " + book.getAuthor() + " rented. Are you sure you want to return it?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                r.returnRentedBook(memeber.getId());
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("You have successfully returned the rented book");
                alert.showAndWait();
                labelId.setText("According to the current record, you have no rented books.");
            }
        }
    }

    public void allBooksAction(ActionEvent actionEvent) throws LibraryException {
        BookManager b = new BookManager();
        List<Book> books = new ArrayList<>(b.getAll());
        listId.setItems(FXCollections.observableList(books));
    }

    public void byAuthorAction(ActionEvent actionEvent) throws LibraryException {
      /*  if(authorNameId.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("The author input field is empty!");
            alert.setContentText("You must enter the name of the author whose books you want to view!");
            alert.showAndWait();
            return;
        }
        BookDaoSQLImpl b = new BookDaoSQLImpl();
        List<Book> books = new ArrayList<>(b.searchByAuthor(authorNameId.getText()));
        if(books == null || books.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("No books found!");
            alert.setContentText("No book was found by the author you entered!");
            alert.showAndWait();
        }
        else listId.setItems(FXCollections.observableList(books));*/
    }

    public void byTitleAction(ActionEvent actionEvent) throws LibraryException {
      /*  if(bookTitleId.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("The title input field is empty!");
            alert.setContentText("You must enter the title of the book you want to view!");
            alert.showAndWait();
            return;
        }
        BookDaoSQLImpl b = new BookDaoSQLImpl();
        List<Book> books = new ArrayList<>(b.searchByTitle(bookTitleId.getText()));
        if(books == null || books.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("No books found!");
            alert.setContentText("No book was found with the title you entered!");
            alert.showAndWait();
        }
        else listId.setItems(FXCollections.observableList(books));*/
    }
}