package controllers;

import application.StudentGrade;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

public class SubmitGradeController implements Initializable {
    public String courseFilePath;
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
    private TableView<StudentGrade> markSheetTable;
    @FXML
    private TableColumn<StudentGrade, String> idColumn;
    @FXML
    private TableColumn<StudentGrade, String> nameColumn;
    @FXML
    private TableColumn<StudentGrade, String> quiz1Column;
    @FXML
    private TableColumn<StudentGrade, String> quiz2Column;
    @FXML
    private TableColumn<StudentGrade, String> quiz3Column;
    @FXML
    private TableColumn<StudentGrade, String> midColumn;
    @FXML
    private TableColumn<StudentGrade, String> finalColumn;
    @FXML
    private TableColumn<StudentGrade, String> gradeColumn;
    private ObservableList<StudentGrade> studentGrades;
    private String userId;
    private String profilePicturePath;
    private String courseInitial;
    private String facultyInitial;

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
        AssignmentButton.setOnAction(event -> handleAssignmentButtonAction());
        viewUpcomingEventsButton.setOnAction(event -> handleViewUpcomingEventsButtonAction());
        resourceManagementButton.setOnAction(event -> handleResourceManagementButtonAction());
        AppointmentButton.setOnAction(event -> handleAppointmentButtonAction());
        viewAnnouncementsButton.setOnAction(event -> handleViewAnnouncementsButtonAction());

        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        quiz1Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuiz1()));
        quiz2Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuiz2()));
        quiz3Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuiz3()));
        midColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMid()));
        finalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFinalGrade()));
        gradeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGrade()));

        // Make the table editable
        markSheetTable.setEditable(true);

        idColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        idColumn.setOnEditCommit(this::handleEditCommit);

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(this::handleEditCommit);

        quiz1Column.setCellFactory(TextFieldTableCell.forTableColumn());
        quiz1Column.setOnEditCommit(this::handleEditCommit);

        quiz2Column.setCellFactory(TextFieldTableCell.forTableColumn());
        quiz2Column.setOnEditCommit(this::handleEditCommit);

        quiz3Column.setCellFactory(TextFieldTableCell.forTableColumn());
        quiz3Column.setOnEditCommit(this::handleEditCommit);

        midColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        midColumn.setOnEditCommit(this::handleEditCommit);

        finalColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        finalColumn.setOnEditCommit(this::handleEditCommit);

        gradeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        gradeColumn.setOnEditCommit(this::handleEditCommit);


        markSheetTable.setItems(studentGrades);

        // Set up the menu item action
        logoutMenuItem.setOnAction(this::handleLogoutMenuItemAction);
        setFacultyInfoInMenuItem(userId);

    }

    private void handleEditCommit(TableColumn.CellEditEvent<StudentGrade, String> event) {
        TablePosition<StudentGrade, String> position = event.getTablePosition();
        String newValue = event.getNewValue();

        int row = position.getRow();
        StudentGrade studentGrade = event.getTableView().getItems().get(row);

        if (position.getColumn() == 0) {
            studentGrade.setId(newValue);
        } else if (position.getColumn() == 1) {
            studentGrade.setName(newValue);
        } else if (position.getColumn() == 2) {
            studentGrade.setQuiz1(newValue);
        } else if (position.getColumn() == 3) {
            studentGrade.setQuiz2(newValue);
        } else if (position.getColumn() == 4) {
            studentGrade.setQuiz3(newValue);
        } else if (position.getColumn() == 5) {
            studentGrade.setMid(newValue);
        } else if (position.getColumn() == 6) {
            studentGrade.setFinalGrade(newValue);
        } else if (position.getColumn() == 7) {
            studentGrade.setGrade(newValue);
        }
        // Save the changes immediately
        loadAndSaveStudentGrades();
    }

    private void saveStudentGrades() {


        try {
            File file = new File("courseData/" + courseInitial + "/" + courseInitial + "_" + facultyInitial + ".txt");
            BufferedWriter writer = Files.newBufferedWriter(file.toPath());

            for (StudentGrade studentGrade : markSheetTable.getItems()) {
                String id = studentGrade.getId();
                String name = studentGrade.getName();
                String quiz1 = studentGrade.getQuiz1();
                String quiz2 = studentGrade.getQuiz2();
                String quiz3 = studentGrade.getQuiz3();
                String mid = studentGrade.getMid();
                String finalGrade = studentGrade.getFinalGrade();
                String grade = studentGrade.getGrade();

                String line = String.join(",", id, name, quiz1, quiz2, quiz3, mid, finalGrade, grade);

                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    private void loadStudentGrades() {
        System.out.println("Before load: " + courseInitial + ", " + facultyInitial);
        studentGrades = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader("courseData/" + courseInitial + "/" + courseInitial + "_" + facultyInitial + ".txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0];
                String name = parts[1];
                String quiz1 = (parts.length > 2) ? parts[2] : null;
                String quiz2 = (parts.length > 3) ? parts[3] : null;
                String quiz3 = (parts.length > 4) ? parts[4] : null;
                String mid = (parts.length > 5) ? parts[5] : null;
                String finalGrade = (parts.length > 6) ? parts[6] : null;
                String grade = (parts.length > 7) ? parts[7] : null;

                StudentGrade studentGrade = new StudentGrade(id, name, quiz1, quiz2, quiz3, mid, finalGrade, grade);
                studentGrades.add(studentGrade);
            }

            // Set the data to the table
            markSheetTable.setItems(studentGrades);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }

        System.out.println("After load: " + courseInitial + ", " + facultyInitial);
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

    public void setCourseAndFacultyInitials(String courseInitial, String facultyInitial) {
        this.courseInitial = courseInitial;
        this.facultyInitial = facultyInitial;
        loadAndSaveStudentGrades();
    }

    private void loadAndSaveStudentGrades() {
        loadStudentGrades();
        saveStudentGrades();
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


    public void handleNotificationCenterAction(MouseEvent mouseEvent) {
    }

    public void handleDashboardButtonAction(ActionEvent event) {
        callScene(dashBoardButton, "/fxml/FacultyDashboard.fxml", "Dashboard");
    }
}
