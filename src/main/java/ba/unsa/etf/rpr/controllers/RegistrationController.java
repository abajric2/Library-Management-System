package ba.unsa.etf.rpr.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrationController {
    public TextField firstNameId;
    public TextField lastNameId;
    public TextField usernameId;
    public PasswordField passwordId;
    public PasswordField confirmPasswordId;
    public Button signUpId;
    @FXML
    public void initialize() {
        passwordId.getStyleClass().add("/css/registration.css/poljeNijeIspravno");
        passwordId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String o, String n) {
                if (n.length() < 8) {
                    passwordId.getStyleClass().add("/css/registration.css/poljeNijeIspravno");
                } else {
                    passwordId.getStyleClass().removeAll("/css/registration.css/poljeNijeIspravno");
                }
            }
        });
    }

    public void signUp(ActionEvent actionEvent) {
    }
}
