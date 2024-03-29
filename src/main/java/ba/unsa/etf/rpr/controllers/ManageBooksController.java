package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.business.BookManager;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class ManageBooksController {
    public Button allBooksId;
    public TextField byTitleId;
    public TextField byAuthorId;
    public TextField byGenreId;
    public TableView tableId;
    public TableColumn<Book,Integer> id;
    public TableColumn<Book,String> title ;
    public TableColumn<Book,String> author;
    public TableColumn<Book, String> yearOfPublication;
    public TableColumn<Book, String> genre;
    public TableColumn<Book, Integer> totalNumber;
    public TableColumn<Book, Integer> availableNumber;
    public BookManager manager = new BookManager();
    public TextField titleLabel;
    public TextField authorLabel;
    public TextField yearLabel;
    public Button aboutBttn;
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
    public Button deleteBttn;
    public Button mainPageBttn;
    public Button logOutBttn;
    public Label titleUpdateCheck;
    public Label authorUpdateCheck;
    public Label yearUpdateCheck;
    public Label genreUpdateCheck;
    public Label totalNumberUpdateCheck;
    public Label availableNumberUpdateCheck;
    public Label titleAddCheck;
    public Label authorAddCheck;
    public Label yearAddCheck;
    public Label genreAddCheck;
    public Label totalNumberAddCheck;
    public Label availableNumberAddCheck;
    public Button profile;
    private BookModel model = new BookModel();
    private Integer idUpdate;
    private Member member;
    ManageBooksController(Member m) {this.member = m;}
    @FXML
    public void initialize() throws LibraryException {
        id.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getId()).asObject();});
        title.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getTitle());});
        author.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getAuthor());});
        yearOfPublication.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getYearOfPublication());});
        genre.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getGenre());});
        totalNumber.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getTotalNumber()).asObject();});
        availableNumber.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getAvailableNumber()).asObject();});
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
        updtTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 1) {
                titleAddCheck.setText("This field can't be empty");
            } else if(!newValue.matches("^[\\S]+(\\s[\\S]+)*$")) {
                titleAddCheck.setText("Space can only be located between 2 sets of characters.");
            } else if (newValue.length() > 200) {
                titleAddCheck.setText("Title can't be longer than 200 characters!");
            } else {
                titleAddCheck.setText("");
            }
        });
        updtAuthor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 1) {
                authorAddCheck.setText("This field can't be empty");
            } else if(!newValue.matches("^[\\S]+(\\s[\\S]+)*$")) {
                authorAddCheck.setText("Space can only be located between 2 sets of characters.");
            } else if (newValue.length() > 100) {
                authorAddCheck.setText("Authors name can't be longer than 100 characters!");
            } else {
                authorAddCheck.setText("");
            }
        });
        updtYear.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 1) {
                yearAddCheck.setText("This field can't be empty");
            } else if (!newValue.matches("\\d*")) {
                yearAddCheck.setText("Only numbers allowed!");
            } else if(Integer.parseInt(newValue) > LocalDate.now().getYear()) {
                yearAddCheck.setText("Year can't be greater than current year!");
            } else {
                yearAddCheck.setText("");
            }
        });
        updtGenre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 1) {
                genreAddCheck.setText("This field can't be empty");
            } else if(!newValue.matches("^[\\S]+(\\s[\\S]+)*$")) {
                genreAddCheck.setText("Space can only be located between 2 sets of characters.");
            } else if (newValue.length() < 3 || newValue.length() > 50) {
                genreAddCheck.setText("Genre length must be between 3 and 50 characters!");
            } else {
                genreAddCheck.setText("");
            }
        });
        updtTotal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 1) {
                totalNumberAddCheck.setText("This field can't be empty");
            } else if (!newValue.matches("\\d*")) {
                totalNumberAddCheck.setText("Only numbers allowed!");
            } else if(Integer.parseInt(newValue) > 100) {
                totalNumberAddCheck.setText("Total number of books can't be greater than 100!");
            } else {
                totalNumberAddCheck.setText("");
            }
        });
        updtAvailable.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 1) {
                availableNumberAddCheck.setText("This field can't be empty");
            } else if (!newValue.matches("\\d*")) {
                availableNumberAddCheck.setText("Only numbers allowed!");
            } else if(Integer.parseInt(newValue) > 100) {
                availableNumberAddCheck.setText("Available number of books can't be greater than 100!");
            } else {
                availableNumberAddCheck.setText("");
            }
        });
        titleLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 1) {
                titleUpdateCheck.setText("This field can't be empty");
            } else if(!newValue.matches("^[\\S]+(\\s[\\S]+)*$")) {
                titleUpdateCheck.setText("Space can only be located between 2 sets of characters.");
            } else if (newValue.length() > 200) {
                titleUpdateCheck.setText("Title can't be longer than 200 characters!");
            } else {
                titleUpdateCheck.setText("");
            }
        });
        authorLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 1) {
                authorUpdateCheck.setText("This field can't be empty");
            } else if(!newValue.matches("^[\\S]+(\\s[\\S]+)*$")) {
                authorUpdateCheck.setText("Space can only be located between 2 sets of characters.");
            } else if (newValue.length() > 100) {
                authorUpdateCheck.setText("Authors name can't be longer than 100 characters!");
            } else {
                authorUpdateCheck.setText("");
            }
        });
        yearLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 1) {
                yearUpdateCheck.setText("This field can't be empty");
            } else if (!newValue.matches("\\d*")) {
                yearUpdateCheck.setText("Only numbers allowed!");
            } else if(Integer.parseInt(newValue) > LocalDate.now().getYear()) {
                yearUpdateCheck.setText("Year can't be greater than current year!");
            } else {
                yearUpdateCheck.setText("");
            }
        });
        genreLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 1) {
                genreUpdateCheck.setText("This field can't be empty");
            } else if(!newValue.matches("^[\\S]+(\\s[\\S]+)*$")) {
                genreUpdateCheck.setText("Space can only be located between 2 sets of characters.");
            } else if (newValue.length() < 3 || newValue.length() > 50) {
                genreUpdateCheck.setText("Genre length must be between 3 and 50 characters!");
            } else {
                genreUpdateCheck.setText("");
            }
        });
        totalNumberLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 1) {
                totalNumberUpdateCheck.setText("This field can't be empty");
            } else if (!newValue.matches("\\d*")) {
                totalNumberUpdateCheck.setText("Only numbers allowed!");
            } else if(Integer.parseInt(newValue) > 100) {
                totalNumberUpdateCheck.setText("Total number of books can't be greater than 100!");
            } else {
                totalNumberUpdateCheck.setText("");
            }
        });
        availableNumberLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 1) {
                availableNumberUpdateCheck.setText("This field can't be empty");
            } else if (!newValue.matches("\\d*")) {
                availableNumberUpdateCheck.setText("Only numbers allowed!");
            } else if(Integer.parseInt(newValue) > 100) {
                availableNumberUpdateCheck.setText("Available number of books can't be greater than 100!");
            } else {
                availableNumberUpdateCheck.setText("");
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
        availableNumber.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getAvailableNumber()).asObject();});
        tableId.setItems(FXCollections.observableList(manager.getAll()));
    }


    public void updateAction(ActionEvent actionEvent) throws LibraryException {
        if(idUpdate == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Update error!");
            alert.setContentText("Choose the book you want to edit from the table!");
            alert.showAndWait();
            return;
        }
        if(!titleUpdateCheck.getText().isEmpty() || !authorUpdateCheck.getText().isEmpty() || !yearUpdateCheck.getText().isEmpty()
                || !genreUpdateCheck.getText().isEmpty() || !totalNumberUpdateCheck.getText().isEmpty()
                || !availableNumberUpdateCheck.getText().isEmpty()) {
            return;
        }
        if(Integer.parseInt(totalNumberLabel.getText()) < Integer.parseInt(availableNumberLabel.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Update error!");
            alert.setContentText("The number of available books cannot be greater than the total number of books!");
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
        b.setAvailableNumber(Integer.parseInt(model.availableNumber.get()));
        try {
            manager.update(b);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Successfully updated!");
            alert.showAndWait();
            tableId.setItems(FXCollections.observableList(manager.getAll()));
            titleLabel.setText("");
            authorLabel.setText("");
            genreLabel.setText("");
            yearLabel.setText("");
            totalNumberLabel.setText("");
            availableNumberLabel.setText("");
            titleUpdateCheck.setText("");
            authorUpdateCheck.setText("");
            yearUpdateCheck.setText("");
            genreUpdateCheck.setText("");
            totalNumberUpdateCheck.setText("");
            availableNumberUpdateCheck.setText("");
            idUpdate = null;
        } catch (LibraryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Update error!");
            alert.setContentText("An error occurred while updating book!");
            alert.showAndWait();
            idUpdate = null;
        }
    }
    public void about(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/about.fxml"));
        AboutController controller = new AboutController(member);
        loader.setController(controller);
        myStage.setTitle("About");
        myStage.setScene(new Scene(loader.<Parent>load(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.setResizable(true);
        myStage.setMaximized(true);
        myStage.show();
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }
    public void profile(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profile.fxml"));
        ProfileController controller = new ProfileController(member);
        loader.setController(controller);
        myStage.setTitle("Profile");
        myStage.setScene(new Scene(loader.<Parent>load(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.setResizable(true);
        myStage.setMaximized(true);
        myStage.show();
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }
    public void addAction(ActionEvent actionEvent) throws LibraryException {
        if(updtTitle.getText().isEmpty() || updtAuthor.getText().isEmpty() || updtYear.getText().isEmpty() ||
                updtGenre.getText().isEmpty() || updtTotal.getText().isEmpty() || updtAvailable.getText().isEmpty()) {
            if(updtTitle.getText().isEmpty()) titleAddCheck.setText("This field can't be empty");
            if(updtAuthor.getText().isEmpty()) authorAddCheck.setText("This field can't be empty");
            if(updtYear.getText().isEmpty()) yearAddCheck.setText("This field can't be empty");
            if(updtGenre.getText().isEmpty()) genreAddCheck.setText("This field can't be empty");
            if(updtTotal.getText().isEmpty()) totalNumberAddCheck.setText("This field can't be empty");
            if(updtAvailable.getText().isEmpty()) availableNumberAddCheck.setText("This field can't be empty");
            return;
        }
        if(!titleAddCheck.getText().isEmpty() || !authorAddCheck.getText().isEmpty() || !yearAddCheck.getText().isEmpty()
                || !genreAddCheck.getText().isEmpty() || !totalNumberAddCheck.getText().isEmpty()
                || !availableNumberAddCheck.getText().isEmpty()) {
            return;
        }
        if(Integer.parseInt(updtAvailable.getText()) > Integer.parseInt(updtTotal.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Adding error");
            alert.setContentText("The number of available books cannot be greater than the total number of books!");
            alert.showAndWait();
            return;
        }
        Book b = new Book();
        b.setTitle(updtTitle.getText());
        b.setAuthor(updtAuthor.getText());
        b.setYearOfPublication(updtYear.getText());
        b.setGenre(updtGenre.getText());
        b.setTotalNumber(Integer.parseInt(updtTotal.getText()));
        b.setAvailableNumber(Integer.parseInt(updtAvailable.getText()));
        try {
            manager.add(b);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Successfully added!");
            alert.showAndWait();
            tableId.setItems(FXCollections.observableList(manager.getAll()));
            updtTitle.setText("");
            updtAuthor.setText("");
            updtGenre.setText("");
            updtYear.setText("");
            updtTotal.setText("");
            updtAvailable.setText("");
            titleAddCheck.setText("");
            authorAddCheck.setText("");
            yearAddCheck.setText("");
            genreAddCheck.setText("");
            totalNumberAddCheck.setText("");
            availableNumberAddCheck.setText("");
        } catch (LibraryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Adding error!");
            alert.setContentText("An error occurred while adding book!");
            alert.showAndWait();
        }
    }


    public void deleteAction(ActionEvent actionEvent) throws LibraryException {
        if (idUpdate == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Delete error!");
            alert.setContentText("Choose the book you want to delete from the table!");
            alert.showAndWait();
            return;
        }
        if(!titleUpdateCheck.getText().isEmpty() || !authorUpdateCheck.getText().isEmpty() || !yearUpdateCheck.getText().isEmpty()
                || !genreUpdateCheck.getText().isEmpty() || !totalNumberUpdateCheck.getText().isEmpty()
                || !availableNumberUpdateCheck.getText().isEmpty()) {
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation Dialog");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete the book '"
                + model.title.get() + "' by '" + model.author.get() + "'?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Book b = new Book();
            b.setId(idUpdate);
            b.setTitle(model.title.get());
            b.setAuthor(model.author.get());
            b.setYearOfPublication(model.yearOfPublication.get());
            b.setGenre(model.genre.get());
            b.setTotalNumber(Integer.parseInt(model.totalNumber.get()));
            b.setAvailableNumber(Integer.parseInt(model.availableNumber.get()));
            try {
                manager.delete(b);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Successfully deleted!");
                alert.showAndWait();
                tableId.setItems(FXCollections.observableList(manager.getAll()));
                titleLabel.setText("");
                authorLabel.setText("");
                genreLabel.setText("");
                yearLabel.setText("");
                totalNumberLabel.setText("");
                availableNumberLabel.setText("");
                titleUpdateCheck.setText("");
                authorUpdateCheck.setText("");
                yearUpdateCheck.setText("");
                genreUpdateCheck.setText("");
                totalNumberUpdateCheck.setText("");
                availableNumberUpdateCheck.setText("");
                idUpdate = null;
            } catch (LibraryException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Delete error!");
                alert.setContentText("You can't delete this book. Check if it is currently rented by a user!");
                alert.showAndWait();
                idUpdate = null;
            }
        }
    }


    public void mainPage(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminMainWindow.fxml"));
        AdminMainWindowController controller = new AdminMainWindowController(member);
        loader.setController(controller);
        myStage.setTitle("Main window");
        myStage.setScene(new Scene(loader.<Parent>load(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.setResizable(true);
        myStage.setMaximized(true);
        myStage.show();
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        LoginController controller = new LoginController();
        loader.setController(controller);
        myStage.setTitle("Log in");
        myStage.setScene(new Scene(loader.<Parent>load(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.setResizable(false);
        myStage.show();
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
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
            this.availableNumber.set(Integer.toString(b.getAvailableNumber()));
        }

        public Book toBook(){
            Book b = new Book();
            b.setTitle(this.title.getValue());
            b.setAuthor(this.author.getValue());
            b.setYearOfPublication(this.yearOfPublication.getValue());
            b.setGenre(this.genre.getValue());
            b.setTotalNumber(Integer.parseInt(totalNumber.get()));
            b.setAvailableNumber(Integer.parseInt(availableNumber.get()));
            return b;
        }
    }

}
