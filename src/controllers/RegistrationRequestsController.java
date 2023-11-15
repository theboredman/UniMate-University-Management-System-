package controllers;

import application.DataFileLoader;
import application.RegistrationRequest;
import application.UserData;
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

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RegistrationRequestsController implements Initializable {

    private static UserData userData;

    @FXML
    private MFXButton dashBoardButton;


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
    private TableView<UserData> tableView;
    @FXML
    private TableColumn<UserData, String> idColumn;
    @FXML
    private TableColumn<UserData, String> userNameColumn;
    @FXML
    private TableColumn<UserData, String> departmentColumn;
    @FXML
    private TableColumn<UserData, String> emailColumn;
    @FXML
    private TableColumn<UserData, String> passwordColumn;
    @FXML
    private TableColumn<UserData, String> phoneColumn;
    @FXML
    private TableColumn<UserData, String> cgpaColumn;
    @FXML
    private TableColumn<UserData, String> passedCreditColumn;
    private ObservableList<UserData> registerDataList = FXCollections.observableArrayList();

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

        tableView.setRowFactory(tv -> {
            TableRow<UserData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    UserData rowData = row.getItem();
                    tableView.getSelectionModel().select(rowData);
                }
            });
            return row;
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

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        cgpaColumn.setCellValueFactory(new PropertyValueFactory<>("cgpa"));
        passedCreditColumn.setCellValueFactory(new PropertyValueFactory<>("passedCredit"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        List<UserData> loadedData = DataFileLoader.loadUserDataFromFile("registration.txt");

        registerDataList.addAll(loadedData);

        tableView.setItems(registerDataList);
    }


    @FXML
    private void handleApproveButtonAction(ActionEvent event) {
        // Get the selected item from the TableView
        UserData selectedUserData = tableView.getSelectionModel().getSelectedItem();

        if (selectedUserData != null) {
            // Save the approved user data to user_data.txt
            saveUserDataToFile(selectedUserData, "user_data.txt");

            // Remove from the TableView
            tableView.getItems().remove(selectedUserData);

            // Remove from the registerDataList
            registerDataList.remove(selectedUserData);

            // Save the updated registerDataList back to registration.txt after removal
            saveDataListToFile(registerDataList, "registration.txt");
        }
    }


    @FXML
    private void handleRejectButtonAction(ActionEvent event) {

    }


    @FXML
    private void handleLogoutMenuItemAction() {
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


    public ObservableList<UserData> getUserDataList() {
        return registerDataList;
    }

    public void setUserDataList(ObservableList<UserData> userDataList) {
        this.registerDataList = userDataList;
    }

    public void addUserData(UserData userData) {
        registerDataList.add(userData);
    }


    public void handleNotificationCenterAction(MouseEvent mouseEvent) {
    }

    private void saveUserDataToFile(UserData userData, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            if (new File(filePath).length() != 0) {
                // Add a newline before each entry (if the file is not empty)
                writer.println();
            }
            writer.print(userData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDataListToFile(List<UserData> dataList, String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            for (UserData userData : dataList) {
                writer.println(userData.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
