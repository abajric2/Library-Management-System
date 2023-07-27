package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.business.MemberManager;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProfileController {
    public Button editBttn;
    public TextField firstName;
    public Label checkFirstName;
    public TextField lastName;
    public Label checkLastName;
    public TextField username;
    public Label checkUsername;
    public TextField password;
    public Label checkPassword;
    public CheckBox isAdmin;
    public CheckBox showPassword;
    public PasswordField passwordField;
    public Button updateBttn;
    public Button deleteBttn;
    public Button mainPageBttn;
    public Button logOutBttn;
    private MemberManager manager = new MemberManager();
    private Member member;
    ProfileController(Member m) {this.member = m;}
    @FXML
    public void initialize() {
        firstName.setText(member.getFirstName());
        firstName.setEditable(false);
        lastName.setText(member.getLastName());
        lastName.setEditable(false);
        username.setText(member.getUsername());
        username.setEditable(false);
        password.setText(member.getPassword());
        password.setEditable(false);
        passwordField.setText(member.getPassword());
        passwordField.setEditable(false);
        updateBttn.setVisible(false);
        isAdmin.setSelected(member.isAdmin());
        firstName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z -]*")) {
                checkFirstName.setText("Only letters, spaces and dashes are allowed.");
            } else {
                checkFirstName.setText("");
            }
        });
        lastName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z -]*")) {
                checkLastName.setText("Only letters, spaces and dashes are allowed.");
            } else {
                checkLastName.setText("");
            }
        });
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 8) {
                checkPassword.setText("Password must be at least 8 characters long.");
            } else {
                checkPassword.setText("");
            }
        });
        password.textProperty().bindBidirectional(passwordField.textProperty());

        showPassword.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                if (new_val) {
                    password.setVisible(true);
                    password.setManaged(true);
                    passwordField.setVisible(false);
                    passwordField.setManaged(false);
                } else {
                    password.setVisible(false);
                    password.setManaged(false);
                    passwordField.setVisible(true);
                    passwordField.setManaged(true);
                }
            }
        });
    }

    public void editAction(ActionEvent actionEvent) {
        firstName.setEditable(true);
        lastName.setEditable(true);
        username.setEditable(true);
        password.setEditable(true);
        passwordField.setEditable(true);
        updateBttn.setVisible(true);
    }

    public void updateAction(ActionEvent actionEvent) {
        if(firstName.getText().isEmpty() || lastName.getText().isEmpty() || username.getText().isEmpty() ||
                password.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Fill in all fields!");
            alert.setContentText("You must fill in all the fields provided!");
            alert.showAndWait();
            return;
        }
        Member m = new Member();
        m.setId(member.getId());
        m.setFirstName(firstName.getText());
        m.setLastName(lastName.getText());
        m.setUsername(username.getText());
        m.setPassword(password.getText());
        m.setAdmin(member.isAdmin());
        try {
            manager.update(m);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Successfully updated!");
            alert.showAndWait();
            firstName.setEditable(false);
            lastName.setEditable(false);
            username.setEditable(false);
            password.setEditable(false);
            passwordField.setEditable(false);
            updateBttn.setVisible(false);
        } catch (LibraryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Update error!");
            if(!m.getFirstName().matches("[a-zA-Z -]*") || !m.getLastName().matches("[a-zA-Z -]*") || m.getPassword().length() < 8) alert.setContentText("Check that the values you entered are of a valid type.");
            else alert.setContentText("Check if someone is already using the username you entered!");
            alert.showAndWait();
            //   idUpdate = null;
        }
    }

    public void deleteAction(ActionEvent actionEvent) {
    }

    public void mainPage(ActionEvent actionEvent) {
    }

    public void logOut(ActionEvent actionEvent) {
    }
}
