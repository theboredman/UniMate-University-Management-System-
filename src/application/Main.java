package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginPage.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/CSS-loginPage.css").toExternalForm());


            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setTitle("Welcome to UniMate");
            primaryStage.setResizable(false);
            Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
            primaryStage.getIcons().add(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
