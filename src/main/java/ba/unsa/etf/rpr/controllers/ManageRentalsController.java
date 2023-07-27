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
import javafx.beans.property.SimpleObjectProperty;
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
import java.time.LocalDate;
import java.util.*;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class ManageRentalsController {
    public Button deadlineExceedingsId;
    public DatePicker datePickerId;
    public TextField firstNameId;
    public TextField lastNameId;
    public TextField usernameId;
    public TextField passwordId;
    public CheckBox adminId;
    public TextField titleId;
    public TextField authorId;
    public TextField yearId;
    public TextField genreId;
    public TextField totalId;
    public TextField availableId;
    public Button deleteRentalId;
    public Button aboutBttn;
    public TableView tableId;
    public TableColumn<Rental,Integer> id;
    public TableColumn<Rental,Integer> book;
    public TableColumn<Rental,Integer> member;
    public TableColumn<Rental, Date> rentDate;
    public TableColumn<Rental,Date> returnDeadline;
    public RentalManager manager = new RentalManager();
    public Button allRentalsId;
    public Button mainPageBttn;
    public Button logOutBttn;
    public Button profile;
    private RentalModel model = new RentalModel();
    private Integer idUpdate;
    private Member logedMember;
    ManageRentalsController(Member m) {this.logedMember = m;}

    @FXML
    public void initialize() throws LibraryException {
        id.setCellValueFactory(cellData->{Rental rental=cellData.getValue(); return new SimpleIntegerProperty(rental.getId()).asObject();});
        book.setCellValueFactory(cellData->{Rental rental=cellData.getValue(); return new SimpleIntegerProperty(rental.getBookID()).asObject();});
        member.setCellValueFactory(cellData->{Rental rental=cellData.getValue(); return new SimpleIntegerProperty(rental.getMemberID()).asObject();});
        rentDate.setCellValueFactory(cellData->{Rental rental=cellData.getValue(); return new SimpleObjectProperty<Date>(rental.getRentDate());});
        returnDeadline.setCellValueFactory(cellData->{Rental rental=cellData.getValue(); return new SimpleObjectProperty<Date>(rental.getReturnDeadline());});
        tableId.setItems(FXCollections.observableList(manager.getAll()));

        //  tableId.getSelectionModel().getSelectedItem();
        tableId.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
            if(o!=null){
                Rental r = (Rental) o;
                idUpdate = r.getId();
                titleId.textProperty().unbindBidirectional(model.bookTitle);
                authorId.textProperty().unbindBidirectional((model.bookAuthor));
                yearId.textProperty().unbindBidirectional(model.bookYear);
                genreId.textProperty().unbindBidirectional(model.bookGenre);
                totalId.textProperty().unbindBidirectional(model.bookTotalNumber);
                availableId.textProperty().unbindBidirectional(model.bookAvailableNumber);
                firstNameId.textProperty().unbindBidirectional(model.memberFirstName);
                lastNameId.textProperty().unbindBidirectional(model.memberLastName);
                usernameId.textProperty().unbindBidirectional(model.memberUsername);
                passwordId.textProperty().unbindBidirectional(model.memberPassword);
                adminId.selectedProperty().unbindBidirectional(model.isMemberAdmin);
            }
            Rental r = (Rental) n;
            if(r != null) {
                idUpdate = r.getId();
                try {
                    model.fromRental((Rental) n);
                } catch (LibraryException e) {
                    throw new RuntimeException(e);
                }
                titleId.textProperty().bindBidirectional(model.bookTitle);
                authorId.textProperty().bindBidirectional((model.bookAuthor));
                yearId.textProperty().bindBidirectional(model.bookYear);
                genreId.textProperty().bindBidirectional(model.bookGenre);
                totalId.textProperty().bindBidirectional(model.bookTotalNumber);
                availableId.textProperty().bindBidirectional(model.bookAvailableNumber);
                firstNameId.textProperty().bindBidirectional(model.memberFirstName);
                lastNameId.textProperty().bindBidirectional(model.memberLastName);
                usernameId.textProperty().bindBidirectional(model.memberUsername);
                passwordId.textProperty().bindBidirectional(model.memberPassword);
                adminId.selectedProperty().bindBidirectional(model.isMemberAdmin);
            }
        });
        datePickerId.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                // Ovdje ćemo dodati kod koji će se izvršiti kada se promijeni datum u datePickeru

                // Prvo dohvatimo novi izabrani datum
                if(newValue != null) {
                    java.sql.Date selectedDate = java.sql.Date.valueOf(newValue);

                    // Pozivamo metodu koja će obrađivati promjenu datuma
                    id.setCellValueFactory(cellData -> {
                        Rental rental = cellData.getValue();
                        return new SimpleIntegerProperty(rental.getId()).asObject();
                    });
                    book.setCellValueFactory(cellData -> {
                        Rental rental = cellData.getValue();
                        return new SimpleIntegerProperty(rental.getBookID()).asObject();
                    });
                    member.setCellValueFactory(cellData -> {
                        Rental rental = cellData.getValue();
                        return new SimpleIntegerProperty(rental.getMemberID()).asObject();
                    });
                    rentDate.setCellValueFactory(cellData -> {
                        Rental rental = cellData.getValue();
                        return new SimpleObjectProperty<Date>(rental.getRentDate());
                    });
                    returnDeadline.setCellValueFactory(cellData -> {
                        Rental rental = cellData.getValue();
                        return new SimpleObjectProperty<Date>(rental.getReturnDeadline());
                    });
                    try {
                        tableId.setItems(FXCollections.observableList(manager.searchByReturnDeadline(selectedDate)));
                    } catch (LibraryException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
    public void deadlineExceedings(ActionEvent actionEvent) throws LibraryException {
        id.setCellValueFactory(cellData->{Rental rental=cellData.getValue(); return new SimpleIntegerProperty(rental.getId()).asObject();});
        book.setCellValueFactory(cellData->{Rental rental=cellData.getValue(); return new SimpleIntegerProperty(rental.getBookID()).asObject();});
        member.setCellValueFactory(cellData->{Rental rental=cellData.getValue(); return new SimpleIntegerProperty(rental.getMemberID()).asObject();});
        rentDate.setCellValueFactory(cellData->{Rental rental=cellData.getValue(); return new SimpleObjectProperty<Date>(rental.getRentDate());});
        returnDeadline.setCellValueFactory(cellData->{Rental rental=cellData.getValue(); return new SimpleObjectProperty<Date>(rental.getReturnDeadline());});
        tableId.setItems(FXCollections.observableList(manager.getDeadlineExceedings()));
    }

    public void deleteRental(ActionEvent actionEvent) {
        if (idUpdate == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Delete error!");
            alert.setContentText("Choose the rental you want to delete from the table!");
            alert.showAndWait();
            return;
        }

        Rental selectedItem = (Rental) tableId.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Delete error!");
            alert.setContentText("Choose the rental you want to delete from the table!");
            alert.showAndWait();
            return;
        }

       // String usernameToDelete = selectedItem.getUsername();

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation Dialog");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete the rental for the user " + usernameId.getText() + ", who currently has the book \"" + titleId.getText() + "\", from the author \"" + authorId.getText() + "\"?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Rental r = new Rental();
            r.setId(idUpdate);
            r.setBookID(model.book.get());
            r.setMemberID(model.member.get());
            r.setRentDate(model.rentDate.get());
            r.setReturnDeadline(model.returnDeadline.get());
            try {
                manager.delete(r);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Successfully deleted!");
                alert.showAndWait();
                titleId.setText("");
                authorId.setText("");
                genreId.setText("");
                yearId.setText("");
                totalId.setText("");
                availableId.setText("");
                firstNameId.setText("");
                lastNameId.setText("");
                usernameId.setText("");
                passwordId.setText("");
                adminId.setSelected(false);
                tableId.setItems(FXCollections.observableList(manager.getAll()));
                idUpdate = null;
            } catch (LibraryException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Delete error!");
                alert.setContentText("You can't delete this rental!");
                alert.showAndWait();
                idUpdate = null;
            }
        }
    }

    public void addRental(ActionEvent actionEvent) {
    }

    public void allRentals(ActionEvent actionEvent) throws LibraryException {
        id.setCellValueFactory(cellData->{Rental rental=cellData.getValue(); return new SimpleIntegerProperty(rental.getId()).asObject();});
        book.setCellValueFactory(cellData->{Rental rental=cellData.getValue(); return new SimpleIntegerProperty(rental.getBookID()).asObject();});
        member.setCellValueFactory(cellData->{Rental rental=cellData.getValue(); return new SimpleIntegerProperty(rental.getMemberID()).asObject();});
        rentDate.setCellValueFactory(cellData->{Rental rental=cellData.getValue(); return new SimpleObjectProperty<Date>(rental.getRentDate());});
        returnDeadline.setCellValueFactory(cellData->{Rental rental=cellData.getValue(); return new SimpleObjectProperty<Date>(rental.getReturnDeadline());});
        tableId.setItems(FXCollections.observableList(manager.getAll()));
    }

    public class RentalModel {
        public SimpleIntegerProperty book = new SimpleIntegerProperty(0);
        public SimpleIntegerProperty member = new SimpleIntegerProperty(0);
        public SimpleObjectProperty<java.sql.Date> rentDate = new SimpleObjectProperty<java.sql.Date>();
        public SimpleObjectProperty<java.sql.Date> returnDeadline = new SimpleObjectProperty<java.sql.Date>();
        public SimpleStringProperty bookTitle = new SimpleStringProperty("");
        public SimpleStringProperty bookAuthor = new SimpleStringProperty("");
        public SimpleStringProperty bookYear = new SimpleStringProperty("");
        public SimpleStringProperty bookGenre = new SimpleStringProperty("");
        public SimpleStringProperty bookTotalNumber = new SimpleStringProperty("");
        public SimpleStringProperty bookAvailableNumber = new SimpleStringProperty("");
        public SimpleStringProperty memberFirstName = new SimpleStringProperty("");
        public SimpleStringProperty memberLastName = new SimpleStringProperty("");
        public SimpleStringProperty memberUsername = new SimpleStringProperty("");
        public SimpleStringProperty memberPassword = new SimpleStringProperty("");
        public SimpleBooleanProperty isMemberAdmin = new SimpleBooleanProperty(false);
        private BookManager bookManager = new BookManager();
        private MemberManager memberManager = new MemberManager();
        public void fromRental (Rental r) throws LibraryException {
            this.book.set(r.getBookID());
            this.member.set(r.getMemberID());
            this.rentDate.set(r.getRentDate());
            this.returnDeadline.set(r.getReturnDeadline());
            this.bookTitle.set(bookManager.searchById(r.getBookID()).getTitle());
            this.bookAuthor.set(bookManager.searchById(r.getBookID()).getAuthor());
            this.bookYear.set(bookManager.searchById(r.getBookID()).getYearOfPublication());
            this.bookGenre.set(bookManager.searchById(r.getBookID()).getGenre());
            this.bookTotalNumber.set(Integer.toString(bookManager.searchById(r.getBookID()).getTotalNumber()));
            this.bookAvailableNumber.set(Integer.toString(bookManager.searchById(r.getBookID()).getAvilableNumber()));
            this.memberFirstName.set(memberManager.searchById(r.getMemberID()).getFirstName());
            this.memberLastName.set(memberManager.searchById(r.getMemberID()).getLastName());
            this.memberUsername.set(memberManager.searchById(r.getMemberID()).getUsername());
            this.memberPassword.set(memberManager.searchById(r.getMemberID()).getPassword());
            this.isMemberAdmin.set(memberManager.searchById(r.getMemberID()).isAdmin());
        }

        public Rental toRental(){
            Rental r = new Rental();
            r.setBookID(this.book.getValue());
            r.setMemberID(this.member.getValue());
            r.setRentDate((java.sql.Date) this.rentDate.getValue());
            r.setReturnDeadline((java.sql.Date) this.returnDeadline.getValue());
            return r;
        }
    }
    public void mainPage(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminMainWindow.fxml"));
        AdminMainWindowController controller = new AdminMainWindowController(logedMember);
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
    public void about(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/about.fxml"));
        AboutController controller = new AboutController(logedMember);
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
        ProfileController controller = new ProfileController(logedMember);
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
}
