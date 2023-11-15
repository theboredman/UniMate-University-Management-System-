package controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

import javafx.scene.Node;
import javafx.event.ActionEvent;

public class AddPostsController {

    @FXML
    private Label titleLabel;

    @FXML
    private MFXTextField postField;

    @FXML
    private MFXButton addButton;

    @FXML
    private MFXButton cancelButton;

    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Circle profilePicture;

    private String userId;
    private String userName;
    private String profilePicturePath;

    @FXML
    private void handleAddButtonAction(ActionEvent event) {
        String postText = postField.getText();
        if (postText.isEmpty()) {
            showAlert("Error", "Please enter a post.");
        } else {
            if (!isDuplicatePost(postText)) {
                storePostData(postText);

                closeCurrentScene(event);

                clearFields();
            } else {
                showAlert("Duplicate Post", "This post is already added.");
            }
        }
    }

    private void closeCurrentScene(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void storePostData(String postText) {
        try (FileWriter writer = new FileWriter("Post_data.txt", true)) {
            String postData = userId + "," + userName + "," + profilePicturePath + "," + postText + "\n";
            writer.write(postData);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to store post data.");
        }
    }

    private boolean isDuplicatePost(String newPost) {
        try {
            // Read all lines from the file
            String content = Files.readString(Paths.get("Post_data.txt"));

            return content.contains(userId + "," + userName + "," + profilePicturePath + "," + newPost);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to check for duplicate post data.");
            return false;
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {

        clearFields();
        closeCurrentScene(event);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        postField.clear();
    }

    public void setUserId(String userId) {
        this.userId = userId;
        if (idLabel != null) {
            idLabel.setText(userId);
        }
    }

    // Method to set user name
    public void setUserName(String userName) {
        this.userName = userName;
        if (nameLabel != null) {
            nameLabel.setText(userName);
        }
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
        updateProfilePicture();
    }

    public void setProfilePicturePathFaculty(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
        updateProfilePictureFaculty();
    }

    public void setProfilePicturePathAdmin(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
        Image profileImage = new Image("file:" + profilePicturePath);
        profilePicture.setFill(new ImagePattern(profileImage));
    }


    private void updateProfilePictureFaculty() {
        File userDirectory = new File("FacultyData/" + userId);

        if (userDirectory.exists() && userDirectory.isDirectory()) {
            File[] imageFiles = userDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));

            if (imageFiles != null && imageFiles.length > 0) {
                Arrays.sort(imageFiles, Comparator.comparing(File::getName));
                File latestProfilePicture = imageFiles[imageFiles.length - 1];

                try {
                    if (userName != null) {
                        profilePicturePath = latestProfilePicture.getAbsolutePath();

                        nameLabel.setText(userName);

                        Image profileImage = new Image("file:" + profilePicturePath);
                        profilePicture.setFill(new ImagePattern(profileImage));
                    }
                } catch (Exception e) {
                    System.err.println("Error loading profile picture: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("No jpg files found in the directory: " + userDirectory.getAbsolutePath());
            }
        }
    }

    private void updateProfilePicture() {
        File userDirectory = new File("userData/" + userId);

        if (userDirectory.exists() && userDirectory.isDirectory()) {
            File[] imageFiles = userDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));

            if (imageFiles != null && imageFiles.length > 0) {
                Arrays.sort(imageFiles, Comparator.comparing(File::getName));
                File latestProfilePicture = imageFiles[imageFiles.length - 1];

                try {
                    if (userName != null) {
                        profilePicturePath = latestProfilePicture.getAbsolutePath();

                        nameLabel.setText(userName);

                        //  image  set it on the Circle
                        Image profileImage = new Image("file:" + profilePicturePath);
                        profilePicture.setFill(new ImagePattern(profileImage));
                    }
                } catch (Exception e) {
                    System.err.println("Error loading profile picture: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("No jpg files found in the directory: " + userDirectory.getAbsolutePath());
            }
        }
    }


}
