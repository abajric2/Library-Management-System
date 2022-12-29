package ba.unsa.etf.rpr.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class LoginController {

    public TextField usernameId;
    public PasswordField passwordId;
    public Button signInBttnId;
    public Hyperlink signUpId;

    public void signUp(ActionEvent actionEvent) throws Exception {
        /*Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registration.fxml"));
        //    Parent root = FXMLLoader.load(getClass().getResource("/fxml/starilogin.fxml"));
        RegistrationController controller = new RegistrationController();
        loader.setController(controller);
        stage.setTitle("Sign up");
        stage.setScene(new Scene((Parent) loader.load(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();*/
        Stage myStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/registration.fxml"));
        myStage.setTitle("Sign up");
        myStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.setResizable(false);
        myStage.show();

    }

    public void signIn(ActionEvent actionEvent) {
    }

}
