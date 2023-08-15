package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.business.BookManager;
import ba.unsa.etf.rpr.business.MemberManager;
import ba.unsa.etf.rpr.business.RentalManager;
import ba.unsa.etf.rpr.domain.Book;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.domain.Rental;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class AddNewRentalController {
    public TableView booksTable;
    public TableColumn<Book,Integer> bookId;
    public TableColumn<Book,String> title;
    public TableColumn<Book,String> author;
    public TableColumn<Book,String> year;
    public TableColumn<Book,String> genre;
    public TableColumn<Book,Integer> total;
    public TableColumn<Book,Integer> available;
    public TableView usersTable;
    public TableColumn<Member,Integer> userId;
    public TableColumn<Member,String> firstName;
    public TableColumn<Member,String> lastName;
    public TableColumn<Member,String> username;
    public TableColumn<Member,String> password;
    public TableColumn<Member,Boolean> admin;
    public TextField firstNameSelected;
    public TextField lastNameSelected;
    public TextField usernameSelected;
    public TextField passwordSelected;
    public CheckBox adminSelected;
    public TextField titleSelected;
    public TextField authorSelected;
    public TextField yearSelected;
    public TextField genreSelected;
    public TextField totalSelected;
    public TextField availableSelected;
    public Button addRentalBttn;
    public TextField searchByName;
    public Button aboutBttn;
    public TextField searchByUsername;
    public TextField searchByTitle;
    public TextField searchByAuthor;
    public TextField searchByGenre;
    public BookManager bookManager = new BookManager();
    public MemberManager memberManager = new MemberManager();
    public Button allBooksBttn;
    public Button allUsersBttn;
    public Label checkRental;
    public Button mainPageBttn;
    public Button logOutBttn;
    public Button profile;
    private BookModel bookModel = new BookModel();
    private MemberModel memberModel = new MemberModel();
    private Integer idUser;
    private Integer idBook;
    private Member member;
    AddNewRentalController(Member m) {this.member = m;}
    @FXML
    public void initialize() throws LibraryException {
        bookId.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getId()).asObject();});
        title.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getTitle());});
        author.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getAuthor());});
        year.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getYearOfPublication());});
        genre.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getGenre());});
        total.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getTotalNumber()).asObject();});
        available.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getAvailableNumber()).asObject();});
        booksTable.setItems(FXCollections.observableList(bookManager.getAll()));
        userId.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleIntegerProperty(member.getId()).asObject();});
        firstName.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getFirstName());});
        lastName.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getLastName());});
        username.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getUsername());});
        password.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getPassword());});
        admin.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleBooleanProperty(member.isAdmin());});
        usersTable.setItems(FXCollections.observableList(memberManager.getAll()));
        searchByAuthor.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                BookManager b = new BookManager();
                try {
                    booksTable.setItems(FXCollections.observableList(b.searchByAuthor(n)));
                } catch (LibraryException e) {
                    e.printStackTrace();
                }
            }
        });
        searchByTitle.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                BookManager b = new BookManager();
                try {
                    booksTable.setItems(FXCollections.observableList(b.searchByTitle(n)));
                } catch (LibraryException e) {
                    e.printStackTrace();
                }
            }
        });
        searchByGenre.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                BookManager b = new BookManager();
                try {
                    booksTable.setItems(FXCollections.observableList(b.searchByGenre(n)));
                } catch (LibraryException e) {
                    e.printStackTrace();
                }
            }
        });
        //  tableId.getSelectionModel().getSelectedItem();
        booksTable.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
            if(o!=null){
                Book b = (Book) o;
                idBook = b.getId();
                titleSelected.textProperty().unbindBidirectional(bookModel.title);
                authorSelected.textProperty().unbindBidirectional((bookModel.author));
                yearSelected.textProperty().unbindBidirectional(bookModel.yearOfPublication);
                genreSelected.textProperty().unbindBidirectional(bookModel.genre);
                totalSelected.textProperty().unbindBidirectional(bookModel.totalNumber);
                availableSelected.textProperty().unbindBidirectional(bookModel.availableNumber);
            }
            Book b = (Book) n;
            if(b != null) {
                idBook = b.getId();
                bookModel.fromBook((Book) n);
                titleSelected.textProperty().bindBidirectional(bookModel.title);
                authorSelected.textProperty().bindBidirectional(bookModel.author);
                yearSelected.textProperty().bindBidirectional(bookModel.yearOfPublication);
                genreSelected.textProperty().bindBidirectional(bookModel.genre);
                totalSelected.textProperty().bindBidirectional(bookModel.totalNumber);
                availableSelected.textProperty().bindBidirectional(bookModel.availableNumber);
            }
        });
        searchByUsername.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                MemberManager m = new MemberManager();
                try {
                    usersTable.setItems(FXCollections.observableList(m.searchByUsername(n)));
                } catch (LibraryException e) {
                    e.printStackTrace();
                }
            }
        });
        searchByName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                MemberManager m = new MemberManager();
                try {
                    usersTable.setItems(FXCollections.observableList(m.searchByName(n)));
                } catch (LibraryException e) {
                    e.printStackTrace();
                }
            }
        });

        //  tableId.getSelectionModel().getSelectedItem();
        usersTable.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
            if(o!=null){
                Member m = (Member) o;
                idUser = m.getId();
                firstNameSelected.textProperty().unbindBidirectional(memberModel.firstName);
                lastNameSelected.textProperty().unbindBidirectional((memberModel.lastName));
                usernameSelected.textProperty().unbindBidirectional(memberModel.username);
                passwordSelected.textProperty().unbindBidirectional(memberModel.password);
                adminSelected.selectedProperty().unbindBidirectional(memberModel.admin);
            }
            Member m = (Member) n;
            if(m != null) {
                idUser = m.getId();
                memberModel.fromMember((Member) n);
                firstNameSelected.textProperty().bindBidirectional(memberModel.firstName);
                lastNameSelected.textProperty().bindBidirectional(memberModel.lastName);
                usernameSelected.textProperty().bindBidirectional(memberModel.username);
                passwordSelected.textProperty().bindBidirectional(memberModel.password);
                adminSelected.selectedProperty().bindBidirectional(memberModel.admin);
                RentalManager r = new RentalManager();
                Rental rent = null;
                try {
                    rent = r.checkUsersRental(idUser);
                } catch (LibraryException e) {
                    throw new RuntimeException(e);
                }
                if (rent == null) {
                    checkRental.setText(" The selected user currently has no rented books. ");
                } else {
                    int id = rent.getBookID();
                    Book book = null;
                    try {
                        book = bookManager.searchById(id);
                    } catch (LibraryException e) {
                        throw new RuntimeException(e);
                    }
                    checkRental.setText(" The selected user currently has the book \"" + book.getTitle() + "\" by author "
                            + book.getAuthor() + ". ");
                }
            }
        });
    }
    public void addRental(ActionEvent actionEvent) throws LibraryException {
        Member selectedMember = (Member) usersTable.getSelectionModel().getSelectedItem();
        Book selectedBook = (Book) booksTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null || selectedBook == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Adding error!");
            ScrollPane scrollPane = new ScrollPane(new Label("Please select from the table the user you want to rent the book to and the book you want to rent to the selected user!"));
            scrollPane.setFitToWidth(true);
            alert.getDialogPane().setContent(scrollPane);
            alert.showAndWait();
            return;
        }
        RentalManager r = new RentalManager();
        try {
            r.rentABook(idUser, idBook, selectedBook.getTitle(), selectedBook.getAuthor());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("The book has been successfully rented!");
            alert.showAndWait();
            firstNameSelected.setText("");
            lastNameSelected.setText("");
            usernameSelected.setText("");
            passwordSelected.setText("");
            adminSelected.setSelected(false);
            titleSelected.setText("");
            authorSelected.setText("");
            genreSelected.setText("");
            yearSelected.setText("");
            totalSelected.setText("");
            availableSelected.setText("");
            bookId.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getId()).asObject();});
            title.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getTitle());});
            author.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getAuthor());});
            year.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getYearOfPublication());});
            genre.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getGenre());});
            total.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getTotalNumber()).asObject();});
            available.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getAvailableNumber()).asObject();});
            booksTable.setItems(FXCollections.observableList(bookManager.getAll()));
        } catch (LibraryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("It is not possible to rent the selected book to the desired user!");
            alert.setContentText("Check if the selected user already has a rented book. If he has, he needs to return it to rent a new one.");
            alert.showAndWait();
            return;
        }
    }

    public void allBooks(ActionEvent actionEvent) throws LibraryException {
        bookId.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getId()).asObject();});
        title.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getTitle());});
        author.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getAuthor());});
        year.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getYearOfPublication());});
        genre.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleStringProperty(book.getGenre());});
        total.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getTotalNumber()).asObject();});
        available.setCellValueFactory(cellData->{Book book=cellData.getValue(); return new SimpleIntegerProperty(book.getAvailableNumber()).asObject();});
        booksTable.setItems(FXCollections.observableList(bookManager.getAll()));
    }

    public void allUsers(ActionEvent actionEvent) throws LibraryException {
        userId.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleIntegerProperty(member.getId()).asObject();});
        firstName.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getFirstName());});
        lastName.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getLastName());});
        username.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getUsername());});
        password.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getPassword());});
        admin.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleBooleanProperty(member.isAdmin());});
        usersTable.setItems(FXCollections.observableList(memberManager.getAll()));
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
    public class MemberModel {
        public SimpleStringProperty firstName = new SimpleStringProperty("");
        public SimpleStringProperty lastName = new SimpleStringProperty("");
        public SimpleStringProperty username = new SimpleStringProperty("");
        public SimpleStringProperty password = new SimpleStringProperty("");
        public SimpleBooleanProperty admin = new SimpleBooleanProperty(false);

        public void fromMember (Member m){
            this.firstName.set(m.getFirstName());
            this.lastName.set(m.getLastName());
            this.username.set(m.getUsername());
            this.password.set(m.getPassword());
            this.admin.set(m.isAdmin());
        }

        public Member toMember(){
            Member m = new Member();
            m.setFirstName(this.firstName.getValue());
            m.setLastName(this.lastName.getValue());
            m.setUsername(this.username.getValue());
            m.setPassword(this.password.getValue());
            m.setAdmin(this.admin.getValue());
            return m;
        }
    }

}
