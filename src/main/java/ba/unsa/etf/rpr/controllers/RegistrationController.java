package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dao.MemberDaoSQLImpl;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.Style;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrationController {
    public TextField firstNameId;
    public TextField lastNameId;
    public TextField usernameId;
    public PasswordField passwordId;
    public PasswordField confirmPasswordId;
    public Button signUpId;

 @FXML
 public void initialize() {
     passwordId.textProperty().addListener(new ChangeListener<String>() {
         @Override
         public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
             if (n.length() >= 8) {
                 passwordId.setStyle("-fx-background-color: yellowgreen;");

             } else {
                 passwordId.setStyle("-fx-background-color: red;");
             }
             if(!confirmPasswordId.getText().isEmpty() && passwordId.getText().equals(confirmPasswordId.getText())) {
                 confirmPasswordId.setStyle("-fx-background-color: yellowgreen;");
             } else {
                 if(!confirmPasswordId.getText().isEmpty()) confirmPasswordId.setStyle("-fx-background-color: red;");
             }
         }
     });
     confirmPasswordId.textProperty().addListener(new ChangeListener<String>() {
         @Override
         public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
             // System.out.println(o + " " + n);
             if (confirmPasswordId.getText().equals(passwordId.getText())) {
                 confirmPasswordId.setStyle("-fx-background-color: yellowgreen;");
             } else {
                 confirmPasswordId.setStyle("-fx-background-color: red;");
             }
         }
     });
 }


    public void signUp(ActionEvent actionEvent) throws LibraryException {
        if(firstNameId.getText().isEmpty() || lastNameId.getText().isEmpty() || usernameId.getText().isEmpty() ||
                passwordId.getText().isEmpty() || confirmPasswordId.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Fill in all fields!");
            alert.setContentText("You must fill in all the fields provided!");
            alert.showAndWait();
            return;
        }
        if(passwordId.getText().length() < 8) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Password error");
            alert.setContentText("Password must be at least 8 characters long!");
            alert.showAndWait();
            return;
        }
        if(!passwordId.getText().equals(confirmPasswordId.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Password error");
            alert.setContentText("In the confirm password field, you must enter the same password that you entered in the password field!");
            alert.showAndWait();
            return;
        }
        Member member = new Member(1, firstNameId.getText(), lastNameId.getText(), usernameId.getText(),
                passwordId.getText(), false);
        MemberDaoSQLImpl m = new MemberDaoSQLImpl();
        Member check = m.add(member);
        if(check == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Username or password error");
            alert.setContentText("Someone is already using this username or password!");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("You have successfully created an account! Sign in to continue!");
        alert.showAndWait();
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }
}
