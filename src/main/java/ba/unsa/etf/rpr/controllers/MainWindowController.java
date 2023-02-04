package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dao.BookDaoSQLImpl;
import ba.unsa.etf.rpr.dao.RentalDaoSQLImpl;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.domain.Rental;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
    public Label labelId;
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

    }

    public void byAuthorAction(ActionEvent actionEvent) {
    }

    public void byTitleAction(ActionEvent actionEvent) {
    }
}
