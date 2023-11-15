package controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ResourceBundle;

public class SeeAnnouncementController implements Initializable {

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
    private MFXButton cancelClasseButton;

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
        viewProfileButton.setOnAction(event -> handleViewProfileAction());
        takenCourseButton.setOnAction(event -> handleTakeCourseButtonAction());
        viewCourseDetailsButton.setOnAction(event -> handleViewCourseDetailsButtonAction());
        cancelClasseButton.setOnAction(event -> handleCancelClassButtonAction());
        newsFeedButton.setOnAction(event -> handleNewsFeedButtonAction());
        postManagementButton.setOnAction(event -> handlePostManagementButtonAction());
        submitGraadesButton.setOnAction(event -> handleSubmitGraadesButtonAction());
        AssignmentButton.setOnAction(event -> handleAssignmentButtonAction());
        viewUpcomingEventsButton.setOnAction(event -> handleViewUpcomingEventsButtonAction());
        resourceManagementButton.setOnAction(event -> handleResourceManagementButtonAction());
        AppointmentButton.setOnAction(event -> handleAppointmentButtonAction());
        // Set up the menu item action
        logoutMenuItem.setOnAction(event -> handleLogoutMenuItemAction());
        setFacultyInfoInMenuItem(userId);

    }

    private void handleViewProfileAction() {
        callScene(viewProfileButton, "/fxml/ViewFacultyProfile.fxml", "View Profile");
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
        callScene(cancelClasseButton, "/fxml/CancelClass.fxml", "Cancel Class");
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
    private void handleLogoutMenuItemAction() {
        // Implement the action for logout menu item
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


    // write one method for closing scene to enter another scene
    private void closeStage(Stage stage) {
        stage.close();
    }


    public void handleNotificationCenterAction(MouseEvent mouseEvent) {
    }

    public void setStudentId(String userId) throws FileNotFoundException {
        this.userId = userId;
        setFacultyInfoInMenuItem(userId);
    }
}
