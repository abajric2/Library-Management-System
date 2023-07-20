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
import javafx.scene.control.*;

import java.util.Optional;

public class ManageUsersController {
    public Button allUsersId;
    public TextField byUsernameId;
    public TextField firstNameUpdt;
    public TextField lastNameUpdt;
    public TextField usernameUpdt;
    public TextField passwordUpdt;
    public CheckBox adminUpdt;
    public Button updateBttn;
    public Button deleteBttn;
    public TableView tableId;
    public TableColumn<Member,Integer> id;
    public TableColumn<Member,String> firstName;
    public TableColumn<Member,String> lastName;
    public TableColumn<Member,String> username;
    public TableColumn<Member,String> password;
    public TableColumn<Member,Boolean> admin;
    public TextField firstNameAdd;
    public TextField lastNameAdd;
    public TextField usernameAdd;
    public TextField passwordAdd;
    public CheckBox adminAdd;
    public Button addBttn;
    public MemberManager manager = new MemberManager();
    public TextField byNameId;
    public Label rentalInfoId;
    public Button manageRentals;
    private MemberModel model = new MemberModel();
    private Integer idUpdate;

    @FXML
    public void initialize() throws LibraryException {
        id.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleIntegerProperty(member.getId()).asObject();});
        firstName.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getFirstName());});
        lastName.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getLastName());});
        username.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getUsername());});
        password.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getPassword());});
        admin.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleBooleanProperty(member.isAdmin());});
        tableId.setItems(FXCollections.observableList(manager.getAll()));
        byUsernameId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                MemberManager m = new MemberManager();
                try {
                    tableId.setItems(FXCollections.observableList(m.searchByUsername(n)));
                } catch (LibraryException e) {
                    e.printStackTrace();
                }
            }
        });
        byNameId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                MemberManager m = new MemberManager();
                try {
                    tableId.setItems(FXCollections.observableList(m.searchByName(n)));
                } catch (LibraryException e) {
                    e.printStackTrace();
                }
            }
        });

        //  tableId.getSelectionModel().getSelectedItem();
        tableId.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
            if(o!=null){
                Member m = (Member) o;
                idUpdate = m.getId();
                firstNameUpdt.textProperty().unbindBidirectional(model.firstName);
                lastNameUpdt.textProperty().unbindBidirectional((model.lastName));
                usernameUpdt.textProperty().unbindBidirectional(model.username);
                passwordUpdt.textProperty().unbindBidirectional(model.password);
                adminUpdt.selectedProperty().unbindBidirectional(model.admin);
            }
            Member m = (Member) n;
            if(m != null) {
                idUpdate = m.getId();
                model.fromMember((Member) n);
                firstNameUpdt.textProperty().bindBidirectional(model.firstName);
                lastNameUpdt.textProperty().bindBidirectional(model.lastName);
                usernameUpdt.textProperty().bindBidirectional(model.username);
                passwordUpdt.textProperty().bindBidirectional(model.password);
                adminUpdt.selectedProperty().bindBidirectional(model.admin);
            }
            if(n != null) {
                RentalManager r = new RentalManager();
                Member memb = (Member) n;
                Rental rent = null;
                try {
                    rent = r.checkUsersRental(memb.getId());
                } catch (LibraryException e) {
                    throw new RuntimeException(e);
                }
                if(rent == null) {
                    rentalInfoId.setText(memb.getUsername() + " currently has no rented books.");
                }
                else {
                    int id = rent.getBookID();
                    BookManager b = new BookManager();
                    Book book = null;
                    try {
                        book = b.searchById(id);
                    } catch (LibraryException e) {
                        throw new RuntimeException(e);
                    }
                    rentalInfoId.setText(memb.getUsername() + " currently has a book \"" + book.getTitle() + "\" by author "
                            + book.getAuthor() + ".");
                }
            }
         });

    }
    public void allUsers(ActionEvent actionEvent) throws LibraryException {
        id.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleIntegerProperty(member.getId()).asObject();});
        firstName.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getFirstName());});
        lastName.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getLastName());});
        username.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getUsername());});
        password.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleStringProperty(member.getPassword());});
        admin.setCellValueFactory(cellData->{Member member=cellData.getValue(); return new SimpleBooleanProperty(member.isAdmin());});
        tableId.setItems(FXCollections.observableList(manager.getAll()));
    }

    public void searchByUsername(ActionEvent actionEvent) {
    }

    public void updateAction(ActionEvent actionEvent) throws LibraryException {
        if(firstNameUpdt.getText().isEmpty() || lastNameUpdt.getText().isEmpty() || usernameUpdt.getText().isEmpty() ||
                passwordUpdt.getText().isEmpty()) {
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
            alert.setContentText("Choose the member you want to edit in the table!");
            alert.showAndWait();
            return;
        }
        Member m = new Member();
        m.setId(idUpdate);
        m.setFirstName(model.firstName.get());
        m.setLastName(model.lastName.get());
        m.setUsername(model.username.get());
        m.setPassword(model.password.get());
        m.setAdmin(model.admin.get());
        try {
            manager.update(m);
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
            if(m.getPassword().length() < 8) alert.setContentText("Password must be at least 8 characters long!");
            else alert.setContentText("An error occurred while attempting to update this user. Check if someone is already using the username you entered!");
            alert.showAndWait();
            idUpdate = null;
        }
    }

    public void deleteAction(ActionEvent actionEvent) {
        if (idUpdate == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Delete error!");
            alert.setContentText("Choose the member you want to delete in the table!");
            alert.showAndWait();
            return;
        }

        Member selectedItem = (Member) tableId.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Delete error!");
            alert.setContentText("Choose the member you want to delete in the table!");
            alert.showAndWait();
            return;
        }

        String usernameToDelete = selectedItem.getUsername();

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation Dialog");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete the member with username: " + usernameToDelete + "?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Member m = new Member();
            m.setId(idUpdate);
            m.setFirstName(model.firstName.get());
            m.setLastName(model.lastName.get());
            m.setUsername(model.username.get());
            m.setPassword(model.password.get());
            m.setAdmin(model.admin.get());
            try {
                manager.delete(m);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Successfully deleted!");
                alert.showAndWait();
                tableId.setItems(FXCollections.observableList(manager.getAll()));
                idUpdate = null;
            } catch (LibraryException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Delete error!");
                alert.setContentText("You can't delete this member! Check if the user currently has a rented book. If yes, the book must be returned before deletion.");
                alert.showAndWait();
                idUpdate = null;
            }
        }
    }

    public void addAction(ActionEvent actionEvent) {
        if(firstNameAdd.getText().isEmpty() || lastNameAdd.getText().isEmpty() || usernameAdd.getText().isEmpty() ||
                passwordAdd.getText().isEmpty()) {
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
        Member m = new Member();
        m.setId(1);
        m.setFirstName(firstNameAdd.getText());
        m.setLastName(lastNameAdd.getText());
        m.setUsername(usernameAdd.getText());
        m.setPassword(passwordAdd.getText());
        m.setAdmin(adminAdd.isSelected());
        try {
            manager.add(m);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Successfully added!");
            alert.showAndWait();
            tableId.setItems(FXCollections.observableList(manager.getAll()));
        } catch (LibraryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("You can't add member!");
            if(m.getPassword().length() < 8) alert.setContentText("Password must be at least 8 characters long!");
            else alert.setContentText("An error occurred while attempting to update this user. Check if someone is already using the username you entered!");
            alert.showAndWait();
        }
    }

    public void manageRentals(ActionEvent actionEvent) {
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
