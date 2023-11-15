package controllers;

import application.ScholarshipData;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageScholarshipsAdminController implements Initializable {
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
    private MFXButton dashBoardButton;

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
    private MFXButton approveButton;

    @FXML
    private MFXButton removeButton;

    @FXML
    private TableView<ScholarshipData> scholarshipDataTable;

    @FXML
    private TableColumn<ScholarshipData, String> idColumn;

    @FXML
    private TableColumn<ScholarshipData, String> nameColumn;

    @FXML
    private TableColumn<ScholarshipData, String> cgpaColumn;

    @FXML
    private TableColumn<ScholarshipData, String> passedCreditsColumn;

    @FXML
    private TableColumn<ScholarshipData, String> percentageColumn;

    private ObservableList<ScholarshipData> scholarshipDataList;

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

        manageUserProfilesButton.setOnAction(this::handleManageUserProfilesButtonAction);
        manageFacultyProfilesButton.setOnAction(this::handleManageFacultyProfilesButtonAction);
        registrationRequestsButton.setOnAction(this::handleRegistrationRequestsButtonAction);
        courseManagementButton.setOnAction(this::handleCourseManagementButtonAction);
        viewCourseEnrollmentButton.setOnAction(this::handleViewCourseEnrollmentButtonAction);
        viewTuitionAndFeeStatisticsButton.setOnAction(this::handleViewTuitionAndFeeStatisticsButtonAction);
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
        dashBoardButton.setOnAction(this::handleDashboardButtonAction);
        logoutMenuItem.setOnAction(this::handleLogoutMenuItemAction);

        scholarshipDataTable.setEditable(true);

        // Set each column to be editable

        percentageColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set up edit commit handlers
        percentageColumn.setOnEditCommit(this::handleEditCommit);


        scholarshipDataTable.setItems(getScholarshipData());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cgpaColumn.setCellValueFactory(new PropertyValueFactory<>("cgpa"));
        passedCreditsColumn.setCellValueFactory(new PropertyValueFactory<>("passedCredits"));
        percentageColumn.setCellValueFactory(new PropertyValueFactory<>("percentage"));


    }

    private void handleEditCommit(TableColumn.CellEditEvent<ScholarshipData, String> event) {
        TablePosition<ScholarshipData, String> position = event.getTablePosition();
        String newValue = event.getNewValue();

        int row = position.getRow();
        ScholarshipData scholarshipData = event.getTableView().getItems().get(row);

        if (position.getColumn() == 4) {
            scholarshipData.setPercentage(newValue);
            // Add cases for other columns as needed
        }

        // Save the changes back to the file (you may need to implement this)
        saveDataToFile();

        // Refresh the table to reflect the changes
        scholarshipDataTable.refresh();
    }

    private void saveDataToFile() {
        try {
            Path filePath = Paths.get("ManageScholarship_data.txt");
            BufferedWriter writer = Files.newBufferedWriter(filePath);

            // Iterate through the data and write each entry to the file
            for (ScholarshipData scholarshipData : scholarshipDataTable.getItems()) {
                String id = scholarshipData.getId();
                String name = scholarshipData.getName();
                String cgpa = scholarshipData.getCgpa();
                String passedCredits = scholarshipData.getPassedCredits();
                String percentage = scholarshipData.getPercentage();

                // Concatenate the fields with commas
                String line = String.join(", ", id, name, cgpa, passedCredits, percentage);

                // Write the line to the file
                writer.write(line);
                writer.newLine();
                // System.out.println("ID is "+id);
                //  System.out.println("Percentage is "+percentage);
                savePercentageToUserFile(id, percentage);
            }

            // Close the writer
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    private void savePercentageToUserFile(String userId, String percentage) {
        try {
            Path userFilePath = Paths.get("userData/" + userId + "/" + userId + ".txt");
            List<String> lines = Files.readAllLines(userFilePath);

            // Check if the "Percentage" field already exists
            boolean percentageFieldExists = false;
            for (String line : lines) {
                if (line.startsWith("Scholarship Percentage (%):")) {
                    percentageFieldExists = true;
                    // Replace the existing "Percentage" field
                    lines.set(lines.indexOf(line), "Scholarship Percentage (%): " + percentage);
                    break;
                }
            }

            // If "Percentage" field doesn't exist, create and add it at the end
            if (!percentageFieldExists) {
                lines.add("Scholarship Percentage (%): " + percentage);
            }

            // Write the updated lines back to the user file
            Files.write(userFilePath, lines);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    private ObservableList<ScholarshipData> getScholarshipData() {

        ObservableList<ScholarshipData> scholarshipData = FXCollections.observableArrayList();

        try {
            Path filePath = Paths.get("ManageScholarship_data.txt");
            BufferedReader reader = Files.newBufferedReader(filePath);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String cgpa = parts[2].trim();
                    String passedCredits = parts[3].trim();

                    // Handle the absence of the "Percentage" field
                    String percentage = (parts.length == 5) ? parts[4].trim() : "";

                    scholarshipData.add(new ScholarshipData(id, name, cgpa, passedCredits, percentage));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scholarshipData;
    }


    @FXML
    private void handleApproveButtonAction() {
        // Handle the action when the "Approve" button is clicked
    }

    @FXML
    private void handleRemoveButtonAction() {
        // Handle the action when the "Remove" button is clicked
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


    private void handleNewsFeedButtonAction(ActionEvent event) {
        callScene(newsFeedButton, "/fxml/NewsfeedStudent.fxml", "News Feed");
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

    @FXML
    public void handleDashboardButtonAction(ActionEvent event) {
        callScene(dashBoardButton, "/fxml/DashBoard.fxml", "Dashboard");
    }
}
