package controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PaneFacultyController {
    @FXML
    private Label showCourseInitialLabel;

    @FXML
    private MFXButton submitGradesButton;

    @FXML
    private MFXButton syllabusButton;

    @FXML
    private MFXButton cancelClassButton;

    private String courseInitial;
    private String facultyInitial;
    private String userId;
    private String profilePicturePath;

    // Add your initialization logic here
    @FXML
    private void initialize() {
        // You can add any initialization logic here
        // For example, you can set event handlers for the buttons
        submitGradesButton.setOnAction(event -> handleSubmitGrades());
        syllabusButton.setOnAction(event -> handleSyllabus());
        cancelClassButton.setOnAction(event -> handleCancelClass());
    }

    // Define methods for handling button actions
    private void handleSubmitGrades() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SubmitGrade.fxml"));
            Parent root = loader.load();

            // Access the controller and set the user information
            SubmitGradeController submitGradeController = loader.getController();
            submitGradeController.setFacultyId(userId); // pass the user ID
            submitGradeController.setCourseAndFacultyInitials(courseInitial, facultyInitial); // pass the user name
            submitGradeController.setProfilePicturePath(profilePicturePath);


            // Set a listener for when the add post window is closed
            Stage stage = new Stage();
            stage.setTitle("Submit Grades");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            javafx.scene.image.Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
            stage.getIcons().add(logo);

            // Set the stage as a modal window (blocks interactions with the main window)
            stage.initModality(Modality.APPLICATION_MODAL);

            // Show the stage and wait for it to be closed
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSyllabus() {
        // Path to the syllabus PDF file
        String syllabusFilePath = "courseData/" + courseInitial + "/" + courseInitial + "_syllabus.pdf";

        try {
            File syllabusFile = new File(syllabusFilePath);

            if (syllabusFile.exists() && syllabusFile.isFile()) {
                Desktop.getDesktop().open(syllabusFile);
            } else {
                showAlert("Syllabus Not Found", "The syllabus file is not available.", Alert.AlertType.ERROR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }


    private void handleCancelClass() {
        System.out.println("Cancel Class button clicked");
        // Add your logic here
    }

    public void setShowCourseInitial(String courseInitial) {
        this.courseInitial = courseInitial;
        showCourseInitialLabel.setText(courseInitial);
    }

    public void setFacultyInitial(String facultyInitial) {
        this.facultyInitial = facultyInitial;
    }

    public void setFacultyId(String userId) throws FileNotFoundException {
        this.userId = userId;

    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
