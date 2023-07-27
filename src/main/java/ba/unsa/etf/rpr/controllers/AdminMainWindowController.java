package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class AdminMainWindowController {
    public Button profileId;
    public Button logOutId;
    public Label welcomeId;
    public Button aboutBttn;
    public Button manageUserId;
    public Button manageBooksId;
    public Button regularUserId;
    public Button viewRentalsBttn;
    public Button addRentalBttn;

    private Member member;

    AdminMainWindowController(Member m) {
        this.member = m;
    }
    @FXML
    public void initialize() throws LibraryException {
        welcomeId.setText(welcomeId.getText() + " " + member.getFirstName() + "!");
    }
    public void profileAction(ActionEvent actionEvent) throws IOException {
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

    public void about(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/about.fxml"));
        AboutController controller = new AboutController(member);
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


    public void manageBooks(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manageBooks.fxml"));
        ManageBooksController controller = new ManageBooksController(member);
        loader.setController(controller);
        myStage.setTitle("Manage books");
        myStage.setScene(new Scene(loader.<Parent>load(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.setResizable(true);
        myStage.setMaximized(true);
        myStage.show();
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }
    public void manageUsers(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manageUsers.fxml"));
        ManageUsersController controller = new ManageUsersController(member);
        loader.setController(controller);
        myStage.setTitle("Manage users");
        myStage.setScene(new Scene(loader.<Parent>load(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.setResizable(true);
        myStage.setMaximized(true);
        myStage.show();
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    public void regularUserMode(ActionEvent actionEvent) throws IOException {
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

    public void viewRentals(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manageRentals.fxml"));
        ManageRentalsController controller = new ManageRentalsController(member);
        loader.setController(controller);
        myStage.setTitle("View rentals");
        myStage.setScene(new Scene(loader.<Parent>load(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.setResizable(true);
        myStage.setMaximized(true);
        myStage.show();
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    public void addRental(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addNewRental.fxml"));
        AddNewRentalController controller = new AddNewRentalController(member);
        loader.setController(controller);
        myStage.setTitle("Add rental");
        myStage.setScene(new Scene(loader.<Parent>load(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.setResizable(true);
        myStage.setMaximized(true);
        myStage.show();
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

}
