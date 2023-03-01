package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.business.BookManager;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ManageBooksController {
    public Button allBooksId;
    public TextField byTitleId;
    public TextField byAuthorId;
    public TextField byGenreId;
    public TableView tableId;
    public TableColumn<Book,Integer> id;
    public TableColumn<Book,String> title ;//= new TableColumn<>();
    public TableColumn<Book,String> author;// = new TableColumn<>();
    public TableColumn<Book, String> yearOfPublication;// = new TableColumn<>();
    public TableColumn<Book, String> genre;// = new TableColumn<>();
    public TableColumn<Book, Integer> totalNumber;// = new TableColumn<>();
    public TableColumn<Book, Integer> availableNumber;// = new TableColumn<>();
    public BookManager manager = new BookManager();
    public TextField titleLabel;
    public TextField authorLabel;
    public TextField yearLabel;
    public TextField genreLabel;
    public TextField totalNumberLabel;

    @FXML
    public void initialize() throws LibraryException {
        id.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getId()).asObject();});
        title.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getTitle());});
        author.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getAuthor());});
        yearOfPublication.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getYearOfPublication());});
        genre.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getGenre());});
        totalNumber.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getTotalNumber()).asObject();});
        availableNumber.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getAvilableNumber()).asObject();});
        tableId.setItems(FXCollections.observableList(manager.getAll()));
        byAuthorId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                BookManager b = new BookManager();
                try {
                    tableId.setItems(FXCollections.observableList(b.searchByAuthor(n)));
                } catch (LibraryException e) {
                    e.printStackTrace();
                }
            }
        });
        byTitleId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                BookManager b = new BookManager();
                try {
                    tableId.setItems(FXCollections.observableList(b.searchByTitle(n)));
                } catch (LibraryException e) {
                    e.printStackTrace();
                }
            }
        });
        byGenreId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                BookManager b = new BookManager();
                try {
                    tableId.setItems(FXCollections.observableList(b.searchByGenre(n)));
                } catch (LibraryException e) {
                    e.printStackTrace();
                }
            }
        });
      //  tableId.getSelectionModel().getSelectedItem();
    }
    public void allBooks(ActionEvent actionEvent) throws LibraryException {
        id.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getId()).asObject();});
        title.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getTitle());});
        author.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getAuthor());});
        yearOfPublication.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getYearOfPublication());});
        genre.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getGenre());});
        totalNumber.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getTotalNumber()).asObject();});
        availableNumber.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getAvilableNumber()).asObject();});
        tableId.setItems(FXCollections.observableList(manager.getAll()));
    }

    public void searchByTitle(ActionEvent actionEvent) {
    }

    public void searchByAuthor(ActionEvent actionEvent) {
    }

    public void searchByGenre(ActionEvent actionEvent) {
    }
}
