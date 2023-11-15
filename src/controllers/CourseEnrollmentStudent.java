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

public class CourseEnrollmentStudent implements Initializable {

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
    private MFXButton manageBillingButton;

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

    private String userId;
    private String profilePicturePath;


    @FXML
    private TableView<CourseData> offeredCourseTable;


    @FXML
    private TableColumn<CourseData, String> offeredCourseInitial;

    @FXML
    private TableColumn<CourseData, Integer> offeredCourseCredit;

    @FXML
    private TableColumn<CourseData, String> offeredCourseFaculty;

    @FXML
    private TableView<CourseData> enrolledCourseTable;

    @FXML
    private TableColumn<CourseData, String> EnrolledCourseInitial;

    @FXML
    private TableColumn<CourseData, Integer> EnrolledCourseCredit;

    @FXML
    private TableColumn<CourseData, String> EnrolledCourseFaculty;

    @FXML
    private MFXButton addCourseButton;

    @FXML
    private MFXButton removeCourseButton;

    @FXML
    private MFXButton saveCourseButton;


    @FXML
    private void handleAddCourseButtonAction() {
    }

    @FXML
    private void handleRemoveCourseButtonAction() {
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
        viewProfileButton.setOnAction(event -> handleViewProfileButtonAction());
        viewCourseDetailsButton.setOnAction(event -> handleViewCourseDetailsButtonAction());
        viewTuitionFeeButton.setOnAction(event -> handleViewTuitionFeeButtonAction());
        manageBillingButton.setOnAction(event -> handleManageBillingButtonAction());
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

        offeredCourseTable.setItems(getOfferedCourses());
        offeredCourseInitial.setCellValueFactory(new PropertyValueFactory<>("courseInitial"));
        offeredCourseCredit.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));
        offeredCourseFaculty.setCellValueFactory(new PropertyValueFactory<>("courseFaculty"));

        enrolledCourseTable.setItems(getEnrolledCourses());
        EnrolledCourseInitial.setCellValueFactory(new PropertyValueFactory<>("courseInitial"));
        EnrolledCourseCredit.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));
        EnrolledCourseFaculty.setCellValueFactory(new PropertyValueFactory<>("courseFaculty"));


        logoutMenuItem.setOnAction(event -> handleLogoutMenuItemAction());
        setStudentInfoInMenuItem(userId);
        enrolledCourseTable.refresh();

    }

    private ObservableList<CourseData> getOfferedCourses() {

        ObservableList<CourseData> courses = FXCollections.observableArrayList();

        try {
            Path filePath = Paths.get("takenCourse_data.txt");
            BufferedReader reader = Files.newBufferedReader(filePath);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String initial = parts[0].trim();
                    String faculty = parts[2].trim();
                    int credit = Integer.parseInt(parts[1].trim());
                    courses.add(new CourseData(initial, credit, faculty));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return courses;
    }

    private ObservableList<CourseData> getEnrolledCourses() {

        ObservableList<CourseData> courses = FXCollections.observableArrayList();

        try {
            Path filePath = Paths.get("enrolledCourse_data.txt");
            BufferedReader reader = Files.newBufferedReader(filePath);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String initial = parts[0].trim();
                    String faculty = parts[2].trim();
                    int credit = Integer.parseInt(parts[1].trim());
                    courses.add(new CourseData(initial, credit, faculty));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return courses;
    }


    private void saveTakenCoursesToFile(ObservableList<CourseData> courses) {
        try {
            String directoryPath = "userData/" + userId;
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

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : updatedLines) {
                    writer.write(line);
                    writer.newLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            for (CourseData course : courses) {
                createStudentCourseFile(course.getCourseInitial(), userId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void createStudentCourseFile(String courseInitial, String userId) {
        try {
            String directoryPath = "userData/" + userId;
            Files.createDirectories(Paths.get(directoryPath));

            String filePath = directoryPath + "/" + userId + "_" + courseInitial + ".txt";
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

    @FXML
    private void handleAddCourseButtonAction(ActionEvent event) {
        CourseData selectedCourse = offeredCourseTable.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            ObservableList<CourseData> takenCourses = enrolledCourseTable.getItems();

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
        CourseData selectedCourse = enrolledCourseTable.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            ObservableList<CourseData> takenCourses = enrolledCourseTable.getItems();

            int selectedIndex = takenCourses.indexOf(selectedCourse);

            takenCourses.remove(selectedIndex);

            removeCourseFromSavedFiles(selectedCourse);
        }
    }

    private void removeCourseFromSavedFiles(CourseData course) {
        try {
            String directoryPath = "userData/" + userId;
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

            // remove course from the folder

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleSaveCourseButtonAction(ActionEvent event) {
        CourseData selectedCourse = offeredCourseTable.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            ObservableList<CourseData> takenCourses = enrolledCourseTable.getItems();

            for (CourseData course : takenCourses) {
                addUserToCourseFile(course.getCourseInitial(), userId, findUserName(userId));

            }
            saveTakenCoursesToFile(takenCourses);
            showAlert("Success", "Course added successfully.");


        }
    }

    private void reduceSeat(CourseData course) {
        String courseInfoFilePath = "courseData/" + course.getCourseInitial() + "/" + course.getCourseInitial() + "_info.txt";

        try {
            List<String> lines = Files.readAllLines(Path.of(courseInfoFilePath));

            // Find and update the Total Seat line
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith("Total Seat:")) {
                    int currentSeats = Integer.parseInt(line.split(":")[1].trim());
                    if (currentSeats > 0) {
                        currentSeats--; // Reduce by 1 seat
                        lines.set(i, "Total Seat: " + currentSeats); // Update the seat count in the list
                    } else {
                        // Handle the case where there are no more seats available
                        showAlert("Error", "No more seats available for the course: " + course.getCourseInitial());
                        return;
                    }
                    break;
                }
            }

            Files.write(Path.of(courseInfoFilePath), lines, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String findUserName(String userId) {
        String filePath = "userData/" + userId + "/" + userId + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Username:")) {
                    return line.substring(10).trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // Return null if userName is not found
    }

    private void addUserToCourseFile(String courseInitial, String userId, String userName) {
        try {
            // Read facultyInitial from takenCourse_data.txt
            String facultyInitial = findFacultyInitial(courseInitial);

            if (facultyInitial != null) {
                String directoryPath = "courseData/" + courseInitial;
                Files.createDirectories(Paths.get(directoryPath));

                String filePath = directoryPath + "/" + courseInitial + "_" + facultyInitial + ".txt";

                if (!isUserEntryExists(filePath, userId, userName)) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                        String userEntry = userId + ", " + userName;
                        writer.write(userEntry);
                        writer.newLine();

                        CourseData selectedCourse = offeredCourseTable.getSelectionModel().getSelectedItem();
                        reduceSeat(selectedCourse);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    showAlert("Duplicate Entry", "User " + userId + " with " + userName + " already exists in " + courseInitial);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isUserEntryExists(String filePath, String userId, String userName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String userEntry = userId + ", " + userName;
            while ((line = reader.readLine()) != null) {
                if (line.equals(userEntry)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Entry not found
    }


    private String findFacultyInitial(String courseInitial) {
        String filePath = "takenCourse_data.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].trim().equals(courseInitial)) {
                    return parts[2].trim(); // Return the facultyInitial
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void handleViewProfileButtonAction() {
        callScene(viewProfileButton, "/fxml/ViewProfile.fxml", "View Profile");
    }

    private void handleViewCourseDetailsButtonAction() {
        callScene(viewCourseDetailsButton, "/fxml/ViewCourseDetailsStudent.fxml", "View Course Details");
    }

    private void handleViewTuitionFeeButtonAction() {
        callScene(viewTuitionFeeButton, "/fxml/ViewTuitionFee.fxml", "View Tuition Fee");
    }

    private void handleManageBillingButtonAction() {
        callScene(manageBillingButton, "/fxml/ManageBilling.fxml", "Manage Billing");
    }

    private void handleManageScholarshipsButtonAction() {
        callScene(manageScholarshipsButton, "/fxml/ManageScholarship.fxml", "Manage Scholarships");
    }

    private void handleNewsFeedButtonAction() {
        callScene(newsFeedButton, "/fxml/NewsfeedStudent.fxml", "News Feed");
    }

    private void handlePostManagementButtonAction() {
        callScene(postManagementButton, "/fxml/PostManagement.fxml", "Post Management");
    }

    private void handleViewGradesButtonAction() {
        callScene(viewGradesButton, "/fxml/ViewGrades.fxml", "View Grades");
    }

    private void handleSubmitAssignmentButtonAction() {
        callScene(submitAssignmentButton, "/fxml/SubmitAssignment.fxml", "Submit Assignment");
    }

    private void handleTrackAcademicProgressButtonAction() {
        callScene(trackAcademicProgressButton, "/fxml/TracAcademicProgress.fxml", "Track Academic Progress");
    }

    private void handleViewUpcomingEventsButtonAction() {
        callScene(viewUpcomingEventsButton, "/fxml/SeeUpcomingEvents.fxml", "View Upcoming Events");
    }

    private void handleResourceManagementButtonAction() {
        callScene(resourceManagementButton, "/fxml/ResourceManagement.fxml", "Resource Management");
    }

    private void handleMakeAppointmentButtonAction() {
        callScene(makeAppointmentButton, "/fxml/MakeAppointment.fxml", "Make Appointment");
    }

    private void handleViewAnnouncementsButtonAction() {
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


    public void handleDashboardButtonAction(ActionEvent event) {
    }
}
