package controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXContextMenuItem;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewFacultyProfileController implements Initializable {
    public MFXButton updateProfileButton;
    @FXML
    private MFXContextMenuItem showUsername, showPhoneNumber, showID, showGender, showDateofBirth, showAddress, showMail, showDepartment,
            totalCourseCount, totalCredits, officeHour, joiningDate;


    @FXML
    private ImageView profileImageView, profileImageView2;

    @FXML
    private MFXNotificationCenter notificationCenter;

    @FXML
    private MenuButton userMenuButton;

    @FXML
    private MenuItem logoutMenuItem;

    @FXML
    private MFXScrollPane scrollPane;


    @FXML
    private MFXButton viewProfileButton;

    @FXML
    private MFXButton takenCourseButton;

    @FXML
    private MFXButton viewCourseDetailsButton;

    @FXML
    private MFXButton cancelCourseButton;

    @FXML
    private MFXButton newsFeedButton;

    @FXML
    private MFXButton postManagementButton;
    @FXML
    private MFXButton submitGraadesButton;
    @FXML
    private MFXButton viewUpcomingEventsButton;
    @FXML
    private MFXButton AssignmentButton;
    @FXML
    private MFXButton resourceManagementButton;
    @FXML
    private MFXButton AppointmentButton;
    @FXML
    private MFXButton viewAnnouncementsButton;

    private String userId;
    private String profilePicturePath;

    private static ImageView getImageView(String profilePicturePath) throws FileNotFoundException {
        Image profileImage = new Image(new FileInputStream(profilePicturePath));
        ImageView profileImageView = new ImageView(profileImage);

        Circle clip = new Circle(25, 25, 25);
        profileImageView.setClip(clip);
        profileImageView.setFitWidth(50);
        profileImageView.setFitHeight(50);
        profileImageView.setLayoutX(0);
        profileImageView.setLayoutY(0);
        return profileImageView;
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

        // Set up your button actions here using lambda expressions
        takenCourseButton.setOnAction(event -> handleTakeCourseButtonAction());
        viewCourseDetailsButton.setOnAction(event -> handleViewCourseDetailsButtonAction());
        cancelCourseButton.setOnAction(event -> handleCancelClassButtonAction());
        newsFeedButton.setOnAction(event -> handleNewsFeedButtonAction());
        postManagementButton.setOnAction(event -> handlePostManagementButtonAction());
        submitGraadesButton.setOnAction(event -> handleSubmitGraadesButtonAction());
        AssignmentButton.setOnAction(event -> handleAssignmentButtonAction());
        viewUpcomingEventsButton.setOnAction(event -> handleViewUpcomingEventsButtonAction());
        resourceManagementButton.setOnAction(event -> handleResourceManagementButtonAction());
        AppointmentButton.setOnAction(event -> handleAppointmentButtonAction());
        viewAnnouncementsButton.setOnAction(event -> handleViewAnnouncementsButtonAction());
        // Set up the menu item action
        logoutMenuItem.setOnAction(this::handleLogoutMenuItemAction);
        setFacultyInfoInMenuItem(userId);

    }

    public void displayUserData(String userId) throws FileNotFoundException {
        File userFile = new File("FacultyData/" + userId + "/" + userId + ".txt");
        //String profilePicturePath = "userData/" + userId + "/" + userId + ".jpg";
        //  Image profileImage = new Image(new FileInputStream(profilePicturePath));
        //profileImageView.setImage(profileImage);

        if (userFile.exists()) {

            try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(": ");
                    if (parts.length == 2) {
                        String field = parts[0].trim();
                        String value = parts[1].trim();

                        String formattedData = field + ": " + value;

                        switch (field) {
                            case "ID":
                                showID.setText(formattedData);
                                break;
                            case "Username":
                                showUsername.setText(formattedData);
                                break;
                            case "Department":
                                showDepartment.setText(formattedData);
                                break;
                            case "Email":
                                showMail.setText(formattedData);
                                break;
                            case "Phone Number":
                                // Assuming you have a label named `showPhoneNumber`
                                showPhoneNumber.setText(formattedData);
                                break;
                            case "Total Course Count":
                                totalCourseCount.setText(formattedData);
                                break;
                            case "Total Credits":
                                totalCredits.setText(formattedData);
                                break;
                            case "Gender":
                                showGender.setText(formattedData);
                                break;
                            case "Office Hour":
                                officeHour.setText(formattedData);
                                break;
                            case "Address":
                                showAddress.setText(formattedData);
                                break;
                            case "Joining Date":
                                joiningDate.setText(formattedData);
                                break;
                            case "Date of Birth":
                                showDateofBirth.setText(formattedData);
                                break;
                            // Add more cases for other fields as needed
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (e.g., show an error message)
            }
        }
    }

    public void handleUpdateProfileButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UpdateFacultyProfile.fxml"));
            Parent root = loader.load();

            UpdateFacultyProfile controller = loader.getController();
            controller.setUserId(userId); // Set the user ID here

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleChangePasswordButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChangePasswordFaculty.fxml"));
            Parent root = loader.load();

            ChangePassword controller = loader.getController();
            controller.setUserId(userId); // Pass the userId to ChangePassword controller

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleChangeProfilePictureButtonAction(ActionEvent event) {
        // Show a file chooser dialog to select a new profile picture
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                // Save the selected profile picture as "new_userId.jpg"
                String newProfilePicturePath = "FacultyData/" + userId + "/new_" + userId + ".jpg";
                Files.copy(selectedFile.toPath(), new File(newProfilePicturePath).toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Display alert box informing the user about the update
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Profile Picture Updated");
                alert.setHeaderText(null);
                alert.setContentText("Profile picture updated. Please log in again.");
                alert.showAndWait();

                // Take the user back to the login scene (replace this with your code to switch scenes)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FacultyLogin.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();


            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (e.g., show an error message)
            }
        }


    }

    private void handleViewCourseDetailsButtonAction() {
        callScene(viewCourseDetailsButton, "/fxml/CourseDetailsFaculty.fxml", "Course Details");

    }

    private void handleTakeCourseButtonAction() {
        callScene(takenCourseButton, "/fxml/TakeCourse.fxml", "Taken Course");
    }

    private void handleAppointmentButtonAction() {
        callScene(AppointmentButton, "/fxml/SeeAppointments.fxml", "Appointments");
    }

    private void handleNewsFeedButtonAction() {
        callScene(newsFeedButton, "/fxml/NewsfeedFaculty.fxml", "NewsFeed");

    }

    private void handlePostManagementButtonAction() {
        callScene(postManagementButton, "/fxml/PostManagement.fxml", "NewsFeed");

    }

    private void handleCancelClassButtonAction() {
        callScene(cancelCourseButton, "/fxml/CancelClass.fxml", "Cancel Class");
    }

    private void handleSubmitGraadesButtonAction() {
        callScene(submitGraadesButton, "/fxml/SubmitGrade.fxml", "Submit Grade");
    }

    private void handleViewUpcomingEventsButtonAction() {
        callScene(viewUpcomingEventsButton, "/fxml/SeeUpcomingEvents.fxml", "Upcoming Events");
    }

    private void handleResourceManagementButtonAction() {
        callScene(resourceManagementButton, "/fxml/ResourceManagement.fxml", "Resource Management");
    }

    private void handleAssignmentButtonAction() {
        callScene(AssignmentButton, "/fxml/Assignment.fxml", "Assignment");
    }

    private void handleViewAnnouncementsButtonAction() {
        callScene(viewAnnouncementsButton, "/fxml/SeeAnnouncement.fxml", "Announcement");
    }

    private void callScene(MFXButton viewButton, String fxmlPath, String title) {
        try {
            Stage dashboardStage = (Stage) viewButton.getScene().getWindow();
            dashboardStage.close();

            FXMLLoader manageLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent manageRoot = manageLoader.load();

            // Get the controller
            Object controller = manageLoader.getController();

            // Check the type of controller and set properties accordingly
            if (controller instanceof ViewFacultyProfileController viewFacultyProfileController) {
                viewFacultyProfileController.setFacultyId(userId);
            } else if (controller instanceof TakeCourseController takeCourseController) {
                takeCourseController.setFacultyId(userId);
            } else if (controller instanceof CourseDetailsFacultyController courseDetailsController) {
                courseDetailsController.setFacultyId(userId);
            } else if (controller instanceof SeeAppointmentsController appointmentsController) {
                appointmentsController.setFacultyId(userId);
            } else if (controller instanceof NewsFeedControllerFaculty newsFeedControllerFaculty) {
                newsFeedControllerFaculty.setFacultyId(userId);
            } else if (controller instanceof PostManagementController postManagementController) {
                postManagementController.setFacultyId(userId);
            } else if (controller instanceof CancelClassController cancelClassController) {
                cancelClassController.setFacultyId(userId);
            } else if (controller instanceof SubmitGradeController submitGradeController) {
                submitGradeController.setFacultyId(userId);
            } else if (controller instanceof SeeUpcomingEventsController upcomingEventsController) {
                upcomingEventsController.setFacultyId(userId);
            } else if (controller instanceof ResourceManagementController resourceManagementController) {
                resourceManagementController.setFacultyId(userId);
            } else if (controller instanceof AssignmentController assignmentController) {
                assignmentController.setFacultyId(userId);
            } else if (controller instanceof SeeAnnouncementController announcementController) {
                announcementController.setFacultyId(userId);
            } // Add similar else if blocks for other controllers as needed
            // Add similar else if blocks for other controllers as needed

            String profilePicturePath = "FacultyData/" + userId + "/" + userId + ".jpg";

            if (controller instanceof ViewProfileController viewProfileController) {
                viewProfileController.setProfilePicturePath(profilePicturePath);
            } else if (controller instanceof TakeCourseController takeCourseController) {
                takeCourseController.setProfilePicturePath(profilePicturePath);
            } else if (controller instanceof CourseDetailsFacultyController courseDetailsController) {
                courseDetailsController.setProfilePicturePath(profilePicturePath);
            } else if (controller instanceof SeeAppointmentsController appointmentsController) {
                appointmentsController.setProfilePicturePath(profilePicturePath);
            } else if (controller instanceof NewsFeedControllerFaculty newsFeedControllerFaculty) {
                newsFeedControllerFaculty.setProfilePicturePath(profilePicturePath);
            } else if (controller instanceof PostManagementController postManagementController) {
                postManagementController.setProfilePicturePath(profilePicturePath);
            } else if (controller instanceof CancelClassController cancelClassController) {
                cancelClassController.setProfilePicturePath(profilePicturePath);
            } else if (controller instanceof SubmitGradeController submitGradeController) {
                submitGradeController.setProfilePicturePath(profilePicturePath);
            } else if (controller instanceof SeeUpcomingEventsController upcomingEventsController) {
                upcomingEventsController.setProfilePicturePath(profilePicturePath);
            } else if (controller instanceof ResourceManagementController resourceManagementController) {
                resourceManagementController.setProfilePicturePath(profilePicturePath);
            } else if (controller instanceof AssignmentController assignmentController) {
                assignmentController.setProfilePicturePath(profilePicturePath);
            } else if (controller instanceof SeeAnnouncementController announcementController) {
                announcementController.setProfilePicturePath(profilePicturePath);
            } // Add similar else if blocks for other controllers as needed
            // Add similar else if blocks for other controllers as needed
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

    void setFacultyInfoInMenuItem(String userId) {
        File userDirectory = new File("FacultyData/" + userId);

        if (userDirectory.exists() && userDirectory.isDirectory()) {
            File[] imageFiles = userDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));

            if (imageFiles != null && imageFiles.length > 0) {
                Arrays.sort(imageFiles, Comparator.comparing(File::getName));
                File latestProfilePicture = imageFiles[imageFiles.length - 1];

                try {
                    File userFile = new File(userDirectory, userId + ".txt");

                    try (BufferedReader userFileReader = new BufferedReader(new FileReader(userFile))) {
                        String line;
                        String facultyName = null;

                        while ((line = userFileReader.readLine()) != null) {
                            if (line.startsWith("Username:")) {
                                facultyName = line.substring(10).trim();
                                break;
                            }
                        }

                        if (facultyName != null) {
                            String profilePicturePath = latestProfilePicture.getPath();

                            userMenuButton.setText(facultyName);
                            ImageView profileImageView = getImageView(profilePicturePath);
                            userMenuButton.setGraphic(profileImageView);

                            // Set profileImageView to use the same image as profileImageView2
                            Image profileImage = new Image(new FileInputStream(profilePicturePath));
                            profileImageView2.setImage(profileImage);
                            // shape will be square with size 50x50
                            Rectangle rectangle = new Rectangle(250, 250);
                            profileImageView2.setClip(rectangle);
                            profileImageView2.setFitWidth(400);
                            profileImageView2.setFitHeight(200);
                            profileImageView2.setLayoutX(0);
                            profileImageView2.setLayoutY(0);


                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setFacultyId(String userId) throws FileNotFoundException {
        this.userId = userId;
        setFacultyInfoInMenuItem(userId);
        displayUserData(userId); // Add this line
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
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

    public void handleNotificationCenterAction(MouseEvent mouseEvent) {
    }

    public void handleDashboardButtonAction(ActionEvent event) {
    }


}
