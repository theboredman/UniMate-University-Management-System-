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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewTutionFeeController implements Initializable {
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
    private MFXButton generatePaymentSlipButton;

    @FXML
    private MFXContextMenuItem showUsername;

    @FXML
    private MFXContextMenuItem showID;

    @FXML
    private MFXContextMenuItem showPerCreditFee;

    @FXML
    private MFXContextMenuItem showStudentActivityFee;

    @FXML
    private MFXContextMenuItem showScienceLabfee;

    @FXML
    private MFXContextMenuItem showComputerLabFee;

    @FXML
    private MFXContextMenuItem showLibraryFee;

    @FXML
    private MFXContextMenuItem showTakenCourses;

    @FXML
    private MFXContextMenuItem showTotalCredits;

    @FXML
    private MFXContextMenuItem showScholarshipPercentage;

    @FXML
    private MFXContextMenuItem showTotalCreditFee;

    @FXML
    private MFXContextMenuItem showTotalFee;

    @FXML
    private MFXContextMenuItem showTotalFeeAfterScholarship;

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

        loadTuitionFeeData();


    }

    private void loadTuitionFeeData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("TuitionFee_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String feeType = parts[0].trim();
                    String feeAmount = parts[1].trim();

                    // Set the tuition fee data in corresponding menu items
                    setTuitionFeeData(feeType, feeAmount);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayUserData(String userId) throws FileNotFoundException {
        File userFile = new File("userData/" + userId + "/" + userId + ".txt");
        //String profilePicturePath = "userData/" + userId + "/" + userId + ".jpg";
        //  Image profileImage = new Image(new FileInputStream(profilePicturePath));
        //profileImageView.setImage(profileImage);

        double totalCredits = 0; // Initialize total credits
        double scholarshipPercentage = 0; // Initialize scholarship percentage

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
                            case "Total Credit Fee":
                                showTotalCreditFee.setText(formattedData);
                                break;
                            case "Total Credits":
                                showTotalCredits.setText(formattedData);
                                break;
                            case "Taken Courses":
                                showTakenCourses.setText(formattedData);
                                break;
                            case "Scholarship Percentage (%)":
                                showScholarshipPercentage.setText(formattedData);
                                break;

                            // Add more cases for other fields as needed
                        }
                        if ("Total Credits".equals(field)) {
                            totalCredits = Double.parseDouble(value);
                        }
                        if ("Scholarship Percentage (%)".equals(field)) {
                            scholarshipPercentage = Double.parseDouble(value);
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (e.g., show an error message)
            }
            double perCreditCost = Double.parseDouble(showPerCreditFee.getText().split(":")[1].trim());

            // Calculate and set the total fee
            totalCredits = Double.parseDouble(showTotalCredits.getText().split(":")[1].trim());
            double totalCourseFee = totalCredits * perCreditCost;
            double totalFee = totalCourseFee
                    + Double.parseDouble(showStudentActivityFee.getText().split(":")[1].trim())
                    + Double.parseDouble(showScienceLabfee.getText().split(":")[1].trim())
                    + Double.parseDouble(showComputerLabFee.getText().split(":")[1].trim())
                    + Double.parseDouble(showLibraryFee.getText().split(":")[1].trim());

            double totalFeeAfterScholarship = totalFee - (totalFee * (scholarshipPercentage / 100));

            // Set the tuition fee data in corresponding menu items
            setTuitionFeeData("Total Credits", String.valueOf(totalCredits)); // Set total credits
            setTuitionFeeData("Total Fee", String.valueOf(totalFee)); // Set total fee
            setTuitionFeeData("Total Fee After Scholarship", String.valueOf(totalFeeAfterScholarship)); // Set total fee after scholarship
        }
    }

    private void setTuitionFeeData(String feeType, String feeAmount) {
        double totalCourseFee = 0; // Initialize total course fee
        switch (feeType) {
            case "Per Credit Cost":
                showPerCreditFee.setText(feeType + ": " + feeAmount);
                break;
            case "Student Activity Fee":
                showStudentActivityFee.setText(feeType + ": " + feeAmount);
                break;
            case "Science Lab Fee":
                showScienceLabfee.setText(feeType + ": " + feeAmount);
                break;
            case "Computer Lab Fee":
                showComputerLabFee.setText(feeType + ": " + feeAmount);
                break;
            case "Library Fee":
                showLibraryFee.setText(feeType + ": " + feeAmount);
                break;
            case "Total Credits":
                double totalCredits = Double.parseDouble(feeAmount);
                // Assuming per credit cost has been set previously
                double perCreditCost = Double.parseDouble(showPerCreditFee.getText().split(":")[1].trim());

                // Calculate and set the total fee
                totalCourseFee = totalCredits * perCreditCost;
                showTotalCreditFee.setText("Total Credit Fee : " + totalCourseFee);
                break;
            case "Total Fee": // Assuming you want to display the sum of all fees

                // Assuming each fee type has been set previously
                double totalFee = totalCourseFee
                        + Double.parseDouble(showStudentActivityFee.getText().split(":")[1].trim())
                        + Double.parseDouble(showScienceLabfee.getText().split(":")[1].trim())
                        + Double.parseDouble(showComputerLabFee.getText().split(":")[1].trim())
                        + Double.parseDouble(showLibraryFee.getText().split(":")[1].trim());

                showTotalFee.setText("Total Fee : " + totalFee);
                break;
            case "Total Fee After Scholarship":
                // this will reduce the total fee by the scholarship percentage
                double totalFeeAfterScholarship = totalCourseFee
                        + Double.parseDouble(showStudentActivityFee.getText().split(":")[1].trim())
                        + Double.parseDouble(showScienceLabfee.getText().split(":")[1].trim())
                        + Double.parseDouble(showComputerLabFee.getText().split(":")[1].trim())
                        + Double.parseDouble(showLibraryFee.getText().split(":")[1].trim());
                double scholarshipPercentage = Double.parseDouble(showScholarshipPercentage.getText().split(":")[1].trim());
                double totalFeeAfterScholarshipAmount = totalFeeAfterScholarship - (totalFeeAfterScholarship * (scholarshipPercentage / 100));
                showTotalFeeAfterScholarship.setText("Total Fee After Scholarship : " + totalFeeAfterScholarshipAmount);
                break;

        }
    }

    @FXML
    private void handleGeneratePaymentSlipButtonAction() {
        String userId = showID.getText().split(":")[1].trim();
        String userName = showUsername.getText().split(":")[1].trim();
        String totalFee = showTotalFee.getText().split(":")[1].trim();
        String totalCreditFee = showTotalCreditFee.getText().split(":")[1].trim();
        String totalCredits = showTotalCredits.getText().split(":")[1].trim();
        String takenCourses = showTakenCourses.getText().split(":")[1].trim();
        String perCreditFee = showPerCreditFee.getText().split(":")[1].trim();
        String studentActivityFee = showStudentActivityFee.getText().split(":")[1].trim();
        String scienceLabFee = showScienceLabfee.getText().split(":")[1].trim();
        String computerLabFee = showComputerLabFee.getText().split(":")[1].trim();
        String libraryFee = showLibraryFee.getText().split(":")[1].trim();
        String percentage = showScholarshipPercentage.getText().split(":")[1].trim();
        String totalFeeAfterScholarship = showTotalFeeAfterScholarship.getText().split(":")[1].trim();


        generatePaymentSlip(userId, userName, totalFee, totalCreditFee, totalCredits, takenCourses,
                perCreditFee, studentActivityFee, scienceLabFee, computerLabFee, libraryFee, percentage, totalFeeAfterScholarship);
    }

    private void generatePaymentSlip(String userId, String userName, String totalFee, String totalCreditFee, String totalCredits, String takenCourses, String perCreditFee, String studentActivityFee, String scienceLabFee, String computerLabFee, String libraryFee, String percentage, String totalFeeAfterScholarship) {
        // Create the directory if it doesn't exist
        String directoryPath = "userData/" + userId;
        try {
            Files.createDirectories(Paths.get(directoryPath));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
            return;
        }

        // Define the file path for the payment slip
        String filePath = directoryPath + "/" + userId + "_payslip.txt";
        Path existingFilePath = Paths.get(filePath);

        if (!Files.exists(existingFilePath)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // Write payment slip details to the file
                writer.write("Payment Slip for User: " + userName);
                writer.newLine();
                writer.newLine();
                writer.write("User ID: " + userId);
                writer.newLine();
                writer.write("Total Credit Fee: BDT " + totalCreditFee);
                writer.newLine();
                writer.write("Per Credit Fee: BDT " + perCreditFee);
                writer.newLine();
                writer.write("Student Activity Fee: BDT " + studentActivityFee);
                writer.newLine();
                writer.write("Science Lab Fee: BDT " + scienceLabFee);
                writer.newLine();
                writer.write("Computer Lab Fee: BDT " + computerLabFee);
                writer.newLine();
                writer.write("Library Fee: BDT " + libraryFee);
                writer.newLine();
                writer.write("Taken Courses: " + takenCourses);
                writer.newLine();
                writer.write("Total Credits: " + totalCredits);
                writer.newLine();
                writer.write("Total Fee: BDT " + totalFee);
                writer.newLine();
                writer.write("Scholarship Percentage (%): " + percentage);
                writer.newLine();
                writer.write("Total Fee After Scholarship: BDT " + totalFeeAfterScholarship);
                writer.newLine();
                showAlert("Payment Slip Generated", "Payment slip has been generated successfully. Please check the file " + filePath + " for details.");
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (e.g., show an error message)
            }
        } else {
            showAlert("File Already Exists", "The payment slip for this user already exists.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

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

    public void setStudentId(String userId) throws FileNotFoundException {
        this.userId = userId;
        setStudentInfoInMenuItem(userId);
        displayUserData(userId);
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
