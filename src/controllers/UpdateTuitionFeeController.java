package controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class UpdateTuitionFeeController {
    private static final String TUITION_FEE_FILE_PATH = "TuitionFee_data.txt";


    @FXML
    private MFXTextField perCreditCostTextField;

    @FXML
    private MFXTextField studentActivityFeeTextField;

    @FXML
    private MFXTextField scienceLabTextField;

    @FXML
    private MFXTextField computerLabFeeTextField;

    @FXML
    private MFXTextField libraryFeeTextField;

    @FXML
    private MFXButton updateButton;

    @FXML
    private MFXButton cancelButton;

    @FXML
    private void handleupdateButtonAction() {
        // Validate input
        if (!isValidNumber(perCreditCostTextField.getText()) ||
                !isValidNumber(studentActivityFeeTextField.getText()) ||
                !isValidNumber(scienceLabTextField.getText()) ||
                !isValidNumber(computerLabFeeTextField.getText()) ||
                !isValidNumber(libraryFeeTextField.getText())) {
            showAlert("Error", "Please enter valid numeric values for all fields.");
            return; // Exit the method if validation fails
        }

        // Retrieve values from text fields
        double perCreditCost = Double.parseDouble(perCreditCostTextField.getText());
        double studentActivityFee = Double.parseDouble(studentActivityFeeTextField.getText());
        double scienceLabFee = Double.parseDouble(scienceLabTextField.getText());
        double computerLabFee = Double.parseDouble(computerLabFeeTextField.getText());
        double libraryFee = Double.parseDouble(libraryFeeTextField.getText());

        // Save the data to TuitionFee_data.txt
        saveTuitionFeeData(perCreditCost, studentActivityFee, scienceLabFee, computerLabFee, libraryFee);

        // Display success message
        showAlert("Update Successful", "Tuition fees updated successfully.");

        // Close the window
        Stage stage = (Stage) perCreditCostTextField.getScene().getWindow();
        stage.close();
    }


    private void saveTuitionFeeData(double perCreditCost, double studentActivityFee, double scienceLabFee, double computerLabFee, double libraryFee) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("TuitionFee_data.txt"))) {
            // Write the tuition fee data to the file
            writer.write("Per Credit Cost: " + perCreditCost);
            writer.newLine();
            writer.write("Student Activity Fee: " + studentActivityFee);
            writer.newLine();
            writer.write("Science Lab Fee: " + scienceLabFee);
            writer.newLine();
            writer.write("Computer Lab Fee: " + computerLabFee);
            writer.newLine();
            writer.write("Library Fee: " + libraryFee);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your application's requirements
        }
    }

    private boolean isValidNumber(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    @FXML
    private void handleCancelButtonAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();

        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class ManageScholarshipController {
    }
}
