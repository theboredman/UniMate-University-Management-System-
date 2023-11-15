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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;

public class CourseDetailsFacultyController implements Initializable {
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
    @FXML
    private MFXButton dashBoardButton;
    private String userId;
    @FXML
    private VBox vBox;
    private String facultyInitial;
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

        dashBoardButton.setOnAction(this::handleDashboardButtonAction);
        viewProfileButton.setOnAction(event -> handleViewProfileAction());
        takenCourseButton.setOnAction(event -> handleTakeCourseButtonAction());
        cancelClasseButton.setOnAction(event -> handleCancelClassButtonAction());
        newsFeedButton.setOnAction(event -> handleNewsFeedButtonAction());
        postManagementButton.setOnAction(event -> handlePostManagementButtonAction());
        submitGraadesButton.setOnAction(event -> handleSubmitGraadesButtonAction());
        AssignmentButton.setOnAction(event -> handleAssignmentButtonAction());
        viewUpcomingEventsButton.setOnAction(event -> handleViewUpcomingEventsButtonAction());
        resourceManagementButton.setOnAction(event -> handleResourceManagementButtonAction());
        AppointmentButton.setOnAction(event -> handleAppointmentButtonAction());
        viewAnnouncementsButton.setOnAction(event -> handleViewAnnouncementsButtonAction());

        logoutMenuItem.setOnAction(this::handleLogoutMenuItemAction);
        setFacultyInfoInMenuItem(userId);

    }

    private void loadAndDisplayPosts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("takenCourse_data.txt"))) {
            String line;
            List<HBox> postElements = new ArrayList<>();
            System.out.println(facultyInitial);

            while ((line = reader.readLine()) != null) {
                String[] postParts = line.split(",");

                if (postParts.length == 3 && postParts[2].equals(facultyInitial)) {
                    HBox postElement = createPostElement(postParts);

                    VBox.setVgrow(postElement, Priority.ALWAYS);

                    postElements.add(postElement);
                }
            }

            Collections.reverse(postElements);
            vBox.getChildren().addAll(0, postElements);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HBox createPostElement(String[] postParts) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PaneFaculty.fxml"));
            HBox postElement = loader.load();
            PaneFacultyController paneFacultyController = loader.getController();

            String courseInitial = postParts[0];
            String facultyInitial = postParts[2];

            paneFacultyController.setShowCourseInitial(courseInitial);
            paneFacultyController.setFacultyInitial(facultyInitial);
            paneFacultyController.setFacultyId(userId);
            paneFacultyController.setProfilePicturePath(profilePicturePath);


            return postElement;
        } catch (IOException e) {
            e.printStackTrace();
            return new HBox();
        }
    }

    private void handleViewProfileAction() {
        callScene(viewProfileButton, "/fxml/ViewFacultyProfile.fxml", "View Profile");
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

    private void handleViewAnnouncementsButtonAction() {
        callScene(viewAnnouncementsButton, "/fxml/SeeAnnouncement.fxml", "Announcement");
    }

    private void callScene(MFXButton viewButton, String fxmlPath, String title) {
        try {
            Stage dashboardStage = (Stage) viewButton.getScene().getWindow();
            dashboardStage.close();

            FXMLLoader manageLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent manageRoot = manageLoader.load();

            Object controller = manageLoader.getController();

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
            }

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
                    logoutStage.close();
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
        getFacultyInitials(userId);
        loadAndDisplayPosts();

        System.out.println("Faculty Initial: " + facultyInitial);

    }

    private String getFacultyInitials(String userId) {
        System.out.println("User ID: " + userId);
        String facultyInitial = null;
        File userFile = new File("FacultyData/" + userId + "/" + userId + ".txt");

        if (userFile.exists() && userFile.isFile()) {
            try (BufferedReader userFileReader = new BufferedReader(new FileReader(userFile))) {
                String line;
                while ((line = userFileReader.readLine()) != null) {
                    if (line.startsWith("Initial:")) {
                        facultyInitial = line.substring(8).trim();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return this.facultyInitial = facultyInitial;
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
        callScene(dashBoardButton, "/fxml/DashboardFaculty.fxml", "Dashboard");
    }
}
