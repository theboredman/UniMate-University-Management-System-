package controllers;

import application.UserData;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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

public class AddUserPromp implements Initializable {

    @FXML
    private MFXTextField idField;

    @FXML
    private MFXTextField userNameTextField;

    @FXML
    private MFXTextField departmentField;

    @FXML
    private MFXTextField emailField;

    @FXML
    private MFXTextField phoneNumberField;

    @FXML
    private MFXTextField passedCreditField;

    @FXML
    private MFXTextField cgpaField;

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    private MFXButton addButton;

    @FXML
    private MFXButton registerButton;

    @FXML
    private MFXButton cancelButton;

    @FXML
    private MFXButton uploadButton;

    @FXML
    private Label titleLabel;

    @FXML
    private ImageView userImageView;
    @FXML
    private TableView<UserData> tableView;
    private ObservableList<UserData> userDataList = FXCollections.observableArrayList();
    private ManageUserProfile manageUserProfile;

    private File selectedImageFile;

    public AddUserPromp() {
        this.tableView = new TableView<>();
    }

    public UserData getUserData() {
        UserData userData = new UserData();
        userData.setId(getId());
        userData.setUserName(getUserName());
        userData.setDepartment(getDepartment());
        userData.setEmail(getEmail());
        userData.setPhoneNumber(getPhoneNumber());
        userData.setPassedCredit(getPassedCredit());
        userData.setCgpa(getCGPA());
        userData.setPassword(getPassword());
        userData.setProfilePicturePath(String.valueOf(selectedImageFile));


        String profilePictureFileName = null;
        if (selectedImageFile != null) {
            profilePictureFileName = selectedImageFile.getName();
        }

        return new UserData(userData.getId(), userData.getUserName(), userData.getDepartment(), userData.getEmail(), userData.getPhoneNumber(), userData.getPassedCredit(), userData.getCgpa(), userData.getPassword(), profilePictureFileName);
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
    private void handleRegisterButtonAction(ActionEvent event) {
        String id = getId();
        String userName = getUserName();
        String department = getDepartment();
        String email = getEmail();
        String phoneNumber = getPhoneNumber();
        String passedCredit = getPassedCredit();
        String cgpa = getCGPA();
        String password = getPassword();

        if (!isValidId(id) || !isValidPhoneNumber(phoneNumber) || !isValidPassedCredit(passedCredit) || !isValidEmail(email) || !isValidCGPA(cgpa)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please check your input fields for errors. \nID must be 10 digit, \nPhone number must be 11 digit, \nPassed credit must be 0-180, \nCGPA must be 0.0-4.0, \nEmail must be valid");
            alert.showAndWait();
            return;
        }

        String filePath = "registration.txt";
        userInputs(event, id, userName, department, email, phoneNumber, passedCredit, cgpa, password, filePath);

    }

    private void userInputs(ActionEvent event, String id, String userName, String department, String email, String phoneNumber, String passedCredit, String cgpa, String password, String filePath) {
        String profilePicturePath = selectedImageFile != null ? selectedImageFile.getName() : null;
        UserData userData = new UserData(id, userName, department, email, phoneNumber, passedCredit, cgpa, password, profilePicturePath);

        if (this.manageUserProfile != null) {
            this.manageUserProfile.getUserDataList().add(userData);
        }

        userDataList.add(userData);

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            File userDirectory = new File("userData/" + id);
            if (!userDirectory.exists()) {
                userDirectory.mkdirs();
            }

            File userFile = new File(userDirectory, id + ".txt");
            if (!userFile.exists()) {
                userFile.createNewFile();
            }

            BufferedWriter writer1 = new BufferedWriter(new FileWriter(file, true));
            writer1.write(id + "," + userName + "," + department + "," + email + "," + phoneNumber + "," + passedCredit + "," + cgpa + "," + password);
            writer1.newLine();
            writer1.close();

            BufferedWriter writer2 = new BufferedWriter(new FileWriter(userFile));
            writer2.write("ID: " + id + "\n");
            writer2.write("Username: " + userName + "\n");
            writer2.write("Department: " + department + "\n");
            writer2.write("Email: " + email + "\n");
            writer2.write("Phone Number: " + phoneNumber + "\n");
            writer2.write("Passed Credit: " + passedCredit + "\n");
            writer2.write("CGPA: " + cgpa + "\n");
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

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
    private void handleAddButtonAction(ActionEvent event) {
        String id = getId();
        String userName = getUserName();
        String department = getDepartment();
        String email = getEmail();
        String phoneNumber = getPhoneNumber();
        String passedCredit = getPassedCredit();
        String cgpa = getCGPA();
        String password = getPassword();

        if (!isValidId(id) || !isValidPhoneNumber(phoneNumber) || !isValidPassedCredit(passedCredit) || !isValidEmail(email) || !isValidCGPA(cgpa)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please check your input fields for errors. \nID must be 10 digit, \nPhone number must be 11 digit, \nPassed credit must be 0-180, \nCGPA must be 0.0-4.0, \nEmail must be valid");
            alert.showAndWait();
            return;
        }
        String filePath = "user_data.txt";
        userInputs(event, id, userName, department, email, phoneNumber, passedCredit, cgpa, password, filePath);


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
                File facultyDataDirectory = new File("userData");
                if (!facultyDataDirectory.exists()) {
                    facultyDataDirectory.mkdir();
                }

                String userId = getId();
                File userDirectory = new File(facultyDataDirectory, userId);
                if (!userDirectory.exists()) {
                    userDirectory.mkdirs();
                }

                String newFileName = userId + ".jpg"; // File name will be userID.jpg
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

    public void setManageUserProfile(ManageUserProfile manageUserProfile) {
        this.manageUserProfile = manageUserProfile;
    }

    private boolean isValidId(String id) {
        return id.matches("\\d{10}");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{11}");
    }

    private boolean isValidPassedCredit(String passedCredit) {
        int credit = Integer.parseInt(passedCredit);
        return credit >= 0 && credit <= 180 && passedCredit.matches("\\d{1,3}");
    }

    private boolean isValidEmail(String email) {
        return email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}");
    }

    private boolean isValidCGPA(String cgpa) {
        try {
            double cgpaValue = Double.parseDouble(cgpa);
            return cgpaValue >= 0.0 && cgpaValue <= 4.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void addUser(UserData userData) {
        this.manageUserProfile.getUserDataList().add(userData);
        if (this.manageUserProfile != null) {
            this.manageUserProfile.getUserDataList().add(userData);
        }

    }

    public String getId() {
        return idField.getText();
    }

    public void setId(String id) {
        idField.setText(id);
    }

    public String getUserName() {
        return userNameTextField.getText();
    }

    public void setUserName(String userName) {
        userNameTextField.setText(userName);
    }

    public String getDepartment() {
        return departmentField.getText();
    }

    public void setDepartment(String department) {
        departmentField.setText(department);
    }

    public String getEmail() {
        return emailField.getText();
    }

    public void setEmail(String email) {
        emailField.setText(email);
    }

    public String getPhoneNumber() {
        return phoneNumberField.getText();
    }

    public void setPhoneNumber(String phoneNumber) {
        phoneNumberField.setText(phoneNumber);
    }

    public String getPassedCredit() {
        return passedCreditField.getText();
    }

    public void setPassedCredit(String passedCredit) {
        passedCreditField.setText(passedCredit);
    }

    public String getCGPA() {
        return cgpaField.getText();
    }

    public void setCGPA(String cgpa) {
        cgpaField.setText(cgpa);
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public void setPassword(String password) {
        passwordField.setText(password);
    }

    public ObservableList<UserData> getUserDataList() {
        return userDataList;
    }

}