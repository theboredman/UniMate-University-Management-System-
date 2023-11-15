package controllers;

import application.CourseData;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class TakeCourseController implements Initializable {
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

    @FXML
    private ImageView profileImageView;


    @FXML
    private TableView<CourseData> offeredCourseTable;

    @FXML
    private TableColumn<CourseData, String> offeredCourseInitial;

    @FXML
    private TableColumn<CourseData, Integer> offeredCourseCredit;

    @FXML
    private TableView<CourseData> takenCourseTable;

    @FXML
    private TableColumn<CourseData, String> takenCourseInitial;

    @FXML
    private TableColumn<CourseData, Integer> takenCourseCredit;

    @FXML
    private MFXButton addCourseButton;

    @FXML
    private MFXButton removeCourseButton;

    @FXML
    private MFXButton saveCourseButton;
    private String profilePicturePath;
    private String userId;

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
        dashBoardButton.setOnAction(event -> handleDashboardButtonAction());
        viewProfileButton.setOnAction(event -> handleViewProfileAction());
        viewCourseDetailsButton.setOnAction(event -> handleViewCourseDetailsButtonAction());
        cancelClasseButton.setOnAction(event -> handleCancelClassButtonAction());
        newsFeedButton.setOnAction(event -> handleNewsFeedButtonAction());
        postManagementButton.setOnAction(event -> handlePostManagementButtonAction());
        submitGraadesButton.setOnAction(event -> handleSubmitGraadesButtonAction());
        AssignmentButton.setOnAction(event -> handleAssignmentButtonAction());
        viewUpcomingEventsButton.setOnAction(event -> handleViewUpcomingEventsButtonAction());
        resourceManagementButton.setOnAction(event -> handleResourceManagementButtonAction());
        AppointmentButton.setOnAction(event -> handleAppointmentButtonAction());
        viewAnnouncementsButton.setOnAction(event -> handleViewAnnouncementsButtonAction());


        offeredCourseTable.setItems(getOfferedCourses());
        offeredCourseInitial.setCellValueFactory(new PropertyValueFactory<>("courseInitial"));
        offeredCourseCredit.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));

        takenCourseTable.setItems(getTakenCourses());
        takenCourseInitial.setCellValueFactory(new PropertyValueFactory<>("courseInitial"));
        takenCourseCredit.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));

        // Set up the menu item action
        logoutMenuItem.setOnAction(this::handleLogoutMenuItemAction);
        setFacultyInfoInMenuItem(userId);
        populateOfferedCourseTable();
        takenCourseTable.refresh();


    }

    private ObservableList<CourseData> getOfferedCourses() {

        ObservableList<CourseData> courses = FXCollections.observableArrayList();

        try {
            Path filePath = Paths.get("course_data.txt");
            BufferedReader reader = Files.newBufferedReader(filePath);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String initial = parts[0].trim();
                    String courseName = parts[1].trim();
                    String description = parts[2].trim();
                    int credit = Integer.parseInt(parts[3].trim());
                    int seats = Integer.parseInt(parts[4].trim());
                    String status = parts[5].trim();

                    courses.add(new CourseData(initial, credit));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return courses;
    }

    private ObservableList<CourseData> getTakenCourses() {

        ObservableList<CourseData> courses = FXCollections.observableArrayList();

        Path filePath = Paths.get("takenCourse_data.txt");

        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedReader reader = Files.newBufferedReader(filePath);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String initial = parts[0].trim();
                    int credit = Integer.parseInt(parts[1].trim());
                    courses.add(new CourseData(initial, credit));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return courses;
    }

    private void saveTakenCoursesToFile(ObservableList<CourseData> courses) {
        try {
            String directoryPath = "FacultyData/" + userId;
            Files.createDirectories(Paths.get(directoryPath));

            String filePath = directoryPath + "/" + userId + ".txt";
            Path existingFilePath = Paths.get(filePath);

            List<String> updatedLines = new ArrayList<>();
            Set<String> existingCourses = new HashSet<>(); // Store existing courses

            if (Files.exists(existingFilePath)) {
                try (BufferedReader reader = Files.newBufferedReader(existingFilePath)) {
                    String line;
                    boolean takenCoursesUpdated = false;
                    boolean totalCreditsUpdated = false;

                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("Taken Courses:")) {
                            String[] courseInitials = line.substring(15).trim().split(", ");
                            existingCourses.addAll(Arrays.asList(courseInitials));

                            StringBuilder updatedCourses = new StringBuilder(line.substring(15).trim());

                            for (CourseData course : courses) {
                                if (!existingCourses.contains(course.getCourseInitial())) {
                                    if (updatedCourses.length() > 0) {
                                        updatedCourses.append(", ");
                                    }
                                    updatedCourses.append(course.getCourseInitial());
                                }
                            }

                            updatedLines.add("Taken Courses: " + updatedCourses.toString());
                            takenCoursesUpdated = true;
                        } else if (line.startsWith("Total Credits:")) {
                            int totalCredits = Integer.parseInt(line.substring(14).trim());

                            for (CourseData course : courses) {
                                if (!existingCourses.contains(course.getCourseInitial())) {
                                    totalCredits += course.getCourseCredit();
                                }
                            }

                            updatedLines.add("Total Credits: " + totalCredits);
                            totalCreditsUpdated = true;
                        } else {
                            updatedLines.add(line);
                        }
                    }

                    if (!takenCoursesUpdated) {
                        StringBuilder courseList = new StringBuilder();

                        for (CourseData course : courses) {
                            if (!existingCourses.contains(course.getCourseInitial())) {
                                if (courseList.length() > 0) {
                                    courseList.append(", ");
                                }
                                courseList.append(course.getCourseInitial());
                            }
                        }

                        if (courseList.length() > 0) {
                            updatedLines.add("Taken Courses: " + courseList.toString());
                        }
                    }

                    if (!totalCreditsUpdated) {
                        int totalCredits = 0;

                        for (CourseData course : courses) {
                            if (!existingCourses.contains(course.getCourseInitial())) {
                                totalCredits += course.getCourseCredit();
                            }
                        }

                        updatedLines.add("Total Credits: " + totalCredits);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Write updated content back to the file
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE)) {
                for (String updatedLine : updatedLines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String filePath2 = "takenCourse_data.txt";

            // List<String> existingCourses2 = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath2))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String initial = parts[0].trim();
                        int credit = Integer.parseInt(parts[1].trim());
                        String facultyInitial = parts[2].trim();
                        existingCourses.add(initial + "," + credit + "," + facultyInitial);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath2, true))) {
                for (CourseData course : courses) {
                    String courseEntry = course.getCourseInitial() + "," + course.getCourseCredit() + "," + getFacultyInitial(userId);

                    if (!existingCourses.contains(courseEntry)) {
                        writer.write(courseEntry);
                        writer.newLine();
                        createFacultyCourseFile(getFacultyInitial(userId), course.getCourseInitial());
                    } else {
                        showAlert("Duplicate Course", "Course " + course.getCourseInitial() + " with " + course.getCourseCredit() + " credits already exists.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }            // ...
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createFacultyCourseFile(String facultyInitial, String courseInitial) {
        try {
            String directoryPath = "courseData/" + courseInitial;
            Files.createDirectories(Paths.get(directoryPath));

            String filePath = directoryPath + "/" + courseInitial + "_" + facultyInitial + ".txt";
            Path newFilePath = Paths.get(filePath);

            if (!Files.exists(newFilePath)) {
                Files.createFile(newFilePath);
                showAlert("Success", "Course Section added successfully.");
            } else {
                showAlert("Duplicate Course", "Course Section " + courseInitial + " already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleAddCourseButtonAction(ActionEvent event) {
        CourseData selectedCourse = offeredCourseTable.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            ObservableList<CourseData> takenCourses = takenCourseTable.getItems();

            // Check if the course is already in takenCourses
            boolean isAlreadyAdded = takenCourses.stream()
                    .anyMatch(course -> course.getCourseInitial().equals(selectedCourse.getCourseInitial()));

            if (!isAlreadyAdded) {
                takenCourses.add(selectedCourse);

            } else {
                showAlert("Duplicate Course", "Course " + selectedCourse.getCourseInitial() + " already exists.");
            }
        }
    }

    @FXML
    private void handleRemoveCourseButtonAction(ActionEvent event) {
        CourseData selectedCourse = takenCourseTable.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            ObservableList<CourseData> takenCourses = takenCourseTable.getItems();

            // Find the index of the selected course
            int selectedIndex = takenCourses.indexOf(selectedCourse);

            // Remove the selected course from the list
            takenCourses.remove(selectedIndex);

            // Remove the course from the saved files
            removeCourseFromSavedFiles(selectedCourse);
        }
    }

    private void removeCourseFromSavedFiles(CourseData course) {
        try {
            String directoryPath = "FacultyData/" + userId;
            String filePath = directoryPath + "/" + userId + ".txt";

            Path existingFilePath = Paths.get(filePath);

            if (Files.exists(existingFilePath)) {
                List<String> updatedLines = new ArrayList<>();
                boolean found = false;

                try (BufferedReader reader = Files.newBufferedReader(existingFilePath)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("Taken Courses:")) {
                            String[] courseInitials = line.substring(15).trim().split(", ");
                            List<String> courseList = new ArrayList<>(Arrays.asList(courseInitials));

                            if (courseList.remove(course.getCourseInitial())) {
                                found = true;
                            }

                            updatedLines.add("Taken Courses: " + String.join(", ", courseList));
                        } else if (line.startsWith("Total Credits:")) {
                            if (found) {
                                int totalCredits = Integer.parseInt(line.substring(14).trim()) - course.getCourseCredit();
                                updatedLines.add("Total Credits: " + totalCredits);
                            } else {
                                updatedLines.add(line);
                            }
                        } else {
                            updatedLines.add(line);
                        }
                    }
                }

                if (found) {
                    Files.write(existingFilePath, updatedLines);
                } else {
                    showAlert("Error", "Course " + course.getCourseInitial() + " not found.");
                }
            } else {
                showAlert("Error", "Course " + course.getCourseInitial() + " not found.");
            }

            // Remove from takenCourse_data.txt
            String takenCoursesFilePath = "takenCourse_data.txt";
            List<String> takenCoursesLines = Files.readAllLines(Paths.get(takenCoursesFilePath));
            List<String> updatedTakenCoursesLines = new ArrayList<>();

            for (String line : takenCoursesLines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String initial = parts[0].trim();
                    int credit = Integer.parseInt(parts[1].trim());

                    if (!(course.getCourseInitial().equals(initial) && course.getCourseCredit() == credit)) {
                        updatedTakenCoursesLines.add(line);
                    }
                }
            }

            Files.write(Paths.get(takenCoursesFilePath), updatedTakenCoursesLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSaveCourseButtonAction(ActionEvent event) {
        CourseData selectedCourse = offeredCourseTable.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            ObservableList<CourseData> takenCourses = takenCourseTable.getItems();


            saveTakenCoursesToFile(takenCourses);
            showAlert("Success", "Course added successfully.");

        }
    }

    private void populateOfferedCourseTable() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("course_data.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 5) {
                    String initial = parts[0].trim();
                    String title = parts[1].trim();
                    int credit = Integer.parseInt(parts[2].trim());
                    int capacity = Integer.parseInt(parts[3].trim());
                    String description = parts[4].trim();

                    // Create a CourseData object and add it to the table
                    CourseData courseData = new CourseData(initial, credit);
                    offeredCourseTable.getItems().add(courseData);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleViewProfileAction() {
        callScene(viewProfileButton, "/fxml/ViewFacultyProfile.fxml", "View Profile");
    }

    private void handleViewCourseDetailsButtonAction() {
        callScene(viewCourseDetailsButton, "/fxml/CourseDetailsFaculty.fxml", "Course Details");

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
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

    public void setFacultyId(String userId) {
        this.userId = userId;
        setFacultyInfoInMenuItem(userId);
    }

    private String getFacultyInitial(String userId) {

        try (BufferedReader reader = new BufferedReader(new FileReader("faculty_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String facultyID = parts[0].trim();
                    String facultyInitial = parts[1].trim();
                    if (userId.startsWith(facultyID)) {
                        return facultyInitial;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ""; // Return an empty string if no matching faculty ID is found
    }


    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public void handleNotificationCenterAction(MouseEvent mouseEvent) {
    }

    public void handleDashboardButtonAction() {
    }

}
