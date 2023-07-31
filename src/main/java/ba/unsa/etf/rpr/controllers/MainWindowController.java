package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.business.BookManager;
import ba.unsa.etf.rpr.business.RentalManager;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.domain.Rental;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class MainWindowController {
    public Button profileBttnId;
    public Button logOutId;
    public Label welcomeLabel;
    public Button rentId;
    public Button returnId;
    public Button allBooksId;
    public Button byAuthorId;
    public Button byTitleId;
    public Button aboutBttn;
    public TextField authorNameId;
    public TextField bookTitleId;
    public TextField genreId;
    public TextField rentAuthorId;
    public TextField rentTitleId;
    public TextField returnAuthorId;
    public TextField returnTitleId;
    public Label labelId;
    public ListView listId;
    public Button adminModeBttn;
    public TextField rentGenreId;
    public TextField rentYearId;
    public Label rentalExp;
    private Member member;
    private Integer idUpdate;
    private BookModel model = new BookModel();
    MainWindowController(Member m) {
        this.member = m;
    }
    @FXML
    public void initialize() throws LibraryException {
        if(member.isAdmin()) {
            adminModeBttn.setVisible(true);
        }
        else adminModeBttn.setVisible(false);
        authorNameId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                BookManager b = new BookManager();
                try {
                    List<Book> books = b.searchByAuthor(n);
                    sortBooks(books);
                    listId.setItems(FXCollections.observableList(books));
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
                    List<Book> books = b.searchByTitle(n);
                    sortBooks(books);
                    listId.setItems(FXCollections.observableList(books));
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
                    List<Book> books = b.searchByGenre(n);
                    sortBooks(books);
                    listId.setItems(FXCollections.observableList(books));
                } catch (LibraryException e) {
                    e.printStackTrace();
                }
            }
        });
        BookManager b = new BookManager();
        List<Book> books = new ArrayList<>(b.getAll());
        sortBooks(books);
        listId.setItems(FXCollections.observableList(books));
        welcomeLabel.setText(welcomeLabel.getText() + " " + member.getFirstName() + "! ");
        RentalManager r = new RentalManager();
        Rental rent = r.checkUsersRental(member.getId());
        if(rent == null) {
            labelId.setText(" According to the current record, you have no rented books. ");
            rentalExp.setText("");
        }
        else {
            int id = rent.getBookID();
            b = new BookManager();
            Book book = b.searchById(id);
            labelId.setText(" According to current records, you currently have book \"" + book.getTitle() + "\" by author "
                    + book.getAuthor() + ". To rent a new book, you need to return this one first. ");
            java.util.Date utilDate = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            if(rent.getReturnDeadline().before(sqlDate)) {
                rentalExp.setText(" Your book return deadline has expired. You must return the book! ");
            }
            else {
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                String dateString = format.format(rent.getReturnDeadline());
                rentalExp.setText(" The rented book must be returned by the " + dateString + ". ");
            }
        }
        listId.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
            if(o!=null){
                Book book = (Book) o;
                idUpdate = book.getId();
                rentTitleId.textProperty().unbindBidirectional(model.title);
                rentAuthorId.textProperty().unbindBidirectional((model.author));
                rentYearId.textProperty().unbindBidirectional(model.yearOfPublication);
                rentGenreId.textProperty().unbindBidirectional(model.genre);
            }
            Book book = (Book) n;
            if(book != null) {
                idUpdate = book.getId();
                model.fromBook((Book) n);
                rentTitleId.textProperty().bindBidirectional(model.title);
                rentAuthorId.textProperty().bindBidirectional(model.author);
                rentYearId.textProperty().bindBidirectional(model.yearOfPublication);
                rentGenreId.textProperty().bindBidirectional(model.genre);
            }
        });
    /*    choiceBoxId.setItems(FXCollections.observableList(books));
        choiceBoxId.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Book book = choiceBoxId.getValue();
                rentTitleId.setText(book.getTitle());
                rentAuthorId.setText(book.getAuthor());
            }
        });*/
        // choiceBoxId.setOnAction(this::getCurrentBook);
    }
    /* public void getCurrentBook(ActionEvent event) {
         Book book = choiceBoxId.getValue();
     }*/
    public void profileBttnAction(ActionEvent actionEvent) throws IOException {
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

    public void logOutAction(ActionEvent actionEvent) throws IOException {
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

    public void rentAction(ActionEvent actionEvent) throws LibraryException {
        if(rentTitleId.getText().isEmpty() || rentAuthorId.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Choose a book!");
            alert.setContentText("Choose the book you want to rent from the list!");
            alert.showAndWait();
            return;
        }
        BookManager b = new BookManager();
        Book book = new Book();
        try {
            book = b.searchById(idUpdate);
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
            Rental rental = r.rentABook(member.getId(), idUpdate, rentTitleId.getText(), rentAuthorId.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("You have successfully rented a book!");
            alert.showAndWait();
            rentTitleId.setText("");
            rentAuthorId.setText("");
            rentGenreId.setText("");
            rentYearId.setText("");
            idUpdate = null;
            int id = rental.getBookID();
            book = b.searchById(id);
            labelId.setText(" According to current records, you currently have book \"" + book.getTitle() + "\" by author "
                    + book.getAuthor() + ". To rent a new book, you need to return this one first. ");
            java.util.Date utilDate = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            if(rental.getReturnDeadline().before(sqlDate)) {
                rentalExp.setText(" Your book return deadline has expired. You must return the book! ");
            }
            else {
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                String dateString = format.format(rental.getReturnDeadline());
                rentalExp.setText(" The rented book must be returned by the " + dateString + ". ");
            }
        } catch (LibraryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            RentalManager rentalManager = new RentalManager();
            Rental rent = rentalManager.checkUsersRental(member.getId());
            if(rent != null) {
                alert.setHeaderText("You already have a rented book!");
                alert.setContentText("To rent a new book, you must first return the one you currently have!");
            }
            else {
                alert.setHeaderText("You can't rent this book!");
                alert.setContentText("The selected book is currently unavailable!");
            }
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
        Rental rental = r.checkUsersRental(member.getId());
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
            ScrollPane scrollPane = new ScrollPane(new Label("You currently have book \"" + book.getTitle() + "\" by author " + book.getAuthor() + " rented. Are you sure you want to return it?"));
            scrollPane.setFitToWidth(true);
            alert.getDialogPane().setContent(scrollPane);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                r.returnRentedBook(member.getId());
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("You have successfully returned the rented book");
                alert.showAndWait();
                labelId.setText(" According to the current record, you have no rented books. ");
                rentalExp.setText("");
            }
        }
    }

    public void allBooksAction(ActionEvent actionEvent) throws LibraryException {
        BookManager b = new BookManager();
        List<Book> books = new ArrayList<>(b.getAll());
        sortBooks(books);
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

    public void adminMode(ActionEvent actionEvent) throws IOException {
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
    private List<Book> sortBooks(List<Book> books) {
        Collections.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book b1, Book b2) {
                int titleComparison = b1.getTitle().compareTo(b2.getTitle());
                if (titleComparison != 0) {
                    return titleComparison;
                } else {
                    int authorComparison = b1.getAuthor().compareTo(b2.getAuthor());
                    if (authorComparison != 0) {
                        return authorComparison;
                    } else {
                        int yearComparison = b1.getYearOfPublication().compareTo(b2.getYearOfPublication());
                        if (yearComparison != 0) {
                            return yearComparison;
                        } else {
                            return b1.getGenre().compareTo(b2.getGenre());
                        }
                    }
                }
            }
        });
        return books;
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