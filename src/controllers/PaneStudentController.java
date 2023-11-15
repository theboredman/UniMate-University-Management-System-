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

public class PaneStudentController {
    @FXML
    private Label showCourseInitialLabel;

    @FXML
    private MFXButton submitGradesButton;

    @FXML
    private MFXButton syllabusButton;

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
    }

    // Define methods for handling button actions
    private void handleSubmitGrades() {
        showAlert("View Marks", "This feature is not available yet.", Alert.AlertType.INFORMATION);
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

    public void setShowCourseInitial(String courseInitial) {
        this.courseInitial = courseInitial;
        showCourseInitialLabel.setText(courseInitial);
    }

    public void setStudentId(String userId) {
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
