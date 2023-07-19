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
import javafx.scene.control.*;

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
    public Button updateBttn;
    public Button addBttn;
    public TextField availableNumberLabel;
    public TextField updtTitle;
    public TextField updtAuthor;
    public TextField updtYear;
    public TextField updtGenre;
    public TextField updtTotal;
    public TextField updtAvailable;
    private BookModel model = new BookModel();
    private Integer idUpdate;

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
        tableId.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
            if(o!=null){
                Book b = (Book) o;
                idUpdate = b.getId();
                titleLabel.textProperty().unbindBidirectional(model.title);
                authorLabel.textProperty().unbindBidirectional((model.author));
                yearLabel.textProperty().unbindBidirectional(model.yearOfPublication);
                genreLabel.textProperty().unbindBidirectional(model.genre);
                totalNumberLabel.textProperty().unbindBidirectional(model.totalNumber);
                availableNumberLabel.textProperty().unbindBidirectional(model.availableNumber);
            }
            Book b = (Book) n;
            if(b != null) {
                idUpdate = b.getId();
                model.fromBook((Book) n);
                titleLabel.textProperty().bindBidirectional(model.title);
                authorLabel.textProperty().bindBidirectional(model.author);
                yearLabel.textProperty().bindBidirectional(model.yearOfPublication);
                genreLabel.textProperty().bindBidirectional(model.genre);
                totalNumberLabel.textProperty().bindBidirectional(model.totalNumber);
                availableNumberLabel.textProperty().bindBidirectional(model.availableNumber);
            }
        });

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

    public void updateAction(ActionEvent actionEvent) throws LibraryException {
        if(titleLabel.getText().isEmpty() || authorLabel.getText().isEmpty() || yearLabel.getText().isEmpty() ||
        genreLabel.getText().isEmpty() || totalNumberLabel.getText().isEmpty() || availableNumberLabel.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Fill in all fields!");
            alert.setContentText("You must fill in all the fields provided or select an option from the drop down menu!");
            alert.showAndWait();
            return;
        }
        if(idUpdate == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Update error!");
            alert.setContentText("Choose the book you want to edit in the table!");
            alert.showAndWait();
            return;
        }
        Book b = new Book();
        b.setId(idUpdate);
        b.setTitle(model.title.get());
        b.setAuthor(model.author.get());
        b.setYearOfPublication(model.yearOfPublication.get());
        b.setGenre(model.genre.get());
        b.setTotalNumber(Integer.parseInt(model.totalNumber.get()));
        b.setAvilableNumber(Integer.parseInt(model.availableNumber.get()));
        try {
            manager.update(b);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Successfully updated!");
            alert.showAndWait();
            tableId.setItems(FXCollections.observableList(manager.getAll()));
            idUpdate = null;
        } catch (LibraryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Update error!");
            alert.setContentText("You can't update this book!");
            alert.showAndWait();
            idUpdate = null;
        }
    }

    public void addAction(ActionEvent actionEvent) {
        if(updtTitle.getText().isEmpty() || updtAuthor.getText().isEmpty() || updtYear.getText().isEmpty() ||
                updtGenre.getText().isEmpty() || updtTotal.getText().isEmpty() || updtAvailable.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Fill in all fields!");
            alert.setContentText("You must fill in all the fields provided or select an option from the drop down menu!");
            alert.showAndWait();
            return;
        }
     /*   if(idUpdate != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Add error!");
            alert.setContentText("You cannot add a book that already exists, if you want to make changes, use the update option!");
            alert.showAndWait();
            return;
        }*/
        Book b = new Book();
        b.setId(1);
        b.setTitle(updtTitle.getText());
        b.setAuthor(updtAuthor.getText());
        b.setYearOfPublication(updtYear.getText());
        b.setGenre(updtGenre.getText());
        b.setTotalNumber(Integer.parseInt(updtTotal.getText()));
        b.setAvilableNumber(Integer.parseInt(updtAvailable.getText()));
        try {
            manager.add(b);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Successfully added!");
            alert.showAndWait();
            tableId.setItems(FXCollections.observableList(manager.getAll()));
        } catch (LibraryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("You can't add book!");
            alert.setContentText("You can't add book!");
            alert.showAndWait();
        }
    }

    public class BookModel {
        public SimpleStringProperty title = new SimpleStringProperty("");
        public SimpleStringProperty author = new SimpleStringProperty("");
        public SimpleStringProperty yearOfPublication = new SimpleStringProperty("");
        public SimpleStringProperty genre = new SimpleStringProperty("");
        public SimpleStringProperty totalNumber = new SimpleStringProperty();
        public SimpleStringProperty availableNumber = new SimpleStringProperty();

        public void fromBook (Book b){
            this.title.set(b.getTitle());
            this.author.set(b.getAuthor());
            this.yearOfPublication.set(b.getYearOfPublication());
            this.genre.set(b.getGenre());
            this.totalNumber.set(Integer.toString(b.getTotalNumber()));
            this.availableNumber.set(Integer.toString(b.getAvilableNumber()));
        }

        public Book toBook(){
            Book b = new Book();
            b.setTitle(this.title.getValue());
            b.setAuthor(this.author.getValue());
            b.setYearOfPublication(this.yearOfPublication.getValue());
            b.setGenre(this.genre.getValue());
            b.setTotalNumber(Integer.parseInt(totalNumber.get()));
            b.setAvilableNumber(Integer.parseInt(availableNumber.get()));
            return b;
        }
    }
}
