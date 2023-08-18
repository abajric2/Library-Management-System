package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.business.MemberManager;
import ba.unsa.etf.rpr.domain.Member;
import ba.unsa.etf.rpr.exceptions.LibraryException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

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
    public Button aboutBttn;
    public Label removeAdminLabel;
    public Button removeAdminBttn;
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
        if(!member.isAdmin()) {
            removeAdminLabel.setVisible(false);
            removeAdminBttn.setVisible(false);
        }
        else {
            removeAdminLabel.setVisible(true);
            removeAdminBttn.setVisible(true);
        }
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
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("^[a-zA-Z0-9_.-]+$")) {
                checkUsername.setText("Username can only contain letters, numbers, underscores, dots, and dashes.");
            } else {
                checkUsername.setText("");
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
            member = manager.update(m);
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
            if(!m.getUsername().matches("^[a-zA-Z0-9_.-]+$") || !m.getFirstName().matches("[a-zA-Z -]*") ||
                    !m.getLastName().matches("[a-zA-Z -]*") || m.getPassword().length() < 8)
                alert.setContentText("Check that the values you entered are of a valid type.");
            else alert.setContentText("It seems like someone is already using the username you entered!");
            alert.showAndWait();
            //   idUpdate = null;
        }
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
    public void deleteAction(ActionEvent actionEvent) throws IOException {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation Dialog");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete your account?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Member m = new Member();
            m.setId(member.getId());
            m.setFirstName(member.getFirstName());
            m.setLastName(member.getLastName());
            m.setUsername(member.getUsername());
            m.setPassword(member.getPassword());
            m.setAdmin(member.isAdmin());
            try {
                manager.delete(m);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Successfully deleted!");
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
            } catch (LibraryException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Delete error!");
                alert.setContentText("Check if you have rented books. If answer is yes, you need to return them before deleting your account!");
                alert.showAndWait();
                //idUpdate = null;
            }
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
            Scene scene = new Scene(loader.<Parent>load(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
            scene.getStylesheets().add(getClass().getResource("/css/list.css").toExternalForm());
            myStage.setScene(scene);
            myStage.setResizable(true);
            myStage.setMaximized(true);
            myStage.show();
            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
        }
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

    public void removeAdmin(ActionEvent actionEvent) throws IOException {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation Dialog");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to remove yourself from administrators?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                member.setAdmin(false);
                member = manager.update(member);
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
            catch (LibraryException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Update error!");
                alert.setContentText("An error occurred while removing the administrator role!");
            }
        }
    }
}
