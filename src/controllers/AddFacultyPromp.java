package controllers;

import application.FacultyData;
import application.UserData;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class AddFacultyPromp implements Initializable {

    @FXML
    private MFXTextField idField;
    @FXML
    private MFXTextField initialField;

    @FXML
    private MFXTextField userNameTextField;

    @FXML
    private MFXTextField departmentField;

    @FXML
    private MFXTextField emailField;

    @FXML
    private MFXTextField phoneNumberField;

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    private MFXButton addButton;
    @FXML
    private ImageView userImageView;

    @FXML
    private TableView<FacultyData> tableView;
    private ObservableList<FacultyData> facultyDataList = FXCollections.observableArrayList();
    private ManageFacultyProfile manageFacultyProfile;


    @FXML
    private MFXButton cancelButton;

    private File selectedImageFile;

    public AddFacultyPromp() {
        this.tableView = new TableView<>();
    }

    public FacultyData getFacultyData() {
        FacultyData facultyData = new FacultyData();
        facultyData.setId(getId());
        facultyData.setInitial(getInitial());
        facultyData.setUserName(getUserName());
        facultyData.setDepartment(getDepartment());
        facultyData.setEmail(getEmail());
        facultyData.setPhoneNumber(getPhoneNumber());
        facultyData.setPassword(getPassword());
        facultyData.setProfilePicturePath(String.valueOf(selectedImageFile));

        String profilePictureFileName = null;
        if (selectedImageFile != null) {
            profilePictureFileName = selectedImageFile.getName();
        }

        return new FacultyData(facultyData.getId(), facultyData.getUserName(), facultyData.getDepartment(), facultyData.getEmail(), facultyData.getPhoneNumber(), facultyData.getPassword(), profilePictureFileName);


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.tableView = new TableView<>();
    }

    @FXML
    private void handleCancelButtonAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Unsaved Changes");
        alert.setContentText("Are you sure you want to cancel? Your changes will not be saved.");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            }
        });
    }

    @FXML
    private void handleAddButtonAction(ActionEvent event) {
        String id = getId();
        String initial = getInitial();
        String userName = getUserName();
        String department = getDepartment();
        String email = getEmail();
        String phoneNumber = getPhoneNumber();
        String password = getPassword();

        if (!isValidId(id) || !isValidPhoneNumber(phoneNumber) || !isValidEmail(email)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please check your input fields for errors. \nID must be 10 digits, \nPhone number must be 11 digits, \nEmail must be valid");
            alert.showAndWait();
            return;
        }

        String filePath = "faculty_data.txt";
        String profilePicturePath = selectedImageFile != null ? selectedImageFile.getName() : null;
        FacultyData facultyData = new FacultyData(id, userName, department, email, phoneNumber, password, profilePicturePath);


        if (this.manageFacultyProfile != null) {
            this.manageFacultyProfile.getFacultyDataList().add(facultyData);
        }
        facultyDataList.add(facultyData);

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            File userDirectory = new File("FacultyData/" + id);
            if (!userDirectory.exists()) {
                userDirectory.mkdirs();
            }

            File userFile = new File(userDirectory, id + ".txt");
            if (!userFile.exists()) {
                userFile.createNewFile();
            }
            BufferedWriter writer1 = new BufferedWriter(new FileWriter(file, true));
            writer1.write(id + "," + userName + "," + initial + "," + department + "," + email + "," + phoneNumber + "," + password);
            writer1.newLine();
            writer1.close();

            BufferedWriter writer2 = new BufferedWriter(new FileWriter(userFile));
            writer2.write("ID: " + id + "\n");
            writer2.write("Username: " + userName + "\n");
            writer2.write("Initial: " + initial + "\n");
            writer2.write("Department: " + department + "\n");
            writer2.write("Email: " + email + "\n");
            writer2.write("Phone Number: " + phoneNumber + "\n");
            writer2.write("Password: " + password);
            writer2.newLine();
            writer2.close();

            if (selectedImageFile != null) {
                Files.copy(selectedImageFile.toPath(), new File(userDirectory, selectedImageFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User data has been saved.");
            alert.showAndWait();
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while saving user data.");
            alert.showAndWait();
        }

    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                File facultyDataDirectory = new File("FacultyData");
                if (!facultyDataDirectory.exists()) {
                    facultyDataDirectory.mkdir();
                }

                String userId = getId();
                File userDirectory = new File(facultyDataDirectory, userId);
                if (!userDirectory.exists()) {
                    userDirectory.mkdirs();
                }

                String newFileName = userId + ".jpg";
                File newFile = new File(userDirectory, newFileName);

                Files.copy(selectedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                Image image = new Image(newFile.toURI().toString());
                userImageView.setImage(image);

                selectedImageFile = newFile;

            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while saving the profile picture.");
                alert.showAndWait();
            }
        }
    }

    private boolean isValidId(String id) {
        return id.matches("\\d{10}");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{11}");
    }

    private boolean isValidEmail(String email) {
        return email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}");
    }

    public void setFacultyDataList(ObservableList<FacultyData> facultyDataList) {
        this.facultyDataList = facultyDataList;
    }

    public String getId() {
        return idField.getText();
    }

    public String getInitial() {
        return initialField.getText();
    }

    public String getUserName() {
        return userNameTextField.getText();
    }

    public String getDepartment() {
        return departmentField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPhoneNumber() {
        return phoneNumberField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public void setManageFacultyProfile(ManageFacultyProfile manageFacultyProfile) {
        this.manageFacultyProfile = manageFacultyProfile;
    }

    public void addUser(FacultyData facultyData) {
        this.manageFacultyProfile.getFacultyDataList().add(facultyData);
        if (this.manageFacultyProfile != null) {
            this.manageFacultyProfile.getFacultyDataList().add(facultyData);
        }

    }
}
