package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.business.MemberManager;
import ba.unsa.etf.rpr.dao.MemberDaoSQLImpl;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    public void signIn(ActionEvent actionEvent) throws IOException, LibraryException {
        if(usernameId.getText().isEmpty() || passwordId.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Fill in all fields!");
            alert.setContentText("You must fill in all the fields provided!");
            alert.showAndWait();
            return;
        }
        MemberManager mimp = new MemberManager();
        Member m = new Member();
        try {
            m = mimp.searchByUserameandPassword(usernameId.getText(), passwordId.getText());
        } catch (LibraryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("User not found!");
            alert.setContentText("The user with the entered data was not found!");
            alert.showAndWait();
            return;
        }
        if(m.isAdmin()) {
            Stage myStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminMainWindow.fxml"));
            AdminMainWindowController controller = new AdminMainWindowController(m);
            loader.setController(controller);
            myStage.setTitle("Main window");
            myStage.setScene(new Scene(loader.<Parent>load(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            myStage.setResizable(true);
            myStage.show();
        }
        else {
            Stage myStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainwindow.fxml"));
            MainWindowController controller = new MainWindowController(m);
            loader.setController(controller);
            myStage.setTitle("Main window");
            myStage.setScene(new Scene(loader.<Parent>load(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            myStage.setResizable(true);
            myStage.show();
        }
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

}
