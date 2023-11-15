package controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class ControllerStudent {

    @FXML
    private TextField studentIDField;

    @FXML
    private TextField studentPasswordField;

    @FXML
    private Button studentLoginButton;

    @FXML
    private Button studentBackButton;
    @FXML
    private Hyperlink registerLink;

    private Stage loginStage;


    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Login Panel");
        stage.setResizable(false);
        Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
        stage.getIcons().add(logo);

    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String userId = studentIDField.getText();
        String password = studentPasswordField.getText();

        try (BufferedReader br = new BufferedReader(new FileReader("user_data.txt"))) {
            String line;
            boolean loginSuccessful = false;

            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");

                if (userData.length == 8) {
                    String storedUserID = userData[0].trim();
                    String storedPhoneNumber = userData[4].trim();
                    String storedPassword = userData[userData.length - 1].trim();

                    if (storedUserID.equals(userId) && (password.equals(storedPassword) || password.equals(storedPhoneNumber))) {
                        loginSuccessful = true;
                        break;
                    }
                } else {
                    System.out.println("Invalid data format: " + line);
                }
            }


            if (loginSuccessful) {
                System.out.println("Login successful!");
                Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                loginStage.close();

                showLoadingScreen();
            } else {
                System.out.println("Login unsuccessful. Wrong user ID or password.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Error");
                alert.setHeaderText(null);
                alert.setContentText("Wrong user ID or password.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showLoadingScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoadingScreen.fxml"));
            Parent root = loader.load();
            LoadingScreenController loadingController = loader.getController();

            loadingController.setUserId(getUserId());

            Scene scene = new Scene(root);

            Stage loadingStage = new Stage();
            loadingStage.initModality(Modality.WINDOW_MODAL);
            loadingStage.setScene(scene);
            loadingStage.show();
            loadingStage.setTitle("Loading");
            loadingStage.setResizable(false);
            Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
            loadingStage.getIcons().add(logo);


            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    for (int i = 0; i <= 100; i++) {
                        final int progress = i;
                        Platform.runLater(() -> loadingController.getProgressBar().setProgress(progress / 100.0));
                        Thread.sleep(30);
                    }
                    return null;
                }
            };

            task.setOnSucceeded(e -> {
                try {
                    showDashboard();
                    loadingStage.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            new Thread(task).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDashboard() throws IOException {
        FXMLLoader manageLoader = new FXMLLoader(getClass().getResource("/fxml/StudentDashboard.fxml"));
        Parent manageRoot = manageLoader.load();
        Scene manageScene = new Scene(manageRoot);

        StudentDashboardController dashboardController = manageLoader.getController();
        dashboardController.setStudentId(studentIDField.getText());

        Stage dashboardStage = new Stage();
        dashboardStage.setScene(manageScene);
        dashboardStage.show();
        dashboardStage.setTitle("Student Dashboard");
        dashboardStage.setResizable(false);
        Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
        dashboardStage.getIcons().add(logo);
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void handleRegisterLink(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Use your given phone number instead.");

        alert.showAndWait();
    }

    public String getUserId() {
        return studentIDField.getText();
    }


}
