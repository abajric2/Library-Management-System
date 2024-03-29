package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.business.MemberManager;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class RegistrationController {
    public TextField firstNameId;
    public TextField lastNameId;
    public TextField usernameId;
    public PasswordField passwordId;
    public PasswordField confirmPasswordId;
    public Button signUpId;
    public Label chckPasswordLngth;
    public Label chckPasswordSame;
    public Label checkFirstName;
    public Label checkLastName;
    public Label checkUsername;
    public Hyperlink about;
    public Hyperlink logIn;

    @FXML
 public void initialize() {

     passwordId.textProperty().addListener(new ChangeListener<String>() {
         @Override
         public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
             if(!n.matches("^[^\\s]+$")) {
                 chckPasswordLngth.setText("The password can't be empty, or contain spaces or other blank characters.");
             } else if (n.length() >= 8 && n.length() <= 128) {
                 chckPasswordLngth.setText("");
             } else {
                 chckPasswordLngth.setText("Password must be between 8 and 128 characters long!");
             }
             if(!confirmPasswordId.getText().isEmpty() && passwordId.getText().equals(confirmPasswordId.getText())) {
                 chckPasswordSame.setText("");
             } else {
                 if(confirmPasswordId.getText().isEmpty()) chckPasswordSame.setText("");
                 else chckPasswordSame.setText("The password does not match!");
             }
         }
     });
     confirmPasswordId.textProperty().addListener(new ChangeListener<String>() {
         @Override
         public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
             if (confirmPasswordId.getText().equals(passwordId.getText())) {
                 chckPasswordSame.setText("");
             } else {
                 chckPasswordSame.setText("The password does not match!");
             }
         }
     });
        firstNameId.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() < 1) {
                checkFirstName.setText("This field can't be empty");
            } else if (!newValue.matches("^[a-zA-Z-]+(\\s[a-zA-Z-]+)*$")) {
                checkFirstName.setText("Only letters, dashes and spaces. Spaces can only be between two sets of characters.");
            } else if (newValue.length() > 30) {
                checkFirstName.setText("First name can't be longer than 30 characters!");
            } else {
                checkFirstName.setText("");
            }
        });
        lastNameId.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() < 1) {
                checkLastName.setText("This field can't be empty");
            } else if (!newValue.matches("^[a-zA-Z-]+(\\s[a-zA-Z-]+)*$")) {
                checkLastName.setText("Only letters, dashes and spaces. Spaces can only be between two sets of characters.");
            } else if (newValue.length() > 50) {
                checkLastName.setText("Last name can't be longer than 50 characters!");
            } else {
                checkLastName.setText("");
            }
        });
        usernameId.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() < 1) {
                checkUsername.setText("This field can't be empty");
            } else if (!newValue.isEmpty() && !newValue.matches("^[a-zA-Z0-9_.-]+$")) {
                checkUsername.setText("Username can only contain letters, numbers, underscores, dots, and dashes");
            } else if (newValue.length() < 3 || newValue.length() > 30) {
                checkUsername.setText("Username length must be between 3 and 30 characters");
            } else {
                checkUsername.setText("");
            }
        });
 }


    public void signUp(ActionEvent actionEvent) throws LibraryException, IOException {
        if(firstNameId.getText().isEmpty() || lastNameId.getText().isEmpty() || usernameId.getText().isEmpty() ||
                passwordId.getText().isEmpty() || confirmPasswordId.getText().isEmpty()) {
            if(firstNameId.getText().isEmpty()) checkFirstName.setText("This field can't be empty");
            if(lastNameId.getText().isEmpty()) checkLastName.setText("This field can't be empty");
            if(usernameId.getText().isEmpty()) checkUsername.setText("This field can't be empty");
            if(passwordId.getText().isEmpty()) chckPasswordLngth.setText("This field can't be empty");
            if(confirmPasswordId.getText().isEmpty()) chckPasswordSame.setText("This field can't be empty");
            return;
        }
        if(!checkFirstName.getText().isEmpty() || !checkLastName.getText().isEmpty() || !checkUsername.getText().isEmpty()
            || !chckPasswordLngth.getText().isEmpty() || !chckPasswordSame.getText().isEmpty()) {
            return;
        }
        Member member = new Member(firstNameId.getText(), lastNameId.getText(), usernameId.getText(),
                passwordId.getText(), false);
        MemberManager m = new MemberManager();
        try {
            m.add(member);
        } catch (LibraryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Sign up error");
            if(e.getMessage().equals("Someone is already using this username")) alert.setContentText("Someone is already using this username");
            else alert.setContentText("An error occurred while signing up");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("You have successfully created an account! Sign in to continue!");
        alert.showAndWait();
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
        AboutController controller = new AboutController(null);
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
    public void logIn (ActionEvent actionEvent) throws IOException {
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
