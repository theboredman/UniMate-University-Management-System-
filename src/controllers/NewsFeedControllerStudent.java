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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;

public class NewsFeedControllerStudent implements Initializable {
    @FXML
    private ImageView profileImageView;

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
    private MFXButton courseEnrollmentButton;

    @FXML
    private MFXButton viewCourseDetailsButton;

    @FXML
    private MFXButton viewTuitionFeeButton;

    @FXML
    private MFXButton manageScholarshipsButton;

    @FXML
    private MFXButton newsFeedButton;

    @FXML
    private MFXButton postManagementButton;
    @FXML
    private MFXButton viewGradesButton;
    @FXML
    private MFXButton submitAssignmentButton;
    @FXML
    private MFXButton trackAcademicProgressButton;
    @FXML
    private MFXButton viewUpcomingEventsButton;
    @FXML
    private MFXButton resourceManagementButton;
    @FXML
    private MFXButton makeAppointmentButton;
    @FXML
    private MFXButton viewAnnouncementsButton;
    @FXML
    private MFXButton dashBoardButton;
    @FXML
    private MFXButton addPostsButton;

    @FXML
    private VBox vBox;

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
        viewProfileButton.setOnAction(event -> handleViewProfileButtonAction());
        courseEnrollmentButton.setOnAction(event -> handleCourseEnrollmentButtonAction());
        viewCourseDetailsButton.setOnAction(event -> handleViewCourseDetailsButtonAction());
        viewTuitionFeeButton.setOnAction(event -> handleViewTuitionFeeButtonAction());
        manageScholarshipsButton.setOnAction(event -> handleManageScholarshipsButtonAction());
        newsFeedButton.setOnAction(event -> handleNewsFeedButtonAction());
        postManagementButton.setOnAction(event -> handlePostManagementButtonAction());
        viewGradesButton.setOnAction(event -> handleViewGradesButtonAction());
        submitAssignmentButton.setOnAction(event -> handleSubmitAssignmentButtonAction());
        trackAcademicProgressButton.setOnAction(event -> handleTrackAcademicProgressButtonAction());
        viewUpcomingEventsButton.setOnAction(event -> handleViewUpcomingEventsButtonAction());
        resourceManagementButton.setOnAction(event -> handleResourceManagementButtonAction());
        makeAppointmentButton.setOnAction(event -> handleMakeAppointmentButtonAction());
        viewAnnouncementsButton.setOnAction(event -> handleViewAnnouncementsButtonAction());
        // Set up the menu item action
        logoutMenuItem.setOnAction(event -> handleLogoutMenuItemAction());
        setStudentInfoInMenuItem(userId);


        // Load and display posts initially
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
            addPostsController.setUserId(userId); // pass the user ID
            addPostsController.setUserName(getUserName()); // pass the user name
            addPostsController.setProfilePicturePath(profilePicturePath);

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

    private String getUserName() {
        String userName = null;
        File userDataFile = new File("userData/" + userId + "/" + userId + ".txt");

        if (userDataFile.exists() && userDataFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(userDataFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Username:")) {
                        userName = line.substring(10).trim();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return userName;
    }

    // Define your button action methods here
    private void handleViewProfileButtonAction() {
        callScene(viewProfileButton, "/fxml/ViewProfile.fxml", "View Profile");
    }

    private void handleCourseEnrollmentButtonAction() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Message");
        alert.setHeaderText(null);
        alert.setContentText("Once you saved your course enrollment, you cannot change it again.");
        alert.showAndWait();
        callScene(courseEnrollmentButton, "/fxml/CourseEnrollmentStudent.fxml", "Course Enrollment");
    }

    private void handleViewCourseDetailsButtonAction() {
        // Implement the action for view course details button
        callScene(viewCourseDetailsButton, "/fxml/ViewCourseDetailsStudent.fxml", "View Course Details");
    }

    private void handleViewTuitionFeeButtonAction() {
        // Implement the action for view tuition fee button
        callScene(viewTuitionFeeButton, "/fxml/ViewTuitionFee.fxml", "View Tuition Fee");
    }

    private void handleManageScholarshipsButtonAction() {
        // Implement the action for manage scholarships button
        callScene(manageScholarshipsButton, "/fxml/ManageScholarship.fxml", "Manage Scholarships");
    }

    private void handleNewsFeedButtonAction() {
        // Implement the action for news feed button
        callScene(newsFeedButton, "/fxml/NewsfeedStudent.fxml", "News Feed");
    }

    private void handlePostManagementButtonAction() {
        // Implement the action for post management button
        callScene(postManagementButton, "/fxml/PostManagement.fxml", "Post Management");
    }

    private void handleViewGradesButtonAction() {
        // Implement the action for view grades button
        callScene(viewGradesButton, "/fxml/ViewGrades.fxml", "View Grades");
    }

    private void handleSubmitAssignmentButtonAction() {
        // Implement the action for submit assignment button
        callScene(submitAssignmentButton, "/fxml/SubmitAssignment.fxml", "Submit Assignment");
    }

    private void handleTrackAcademicProgressButtonAction() {
        // Implement the action for track academic progress button
        callScene(trackAcademicProgressButton, "/fxml/TracAcademicProgress.fxml", "Track Academic Progress");
    }

    private void handleViewUpcomingEventsButtonAction() {
        // Implement the action for view upcoming events button
        callScene(viewUpcomingEventsButton, "/fxml/SeeUpcomingEvents.fxml", "View Upcoming Events");
    }

    private void handleResourceManagementButtonAction() {
        // Implement the action for resource management button
        callScene(resourceManagementButton, "/fxml/ResourceManagement.fxml", "Resource Management");
    }

    private void handleMakeAppointmentButtonAction() {
        // Implement the action for make appointment button
        callScene(makeAppointmentButton, "/fxml/MakeAppointment.fxml", "Make Appointment");
    }

    private void handleViewAnnouncementsButtonAction() {
        // Implement the action for view announcements button
        callScene(viewAnnouncementsButton, "/fxml/SeeAnnouncement.fxml", "View Announcements");
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

    void setStudentInfoInMenuItem(String userId) {
        File userDirectory = new File("userData/" + userId);

        if (userDirectory.exists() && userDirectory.isDirectory()) {
            File[] imageFiles = userDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));

            if (imageFiles != null && imageFiles.length > 0) {
                // Sort the files by name in descending order to get the latest profile picture
                Arrays.sort(imageFiles, Comparator.comparing(File::getName));
                File latestProfilePicture = imageFiles[imageFiles.length - 1];

                try {
                    File userFile = new File(userDirectory, userId + ".txt");

                    try (BufferedReader userFileReader = new BufferedReader(new FileReader(userFile))) {
                        String line;
                        String studentName = null;

                        while ((line = userFileReader.readLine()) != null) {
                            if (line.startsWith("Username:")) {
                                studentName = line.substring(10).trim();
                                break;
                            }
                        }

                        if (studentName != null) {
                            String profilePicturePath = latestProfilePicture.getPath();

                            // Set the student name and profile picture in the menu item
                            userMenuButton.setText(studentName);
                            Image profileImage = new Image(new FileInputStream(profilePicturePath));
                            ImageView profileImageView = new ImageView(profileImage);

                            Circle clip = new Circle(25, 25, 25);
                            profileImageView.setClip(clip);
                            profileImageView.setFitWidth(50);
                            profileImageView.setFitHeight(50);
                            profileImageView.setLayoutX(0);
                            profileImageView.setLayoutY(0);
                            userMenuButton.setGraphic(profileImageView);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setStudentId(String userId) {
        this.userId = userId;
        setStudentInfoInMenuItem(userId);
    }

    private void callScene(MFXButton viewButton, String fxmlPath, String title) {
        try {
            Stage dashboardStage = (Stage) viewButton.getScene().getWindow();
            dashboardStage.close();

            FXMLLoader manageLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent manageRoot = manageLoader.load();

            // Get the controller
            Object controller = manageLoader.getController();


            if (controller instanceof ViewProfileController viewProfileController) {
                viewProfileController.setStudentId(userId);
            } else if (controller instanceof CourseEnrollmentStudent courseEnrollmentStudent) {
                courseEnrollmentStudent.setStudentId(userId);
            } else if (controller instanceof ViewCourseDetalisStudentController viewCourseDetalisStudentController) {
                viewCourseDetalisStudentController.setStudentId(userId);
            } else if (controller instanceof ViewTutionFeeController viewTutionFeeController) {
                viewTutionFeeController.setStudentId(userId);
            } else if (controller instanceof ManageBillingController manageBillingController) {
                manageBillingController.setStudentId(userId);
            } else if (controller instanceof ManageScholarshipController manageScholarshipController) {
                manageScholarshipController.setStudentId(userId);
            } else if (controller instanceof NewsFeedControllerStudent newsFeedController) {
                newsFeedController.setStudentId(userId);
            } else if (controller instanceof PostManagementControllerStudent postManagementController) {
                postManagementController.setStudentId(userId);
            } else if (controller instanceof ViewGradesController viewGradesController) {
                viewGradesController.setStudentId(userId);
            } else if (controller instanceof SubmitAssignmentController submitAssignmentController) {
                submitAssignmentController.setStudentId(userId);
            } else if (controller instanceof TracAcademicProgressController trackAcademicProgressController) {
                trackAcademicProgressController.setStudentId(userId);
            } else if (controller instanceof SeeUpcomingEventsControllerStudent viewUpcomingEventsController) {
                viewUpcomingEventsController.setStudentId(userId);
            } else if (controller instanceof ResourceManagementControllerStudent resourceManagementController) {
                resourceManagementController.setStudentId(userId);
            } else if (controller instanceof MakeAppoinmnetController makeAppointmentController) {
                makeAppointmentController.setStudentId(userId);
            } else if (controller instanceof SeeAnnouncementControllerStudent viewAnnouncementsController) {
                viewAnnouncementsController.setStudentId(userId);
            }

            String profilePicturePath = "userData/" + userId + "/" + userId + ".jpg";

            if (controller instanceof ViewProfileController viewProfileController) {
                viewProfileController.setProfilePicturePath(profilePicturePath);
            } else if (controller instanceof CourseEnrollmentStudent courseEnrollmentStudent) {
                courseEnrollmentStudent.setProfilePicturePath(profilePicturePath);

            } else if (controller instanceof ViewCourseDetalisStudentController viewCourseDetalisStudentController) {
                viewCourseDetalisStudentController.setProfilePicturePath(profilePicturePath);

            } else if (controller instanceof ViewTutionFeeController viewTutionFeeController) {
                viewTutionFeeController.setProfilePicturePath(profilePicturePath);

            } else if (controller instanceof ManageBillingController manageBillingController) {
                manageBillingController.setProfilePicturePath(profilePicturePath);

            } else if (controller instanceof ManageScholarshipController manageScholarshipController) {
                manageScholarshipController.setProfilePicturePath(profilePicturePath);

            } else if (controller instanceof NewsFeedControllerStudent newsFeedController) {
                newsFeedController.setProfilePicturePath(profilePicturePath);

            } else if (controller instanceof PostManagementControllerStudent postManagementController) {
                postManagementController.setProfilePicturePath(profilePicturePath);

            } else if (controller instanceof ViewGradesController viewGradesController) {
                viewGradesController.setProfilePicturePath(profilePicturePath);

            } else if (controller instanceof SubmitAssignmentController submitAssignmentController) {
                submitAssignmentController.setProfilePicturePath(profilePicturePath);

            } else if (controller instanceof TracAcademicProgressController trackAcademicProgressController) {
                trackAcademicProgressController.setProfilePicturePath(profilePicturePath);

            } else if (controller instanceof SeeUpcomingEventsControllerStudent viewUpcomingEventsController) {
                viewUpcomingEventsController.setProfilePicturePath(profilePicturePath);

            } else if (controller instanceof ResourceManagementControllerStudent resourceManagementController) {
                resourceManagementController.setProfilePicturePath(profilePicturePath);

            } else if (controller instanceof MakeAppoinmnetController makeAppointmentController) {
                makeAppointmentController.setProfilePicturePath(profilePicturePath);

            } else if (controller instanceof SeeAnnouncementControllerStudent viewAnnouncementsController) {
                viewAnnouncementsController.setProfilePicturePath(profilePicturePath);
            }


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

    @FXML
    public void handleDashboardButtonAction(ActionEvent event) {
        callScene(dashBoardButton, "/fxml/StudentDashboard.fxml", "Dashboard");
    }
}
