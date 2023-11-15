package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerLoginPage implements Initializable {

    @FXML
    private VBox rightVBox;


    @FXML
    private Button adminLoginButton;

    @FXML
    private Button studentLoginButton;

    @FXML
    private Button facultyLoginButton;

    @FXML
    private Hyperlink registerLink;

    @FXML
    private Label titleLabel;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label developerLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (rightVBox != null) {
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(javafx.scene.paint.Color.BLACK);
            dropShadow.setHeight(20);
            dropShadow.setWidth(20);
            dropShadow.setRadius(20);
            rightVBox.setEffect(dropShadow);
        }
    }


    @FXML
    private void handleAdminLoginButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminLogin.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) adminLoginButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
            stage.setTitle("Admin Login");
            stage.setResizable(false);
            Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
            stage.getIcons().add(logo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleStudentLoginButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StudentLogin.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) studentLoginButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
            stage.setTitle("Student Login");
            stage.setResizable(false);
            Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
            stage.getIcons().add(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFacultyLoginButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FacultyLogin.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) facultyLoginButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
            stage.setTitle("Faculty Login");
            stage.setResizable(false);
            Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
            stage.getIcons().add(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegisterLink(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Register.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registration");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
