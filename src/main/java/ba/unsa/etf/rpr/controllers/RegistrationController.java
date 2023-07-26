package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.business.MemberManager;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.Style;
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

    @FXML
 public void initialize() {
     passwordId.textProperty().addListener(new ChangeListener<String>() {
         @Override
         public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
             if (n.length() >= 8) {
                 passwordId.setStyle("-fx-background-color: yellowgreen;");
                 chckPasswordLngth.setText("");
             } else {
                 chckPasswordLngth.setText("Password must be at least 8 characters long!");
                 passwordId.setStyle("-fx-background-color: red;");
             }
             if(!confirmPasswordId.getText().isEmpty() && passwordId.getText().equals(confirmPasswordId.getText())) {
                 chckPasswordSame.setText("");
                 confirmPasswordId.setStyle("-fx-background-color: yellowgreen;");
             } else {
                 if(!confirmPasswordId.getText().isEmpty()) confirmPasswordId.setStyle("-fx-background-color: red;");
                 if(confirmPasswordId.getText().isEmpty()) chckPasswordSame.setText("");
                 else chckPasswordSame.setText("The password does not match!");
             }
         }
     });
     confirmPasswordId.textProperty().addListener(new ChangeListener<String>() {
         @Override
         public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
             // System.out.println(o + " " + n);
             if (confirmPasswordId.getText().equals(passwordId.getText())) {
                 chckPasswordSame.setText("");
                 confirmPasswordId.setStyle("-fx-background-color: yellowgreen;");
             } else {
                 chckPasswordSame.setText("The password does not match!");
                 confirmPasswordId.setStyle("-fx-background-color: red;");
             }
         }
     });
 }


    public void signUp(ActionEvent actionEvent) throws LibraryException, IOException {
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
        MemberManager m = new MemberManager();
        try {
            Member check = m.add(member);
        } catch (LibraryException e) {
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
