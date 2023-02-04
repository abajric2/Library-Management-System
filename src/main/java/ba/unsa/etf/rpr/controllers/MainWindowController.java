package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dao.BookDaoSQLImpl;
import ba.unsa.etf.rpr.dao.RentalDaoSQLImpl;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.domain.Rental;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

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
    public TextField rentAuthorId;
    public TextField rentTitleId;
    public TextField returnAuthorId;
    public TextField returnTitleId;
    public Label labelId;
    public ListView listId;
    private Member memeber;
    MainWindowController(Member m) {
        this.memeber = m;
    }
    @FXML
    public void initialize() {
        welcomeLabel.setText(welcomeLabel.getText() + " " + memeber.getFirstName() + "!");
        RentalDaoSQLImpl r = new RentalDaoSQLImpl();
        Rental rent = r.checkUsersRental(memeber.getMemberID());
        if(rent == null) {
            labelId.setText("According to the current record, you have no rented books.");
        }
        else {
            int id = rent.getBookID();
            BookDaoSQLImpl b = new BookDaoSQLImpl();
            Book book = b.searchById(id);
            labelId.setText("According to current records, you currently have book \"" + book.getTitle() + "\" by author "
                    + book.getAuthor() + ". To rent a new book, you need to return this one first.");
        }
    }
    public void profileBttnAction(ActionEvent actionEvent) {
    }

    public void logOutAction(ActionEvent actionEvent) {
    }


    public void helpAction(ActionEvent actionEvent) {
    }

    public void rentAction(ActionEvent actionEvent) {
    }

    public void returnAction(ActionEvent actionEvent) {
    }

    public void allBooksAction(ActionEvent actionEvent) {
        BookDaoSQLImpl b = new BookDaoSQLImpl();
        List<Book> books = new ArrayList<>(b.getAll());
        listId.setItems(FXCollections.observableList(books));
    }

    public void byAuthorAction(ActionEvent actionEvent) {
        if(authorNameId.getText().isEmpty()) {
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
        else listId.setItems(FXCollections.observableList(books));
    }

    public void byTitleAction(ActionEvent actionEvent) {
        if(bookTitleId.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("The author input field is empty!");
            alert.setContentText("You must enter the name of the author whose books you want to view!");
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
        else listId.setItems(FXCollections.observableList(books));
    }
}
