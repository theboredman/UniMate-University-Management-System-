package controllers;

import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LogoutScreenController {

    @FXML
    private Label statusLabel;

    @FXML
    private MFXProgressBar progressBar;

    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public MFXProgressBar getProgressBar() {
        return progressBar;
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public void setProgress(double progress) {
        progressBar.setProgress(progress);
    }
}
