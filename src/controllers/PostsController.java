package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PostsController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Circle profilePicture;

    @FXML
    private Label nameLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label postDataLabel;
    private String profilePicturePath;

    // Add other UI components as needed

    @FXML
    private void initialize() {

    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
        Image profileImage = new Image("file:" + profilePicturePath);
        profilePicture.setFill(new ImagePattern(profileImage));
    }


    public void setNameLabel(String name) {
        nameLabel.setText(name);
    }

    public void setIdLabel(String id) {
        idLabel.setText(id);
    }

    public void setPostDataLabel(String postData) {
        postDataLabel.setText(postData);
    }

}
