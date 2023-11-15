package controllers;

import application.CourseData;
import application.UserData;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class AddCoursePrompController implements Initializable {

    @FXML
    private MFXTextField courseNameField;

    @FXML
    private MFXTextField courseInitialField;

    @FXML
    private MFXTextField creditField;

    @FXML
    private MFXTextField totalSeatField;

    @FXML
    private MFXTextField descriptionField;

    @FXML
    private MFXButton addButton;

    @FXML
    private MFXButton uploadButton;

    @FXML
    private MFXButton cancelButton;

    @FXML
    private Label titleLabel;
    private File syllabusFolder;
    private CourseManagementController courseManagementController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void handleAddButtonAction(ActionEvent event) {
        String courseInitial = courseInitialField.getText();
        String courseName = courseNameField.getText();
        String creditText = creditField.getText();
        String totalSeatText = totalSeatField.getText();
        String description = descriptionField.getText();

        // check empty fields

        if (courseName.isEmpty() || courseInitial.isEmpty() || creditText.isEmpty() || totalSeatText.isEmpty() || description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields are required!");
            alert.showAndWait();
            return;
        }
        int credit;
        int totalSeat;
        try {
            credit = Integer.parseInt(creditText);
            totalSeat = Integer.parseInt(totalSeatText);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Credit and Total Seat must be integers!");
            alert.showAndWait();
            return;
        }

        // range

        if (credit <= 0 || credit > 50 || totalSeat <= 0 || totalSeat > 50) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Credit and Total Seat must be between 1 and 50!");
            alert.showAndWait();
            return;
        }

        //  syllabus availability
        String syllabusAvailability = getSyllabusAvailability(courseInitial);

        String courseData = courseInitial + "," + courseName + "," + description + "," + credit + "," + totalSeat + "," + syllabusAvailability + "\n";

        // writre data
        File courseFolder = new File("courseData/" + courseInitial);
        if (!courseFolder.exists()) {
            courseFolder.mkdir();
        }
        try (FileWriter infoWriter = new FileWriter("courseData/" + courseInitial + "/" + courseInitial + "_info.txt")) {
            infoWriter.write("Course Initial: " + courseInitial + "\n");
            infoWriter.write("Course Name: " + courseName + "\n");
            infoWriter.write("Credit: " + credit + "\n");
            infoWriter.write("Total Seat: " + totalSeat + "\n");
            infoWriter.write("Description: " + description + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while adding the course.");
            alert.showAndWait();
            return;
        }
        try (FileWriter writer = new FileWriter("course_data.txt", true)) {
            writer.write(courseData);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Course added successfully!");
            alert.showAndWait();
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while adding the course.");
            alert.showAndWait();
        }
    }

    private String getSyllabusAvailability(String courseInitial) {
        String syllabusFilePath = "courseData/" + courseInitial + "/" + courseInitial + "_syllabus.pdf";
        File syllabusFile = new File(syllabusFilePath);

        if (syllabusFile.exists() && syllabusFile.isFile()) {
            return "Available";
        } else {
            return "N/A";
        }
    }


    @FXML
    private void handleUploadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Syllabus");
        File selectedFile = fileChooser.showOpenDialog(null);

        String courseInitial = courseInitialField.getText();
        File courseFolder = new File("courseData/" + courseInitial); // Course folder

        if (!courseFolder.exists()) {
            courseFolder.mkdirs(); //  mkdirs to create parent folders if dorkar
        }

        if (selectedFile != null) {
            File destFile = new File(courseFolder, courseInitial + "_syllabus.pdf");

            try {
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Syllabus uploaded successfully!");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error uploading syllabus.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No File Selected");
            alert.setHeaderText(null);
            alert.setContentText("No syllabus file selected. Course will be added without a syllabus.");
            alert.showAndWait();
        }
    }


    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }


    @FXML
    private void handleTitleLabelClicked(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText("Developed By Asadullah Hil Galib 221820642");
        alert.showAndWait();
    }


}