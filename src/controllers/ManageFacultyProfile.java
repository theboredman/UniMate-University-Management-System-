package controllers;

import application.CourseData;
import application.FacultyData;
import application.FacultyDataFileLoader;
import application.UserData;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageFacultyProfile implements Initializable {

    private static FacultyData userData;

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
    private MFXButton addButton;

    @FXML
    private MFXButton removeButton;

    @FXML
    private MFXTextField searchTextField;

    @FXML
    private MFXButton searchButton;

    @FXML
    private TableView<FacultyData> tableView;
    @FXML
    private TableColumn<FacultyData, String> idColumn;
    @FXML
    private TableColumn<FacultyData, String> initialColumn;

    @FXML
    private TableColumn<FacultyData, String> userNameColumn;
    @FXML
    private TableColumn<FacultyData, String> departmentColumn;
    @FXML
    private TableColumn<FacultyData, String> emailColumn;
    @FXML
    private TableColumn<FacultyData, String> passwordColumn;
    @FXML
    private TableColumn<FacultyData, String> phoneColumn;

    private ObservableList<FacultyData> facultyDataList = FXCollections.observableArrayList();

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

    private static void saveUserDataToFile(String filePath, List<FacultyData> facultyDataList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (FacultyData facultyData : facultyDataList) {
                // Assuming UserData has a method to convert data to a string format
                String facultyDataString = facultyData.toString();
                writer.write(facultyDataString);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception based on your application's requirements
        }
    }

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
            TableRow<FacultyData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    FacultyData rowData = row.getItem();
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
        logoutMenuItem.setOnAction(this::handleLogoutMenuItemAction);


        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        initialColumn.setCellValueFactory(new PropertyValueFactory<>("initial"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        idColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        initialColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        userNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        departmentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());


        idColumn.setOnEditCommit(this::handleIdTableCellEdit);
        initialColumn.setOnEditCommit(this::handleInitialTableCellEdit);
        userNameColumn.setOnEditCommit(this::handleUserNameTableCellEdit);
        departmentColumn.setOnEditCommit(this::handleDepartmentTableCellEdit);
        emailColumn.setOnEditCommit(this::handleEmailTableCellEdit);
        phoneColumn.setOnEditCommit(this::handlePhoneTableCellEdit);
        passwordColumn.setOnEditCommit(this::handlePasswordTableCellEdit);

        // Load faculty data from file
        List<FacultyData> loadedData = FacultyDataFileLoader.loadFacultyDataFromFile("faculty_data.txt");
        facultyDataList.addAll(loadedData);
        tableView.setItems(facultyDataList);
        tableView.setEditable(true);

    }

    @FXML
    private void handleIdTableCellEdit(TableColumn.CellEditEvent<FacultyData, String> event) {
        FacultyData facultyData = event.getRowValue();
        facultyData.setId(event.getNewValue());
        updateDataFile(); // Write the updated data back to the file
    }

    @FXML
    private void handleInitialTableCellEdit(TableColumn.CellEditEvent<FacultyData, String> event) {
        FacultyData facultyData = event.getRowValue();
        facultyData.setInitial(event.getNewValue());
        updateDataFile(); // Write the updated data back to the file
    }

    @FXML
    private void handleUserNameTableCellEdit(TableColumn.CellEditEvent<FacultyData, String> event) {
        FacultyData facultyData = event.getRowValue();
        facultyData.setUserName(event.getNewValue());
        updateDataFile(); // Write the updated data back to the file
    }

    @FXML
    private void handleDepartmentTableCellEdit(TableColumn.CellEditEvent<FacultyData, String> event) {
        FacultyData facultyData = event.getRowValue();
        facultyData.setDepartment(event.getNewValue());
        updateDataFile(); // Write the updated data back to the file
    }

    @FXML
    private void handleEmailTableCellEdit(TableColumn.CellEditEvent<FacultyData, String> event) {
        FacultyData facultyData = event.getRowValue();
        facultyData.setEmail(event.getNewValue());
        updateDataFile(); // Write the updated data back to the file
    }

    @FXML
    private void handlePhoneTableCellEdit(TableColumn.CellEditEvent<FacultyData, String> event) {
        FacultyData facultyData = event.getRowValue();
        facultyData.setPhoneNumber(event.getNewValue());
        updateDataFile(); // Write the updated data back to the file
    }


    // Add event handling methods here

    @FXML
    private void handlePasswordTableCellEdit(TableColumn.CellEditEvent<FacultyData, String> event) {
        FacultyData facultyData = event.getRowValue();
        facultyData.setPassword(event.getNewValue());
        updateDataFile(); // Write the updated data back to the file
    }

    private void updateDataFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("faculty_data.txt"))) {
            for (FacultyData facultyData : facultyDataList) {
                String facultyDataLine = String.format("%s,%s,%s,%s,%s,%s,%s\n",
                        facultyData.getId(),
                        facultyData.getInitial(),
                        facultyData.getUserName(),
                        facultyData.getDepartment(),
                        facultyData.getEmail(),
                        facultyData.getPhoneNumber(),
                        facultyData.getPassword()
                );
                writer.write(facultyDataLine);

                // Update individual user files
                try (BufferedWriter userWriter = new BufferedWriter(new FileWriter("FacultyData/" + facultyData.getId() + "/" + facultyData.getId() + ".txt"))) {
                    userWriter.write("ID: " + facultyData.getId() + "\n");
                    userWriter.write("Initial: " + facultyData.getInitial() + "\n");
                    userWriter.write("Username: " + facultyData.getUserName() + "\n");
                    userWriter.write("Department: " + facultyData.getDepartment() + "\n");
                    userWriter.write("Email: " + facultyData.getEmail() + "\n");
                    userWriter.write("Phone Number: " + facultyData.getPhoneNumber() + "\n");
                    userWriter.write("Password: " + facultyData.getPassword());
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception (e.g., show an error message)
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("An error occurred while updating the user data file.");
                    alert.showAndWait();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while updating the data file.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAddUserButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddFacultyPromp.fxml"));
            Parent root = loader.load();

            AddFacultyPromp addFacultyPromp = loader.getController();
            addFacultyPromp.setManageFacultyProfile(this);

            Stage popupStage = new Stage();
            Scene scene = new Scene(root);
            popupStage.setScene(scene);

            scene.getStylesheets().add(getClass().getResource("/css/AddUserPromp.css").toExternalForm());

            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Add Faculty");
            popupStage.setResizable(false);
            Image logo = new Image(new FileInputStream("src/logo/Logo.png"));
            popupStage.getIcons().add(logo);

            popupStage.showAndWait();

            facultyDataList.add(addFacultyPromp.getFacultyData());
            tableView.setItems(facultyDataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemoveButtonAction(ActionEvent event) {
        // Get the selected user
        FacultyData selectedUser = tableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Display a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to remove the selected user?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Remove the user from the table
                facultyDataList.remove(selectedUser);

                // Save the updated user data to the file
                saveUserDataToFile("faculty_data.txt", facultyDataList);

                // Delete the user's folder (assuming it is named with the user's ID)
                String userId = selectedUser.getId();
                String userFolderPath = "FacultyData/" + userId;

                File userFolder = new File(userFolderPath);
                if (userFolder.exists() && userFolder.isDirectory()) {
                    deleteFolder(userFolder);
                }
            }
        } else {
            // Inform the user that no user is selected
            Alert noUserSelectedAlert = new Alert(Alert.AlertType.INFORMATION);
            noUserSelectedAlert.setTitle("Information");
            noUserSelectedAlert.setHeaderText(null);
            noUserSelectedAlert.setContentText("Please select a user to remove.");
            noUserSelectedAlert.showAndWait();
        }
    }

    @FXML
    private void handleSearchButtonAction(ActionEvent event) {
        String searchText = searchTextField.getText().toLowerCase().trim();

        // Create a filtered list and bind it to the original list
        FilteredList<FacultyData> filteredList = new FilteredList<>(facultyDataList, facultyData ->
                facultyData.getId().toLowerCase().contains(searchText) ||
                        facultyData.getUserName().toLowerCase().contains(searchText) ||
                        facultyData.getDepartment().toLowerCase().contains(searchText) ||
                        facultyData.getEmail().toLowerCase().contains(searchText) ||
                        facultyData.getPhoneNumber().toLowerCase().contains(searchText)
        );

        // Bind the filtered list to the TableView
        tableView.setItems(filteredList);
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
    private void handleDashboardButtonAction(ActionEvent event) {
        callScene(dashBoardButton, "/fxml/Dashboard.fxml", "Dashboard");
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

    public ObservableList<FacultyData> getFacultyDataList() {
        return facultyDataList;
    }

    public void setFacultyDataList(ObservableList<FacultyData> facultyDataList) {
        this.facultyDataList = facultyDataList;
    }

    public void addFacltyData(FacultyData userData) {
        facultyDataList.add(userData);
    }

    // Add other methods as needed
}
