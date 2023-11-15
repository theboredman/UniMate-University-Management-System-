package controllers;

import application.CourseData;
import application.CourseFileLoader;
import application.DataFileLoader;
import application.UserData;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseManagementController {

    @FXML
    private MFXButton manageUserProfilesButton;

    @FXML
    private MFXButton registrationRequestsButton;

    @FXML
    private MFXButton courseManagementButton;

    @FXML
    private MFXButton viewCourseEnrollmentButton;

    @FXML
    private MFXButton viewTuitionAndFeeStatisticsButton;

    @FXML
    private MFXButton manageBillingButton;

    @FXML
    private MFXButton manageFinancialAidButton;

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
    private MFXButton dashBoardButton;

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
    private MFXButton manageFacultyProfilesButton;

    @FXML
    private MFXScrollPane scrollPane;
    @FXML
    private MFXNotificationCenter notificationCenter;

    @FXML
    private MenuItem logoutMenuItem;

    @FXML
    private MFXButton addCourseButton;
    @FXML
    private MFXButton removeCourseButton;
    @FXML
    private MFXButton saveButton;

    @FXML
    private TableView<CourseData> tableView;

    @FXML
    private TableColumn<CourseData, String> courseInitialColumn;

    @FXML
    private TableColumn<CourseData, String> courseNameColumn;

    @FXML
    private TableColumn<CourseData, String> descriptionColumn;

    @FXML
    private TableColumn<CourseData, Integer> creditColumn;

    @FXML
    private TableColumn<CourseData, Integer> totalSeatColumn;

    @FXML
    private TableColumn<CourseData, String> syllabusColumn;

    private ObservableList<CourseData> courseDataList = FXCollections.observableArrayList();

    private static void saveUserDataToFile(String filePath, List<CourseData> courseDataList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (CourseData courseData : courseDataList) {
                // Write each property separated by a comma
                writer.write(courseData.getCourseInitial() + "," +
                        courseData.getCourseName() + "," +
                        courseData.getDescription() + "," +
                        courseData.getCourseCredit() + "," +
                        courseData.getTotalSeat() + "," +
                        getSyllabusAvailability(courseData.getCourseInitial()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception based on your application's requirements
        }
    }

    private static String getSyllabusAvailability(String courseInitial) {
        String syllabusFilePath = "courseData/" + courseInitial + "/" + courseInitial + "_syllabus.pdf";
        File syllabusFile = new File(syllabusFilePath);

        if (syllabusFile.exists() && syllabusFile.isFile()) {
            return "Available";
        } else {
            return "N/A";
        }
    }

    private static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    file.delete();
                }
            }
        }
        folder.delete();
    }

    public void initialize() {

        tableView.setRowFactory(tv -> {
            TableRow<CourseData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    CourseData rowData = row.getItem();
                    tableView.getSelectionModel().select(rowData);
                }
            });
            return row;
        });

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
        manageFacultyProfilesButton.setOnAction(this::handleManageFacultyProfilesButtonAction);
        // Add event handlers to buttons, etc.
        addCourseButton.setOnAction(this::handleAddCourseButtonAction);
        removeCourseButton.setOnAction(this::handleRemoveCourseButtonAction);
        saveButton.setOnAction(this::handleSaveButtonAction);
        notificationCenter.setOnIconClicked(this::handleNotificationCenterAction);
        logoutMenuItem.setOnAction(this::handleLogoutMenuItemAction);


        tableView.setItems(allCourses());
        courseInitialColumn.setCellValueFactory(new PropertyValueFactory<>("courseInitial"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));
        totalSeatColumn.setCellValueFactory(new PropertyValueFactory<>("totalSeat"));
        syllabusColumn.setCellValueFactory(new PropertyValueFactory<>("syllabusFilePath"));

    }

    private ObservableList<CourseData> allCourses() {

        ObservableList<CourseData> dataList = FXCollections.observableArrayList();

        try {
            Path filePath = Paths.get("course_data.txt");
            BufferedReader reader = Files.newBufferedReader(filePath);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String courseInitial = parts[0].trim();
                    String courseName = parts[1].trim();
                    String description = parts[2].trim();
                    int credit = Integer.parseInt(parts[3].trim());
                    int totalSeat = Integer.parseInt(parts[4].trim());
                    String syllabus = parts[5].trim();

                    dataList.add(new CourseData(courseInitial, courseName, description, credit, totalSeat, syllabus));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    @FXML
    private void handleNotificationCenterAction(MouseEvent mouseEvent) {
    }

    // Define event handler methods here
    private void handleAddCourseButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddCoursePromp.fxml"));
            Parent root = loader.load();

            AddCoursePrompController addCoursePrompController = loader.getController();
            // Optionally, you can pass any necessary data to the controller here

            Stage popupStage = new Stage();
            Scene scene = new Scene(root);
            popupStage.setScene(scene);

            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Add Course");
            popupStage.setResizable(false);
            Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
            popupStage.getIcons().add(logo);

            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRemoveCourseButtonAction(ActionEvent event) {
        CourseData selectedCourse = tableView.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            // Display a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to remove the selected course?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Retrieve data from the selected course
                String courseInitialToRemove = selectedCourse.getCourseInitial();

                // Remove the course from the list
                courseDataList.remove(selectedCourse);

                // Remove the course from the table view
                tableView.getItems().remove(selectedCourse);

                // Delete the course's folder (assuming it is named with the course's initial)
                String courseFolderPath = "courseData/" + courseInitialToRemove;
                File courseFolder = new File(courseFolderPath);
                if (courseFolder.exists() && courseFolder.isDirectory()) {
                    deleteFolder(courseFolder);
                }
            }
        } else {
            // Inform the user that no course is selected
            Alert noCourseSelectedAlert = new Alert(Alert.AlertType.INFORMATION);
            noCourseSelectedAlert.setTitle("Information");
            noCourseSelectedAlert.setHeaderText(null);
            noCourseSelectedAlert.setContentText("Please select a course to remove.");
            noCourseSelectedAlert.showAndWait();
        }
    }

    private void handleSaveButtonAction(ActionEvent event) {
        // Save the course data to the file
        saveUserDataToFile("course_data.txt", tableView.getItems());

        // Show success message
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Changes saved successfully!");
        successAlert.showAndWait();
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
        loginPage.setTitle("Login Page");
        loginPage.setResizable(false);
        Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
        loginPage.getIcons().add(logo);
    }


    @FXML
    private void handleDashboardButtonAction(ActionEvent event) {
        callScene(dashBoardButton, "/fxml/Dashboard.fxml", "Dashboard");
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


}