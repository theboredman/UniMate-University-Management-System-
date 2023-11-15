package controllers;

import java.io.FileInputStream;
import java.io.IOException;

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

public class ControllerAdmin {

    @FXML
    private Hyperlink registerLink;
    @FXML
    private TextField adminUsernameField;

    @FXML
    private TextField adminPasswordField;

    @FXML
    private Button adminLoginButton;

    @FXML
    private Button adminBackButton;
    @FXML
    private ProgressBar progressBar;

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.setTitle("Login");
        Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
        stage.getIcons().add(logo);
    }

    @FXML
    private void handleRegisterLink(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Use your given phone number instead.");

        alert.showAndWait();
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String username = adminUsernameField.getText();
        String password = adminPasswordField.getText();

        if (username.equals("admin") && password.equals("admin")) {
            ((Stage) adminUsernameField.getScene().getWindow()).close();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoadingScreen.fxml"));
                Parent root = loader.load();
                LoadingScreenController loadingController = loader.getController();

                Scene scene = new Scene(root);
                Window window = adminUsernameField.getScene().getWindow();

                Stage stage = new Stage();
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(window);
                stage.setScene(scene);
                stage.show();
                stage.setResizable(false);
                stage.setTitle("Loading.......");
                Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
                stage.getIcons().add(logo);

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
                        FXMLLoader manageLoader = new FXMLLoader(getClass().getResource("/fxml/DashBoard.fxml"));
                        Parent manageRoot = manageLoader.load();
                        Scene manageScene = new Scene(manageRoot);

                        Stage manageStage = new Stage();
                        manageStage.initModality(Modality.WINDOW_MODAL);
                        manageStage.initOwner(window);
                        manageStage.setScene(manageScene);
                        manageStage.show();
                        manageStage.setResizable(false);
                        manageStage.setTitle("Dashboard");
                        manageStage.getIcons().add(logo);

                        stage.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                new Thread(task).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Wrong username or password.");
            alert.showAndWait();
        }
    }


}

