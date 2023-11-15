package controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class NewsfeedAdminController implements Initializable {
    @FXML
    private MFXButton dashBoardButton;
    @FXML
    private MFXButton manageUserProfilesButton;
    @FXML
    private MFXButton manageFacultyProfilesButton;
    @FXML
    private MFXButton registrationRequestsButton;
    @FXML
    private MFXButton courseManagementButton;
    @FXML
    private MFXButton viewCourseEnrollmentButton;
    @FXML
    private MFXButton viewTuitionAndFeeStatisticsButton;
    @FXML
    private MFXButton manageScholarshipsButton;
    @FXML
    private MFXButton newsFeedButton;
    @FXML
    private MFXButton postManagementButton;
    @FXML
    private MFXButton viewUserEngagementInsightsButton;
    @FXML
    private MFXButton viewGradeDistributionsButton;
    @FXML
    private MFXButton viewPerformanceMetricsButton;
    @FXML
    private MFXButton eventManagementButton;
    @FXML
    private MFXButton resourceManagementButton;
    @FXML
    private MFXButton manageWaitlistsButton;
    @FXML
    private MFXButton accessReportsAndDataExportButton;
    @FXML
    private MFXButton createSystemWideAnnouncementsButton;
    @FXML
    private MFXButton manageAnnouncementsButton;
    @FXML
    private MFXButton sendNotificationsButton;
    @FXML
    private MFXScrollPane scrollPane;
    @FXML
    private MFXNotificationCenter notificationCenter;
    @FXML
    private MenuItem logoutMenuItem;
    @FXML
    private VBox vBox;
    @FXML
    private MFXButton addPostsButton;

    private String profilePicturePath = "C:\\Users\\USER\\IdeaProjects\\MatrialFx\\src\\icon\\useravatar.png  ";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scrollPane.setOnScroll(event -> {
            double deltaY = event.getDeltaY();
            double deltaX = event.getDeltaX();

            if (deltaY != 0) {
                scrollVertically(deltaY);
            }

            if (deltaX != 0) {
                scrollHorizontally(deltaX);
            }
        });
        dashBoardButton.setOnAction(this::handleDashboardButtonAction);
        manageUserProfilesButton.setOnAction(this::handleManageUserProfilesButtonAction);
        manageFacultyProfilesButton.setOnAction(this::handleManageFacultyProfilesButtonAction);
        registrationRequestsButton.setOnAction(this::handleRegistrationRequestsButtonAction);
        courseManagementButton.setOnAction(this::handleCourseManagementButtonAction);
        viewCourseEnrollmentButton.setOnAction(this::handleViewCourseEnrollmentButtonAction);
        viewTuitionAndFeeStatisticsButton.setOnAction(this::handleViewTuitionAndFeeStatisticsButtonAction);
        manageScholarshipsButton.setOnAction(this::handleManageScholarshipsButtonAction);
        newsFeedButton.setOnAction(this::handleNewsFeedButtonAction);
        postManagementButton.setOnAction(this::handlePostManagementButtonAction);
        viewUserEngagementInsightsButton.setOnAction(this::handleViewUserEngagementInsightsButtonAction);
        viewGradeDistributionsButton.setOnAction(this::handleViewGradeDistributionsButtonAction);
        viewPerformanceMetricsButton.setOnAction(this::handleViewPerformanceMetricsButtonAction);
        eventManagementButton.setOnAction(this::handleEventManagementButtonAction);
        resourceManagementButton.setOnAction(this::handleResourceManagementButtonAction);
        manageWaitlistsButton.setOnAction(this::handleManageWaitlistsButtonAction);
        accessReportsAndDataExportButton.setOnAction(this::handleAccessReportsAndDataExportButtonAction);
        createSystemWideAnnouncementsButton.setOnAction(this::handleCreateSystemWideAnnouncementsButtonAction);
        manageAnnouncementsButton.setOnAction(this::handleManageAnnouncementsButtonAction);
        sendNotificationsButton.setOnAction(this::handleSendNotificationsButtonAction);
        logoutMenuItem.setOnAction(this::handleLogoutMenuItemAction);
        loadAndDisplayPosts();

    }

    private void loadAndDisplayPosts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Post_data.txt"))) {
            String line;
            List<AnchorPane> postElements = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                // Assuming each line in the file represents a post
                String[] postParts = line.split(",");

                // Create and configure a new post element
                AnchorPane postElement = createPostElement(postParts);

                // Ensure the VBox expands vertically
                VBox.setVgrow(postElement, Priority.ALWAYS);

                postElements.add(postElement);
            }

            // Add post elements in descending order to the beginning of the VBox
            Collections.reverse(postElements);
            vBox.getChildren().addAll(0, postElements);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    private AnchorPane createPostElement(String[] postParts) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Posts.fxml"));
            AnchorPane postElement = loader.load();
            PostsController postsController = loader.getController();

            // Customize postElementController if needed
            // ...

            // Extract data from postParts
            String userId = postParts[0];
            String userName = postParts[1];
            String profilePicturePath = postParts[2];
            String postContent = postParts[3];

            // Set data in the post element
            postsController.setIdLabel(userId);
            postsController.setNameLabel(userName);
            postsController.setProfilePicturePath(profilePicturePath);
            postsController.setPostDataLabel(postContent);

            return postElement;
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
            return new AnchorPane();  // Return an empty AnchorPane in case of an error
        }
    }

    @FXML
    private void handleAddPostsButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddPosts.fxml"));
            Parent root = loader.load();

            // Access the controller and set the user information
            AddPostsController addPostsController = loader.getController();
            addPostsController.setUserId("Admin"); // pass the user ID
            addPostsController.setUserName("Admin"); // pass the user name
            addPostsController.setProfilePicturePathAdmin(profilePicturePath);

            // Set a listener for when the add post window is closed
            Stage stage = new Stage();
            stage.setTitle("Add Posts");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
            stage.getIcons().add(logo);

            // Set the stage as a modal window (blocks interactions with the main window)
            stage.initModality(Modality.APPLICATION_MODAL);

            // Show the stage and wait for it to be closed
            stage.showAndWait();

            // After the add post window is closed, refresh and display new posts
            refreshAndDisplayPosts();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshAndDisplayPosts() {
        // Clear the existing posts in the VBox
        vBox.getChildren().clear();

        // Reload and display posts
        loadAndDisplayPosts();
    }

    @FXML
    private void handleDashboardButtonAction(ActionEvent event) {
        callScene(dashBoardButton, "/fxml/Dashboard.fxml", "Dashboard");
    }

    @FXML
    private void handleLogoutMenuItemAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage logoutStage = (Stage) logoutMenuItem.getParentPopup().getOwnerWindow();
            logoutStage.close();
            showLogoutScreen();
        }
    }

    private void handleManageFacultyProfilesButtonAction(ActionEvent event) {
        callScene(manageFacultyProfilesButton, "/fxml/ManageFacultyProfile.fxml", "Faculty Management");
    }

    @FXML
    private void handleNotificationCenterAction(ActionEvent event) {
        // Add your code to handle notification center action here
    }

    private void handleManageUserProfilesButtonAction(ActionEvent event) {
        callScene(manageUserProfilesButton, "/fxml/ManageUserProfile.fxml", "User Management");
    }

    private void handleRegistrationRequestsButtonAction(ActionEvent event) {
        callScene(registrationRequestsButton, "/fxml/RegistrationRequests.fxml", "Registration Requests");
    }

    private void handleCourseManagementButtonAction(ActionEvent event) {
        callScene(courseManagementButton, "/fxml/CourseManagement.fxml", "Course Management");
    }

    private void handleViewCourseEnrollmentButtonAction(ActionEvent event) {
        callScene(viewCourseEnrollmentButton, "/fxml/ViewCourseEnrollmentAdmin.fxml", "Course Enrollment");
    }

    private void handleViewTuitionAndFeeStatisticsButtonAction(ActionEvent event) {
        callScene(viewTuitionAndFeeStatisticsButton, "/fxml/ViewTuitionAndFeeStatistics.fxml", "Tuition and Fee Statistics");
    }

    private void handleManageScholarshipsButtonAction(ActionEvent event) {
        callScene(manageScholarshipsButton, "/fxml/ManageScholarshipsAdmin.fxml", "Manage Scholarships");
    }

    private void handleNewsFeedButtonAction(ActionEvent event) {
        callScene(newsFeedButton, "/fxml/NewsfeedAdmin.fxml", "News Feed");
    }

    private void handlePostManagementButtonAction(ActionEvent event) {
        callScene(postManagementButton, "/fxml/PostManagement.fxml", "Post Management");
    }

    private void handleViewUserEngagementInsightsButtonAction(ActionEvent event) {
        callScene(viewUserEngagementInsightsButton, "/fxml/ViewUserEngagementInsights.fxml", "User Engagement Insights");
    }

    private void handleViewGradeDistributionsButtonAction(ActionEvent event) {
        callScene(viewGradeDistributionsButton, "/fxml/ViewGradeDistributions.fxml", "Grade Distributions");
    }

    private void handleViewPerformanceMetricsButtonAction(ActionEvent event) {
        callScene(viewPerformanceMetricsButton, "/fxml/ViewPerformanceMetrics.fxml", "Performance Metrics");
    }

    private void handleEventManagementButtonAction(ActionEvent event) {
        callScene(eventManagementButton, "/fxml/SeeUpcomingEvents.fxml", "Event Management");
    }


    private void handleResourceManagementButtonAction(ActionEvent event) {
        callScene(resourceManagementButton, "/fxml/ResourceManagement.fxml", "Resource Management");
    }

    private void handleManageWaitlistsButtonAction(ActionEvent event) {
        callScene(manageWaitlistsButton, "/fxml/ManageWait-lists.fxml", "Manage Wait-lists");
    }

    private void handleAccessReportsAndDataExportButtonAction(ActionEvent event) {
        callScene(accessReportsAndDataExportButton, "/fxml/AccessReportsAndDataExport.fxml", "Access Reports and Data Export");
    }

    private void handleCreateSystemWideAnnouncementsButtonAction(ActionEvent event) {
        callScene(createSystemWideAnnouncementsButton, "/fxml/CreateSystemWideAnnouncements.fxml", "Create System-wide Announcements");
    }

    private void handleManageAnnouncementsButtonAction(ActionEvent event) {
        callScene(manageAnnouncementsButton, "/fxml/ManageAnnouncements.fxml", "Manage Announcements");
    }

    private void handleSendNotificationsButtonAction(ActionEvent event) {
        callScene(sendNotificationsButton, "/fxml/SendNotifications.fxml", "Send Notifications");
    }

    private void callScene(MFXButton viewButton, String fxmlPath, String title) {
        try {
            Stage dashboardStage = (Stage) viewButton.getScene().getWindow();
            dashboardStage.close();

            FXMLLoader manageLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent manageRoot = manageLoader.load();
            Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
            Scene manageScene = new Scene(manageRoot);

            Stage manageStage = new Stage();
            manageStage.initModality(Modality.WINDOW_MODAL);
            manageStage.initOwner(viewButton.getScene().getWindow());
            manageStage.setScene(manageScene);
            manageStage.getIcons().add(logo);
            manageStage.setResizable(false);
            manageStage.show();
            manageStage.setTitle(title);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void showLogoutScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LogginOut.fxml"));
            Parent root = loader.load();
            LogoutScreenController loadingController = loader.getController();

            Scene scene = new Scene(root);

            // Assuming loadingScene is the name of your Stage in LoadingScreen.fxml
            Stage logoutStage = new Stage();
            logoutStage.initModality(Modality.WINDOW_MODAL);
            logoutStage.setScene(scene);
            logoutStage.show();
            logoutStage.setTitle("Logging Out");
            logoutStage.setResizable(false);
            Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
            logoutStage.getIcons().add(logo);

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    for (int i = 100; i >= 0; i--) {
                        final int progress = i;
                        Platform.runLater(() -> loadingController.getProgressBar().setProgress(progress / 100.0));
                        Thread.sleep(30);
                    }
                    return null;
                }
            };


            task.setOnSucceeded(e -> {
                try {
                    showLoginPage();
                    logoutStage.close(); // Close the loading window
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            new Thread(task).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showLoginPage() throws IOException {
        FXMLLoader manageLoader = new FXMLLoader(getClass().getResource("/fxml/LoginPage.fxml"));
        Parent manageRoot = manageLoader.load();
        Scene manageScene = new Scene(manageRoot);

        Stage loginPage = new Stage();
        loginPage.setScene(manageScene);
        loginPage.show();
        loginPage.setTitle("Login");
        loginPage.setResizable(false);
        Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
        loginPage.getIcons().add(logo);

    }


    @FXML
    private void scrollVertically(double deltaY) {
        double newValue = scrollPane.getVvalue() - (deltaY / scrollPane.getHeight());
        scrollPane.setVvalue(newValue);
    }

    @FXML
    private void scrollHorizontally(double deltaX) {
        double newValue = scrollPane.getHvalue() - (deltaX / scrollPane.getWidth());
        scrollPane.setHvalue(newValue);
    }


}
