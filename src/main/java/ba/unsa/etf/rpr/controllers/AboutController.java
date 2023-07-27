package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.domain.Member;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class AboutController {
    public Button mainPageBttn;
    public Button profile;
    public Button logOutBttn;
    public Button registerBttn;
    public Button logInBttn;
    private Member member;
    AboutController(Member m) {this.member = m;}
    @FXML
    public void initialize() {
        if(member == null) {
            mainPageBttn.setVisible(false);
            profile.setVisible(false);
            logOutBttn.setVisible(false);
            logInBttn.setVisible(true);
            registerBttn.setVisible(true);
        }
        else {
            mainPageBttn.setVisible(true);
            profile.setVisible(true);
            logOutBttn.setVisible(true);
            logInBttn.setVisible(false);
            registerBttn.setVisible(false);
        }
    }

    public void mainPage(ActionEvent actionEvent) throws IOException {
        if(member.isAdmin()) {
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
        else {
            Stage myStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainwindow.fxml"));
            MainWindowController controller = new MainWindowController(member);
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

    public void register(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/registration.fxml"));
        myStage.setTitle("Sign up");
        myStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.setResizable(false);
        myStage.show();
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    public void logIn(ActionEvent actionEvent) throws IOException {
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
