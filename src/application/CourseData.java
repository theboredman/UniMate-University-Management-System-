package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

public class CourseData {
    private final StringProperty courseInitial = new SimpleStringProperty();
    private final StringProperty courseName = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty syllabusFilePath = new SimpleStringProperty();
    private final StringProperty courseFaculty = new SimpleStringProperty();
    private int courseCredit;
    private int totalSeat;


    public CourseData(String courseInitial, String courseName, String description, int credit, int totalSeat, String syllabusFilePath) {
        this.courseInitial.set(courseInitial);
        this.courseName.set(courseName);
        this.description.set(description);
        this.courseCredit = credit;
        this.totalSeat = totalSeat;
        this.syllabusFilePath.set(syllabusFilePath);
    }

    public CourseData() {
    }

    public CourseData(String courseInitial, String courseName, String description, int credit, int totalSeat) {
        this.courseInitial.set(courseInitial);
        this.courseName.set(courseName);
        this.description.set(description);
        this.courseCredit = credit;
        this.totalSeat = totalSeat;
    }

    public CourseData(String initial, int credit) {
        this.courseInitial.set(initial);
        this.courseCredit = credit;
    }

    public CourseData(String initial, int credit, String courseFaculty) {
        this.courseInitial.set(initial);
        this.courseCredit = credit;
        this.courseFaculty.set(courseFaculty);
    }

    public String getSyllabusFilePath() {
        String courseInitial = getCourseInitial();
        String syllabusFolderPath = "Syllabus/";
        String syllabusFilePath = syllabusFolderPath + courseInitial + ".pdf";

        File syllabusFile = new File(syllabusFilePath);

        if (syllabusFile.exists() && syllabusFile.isFile()) {
            return "Available";
        } else {
            return "N/A";
        }
    }

    public void setSyllabusFilePath(String syllabusFilePath) {
        this.syllabusFilePath.set(syllabusFilePath);
    }

    public String getCourseInitial() {
        return courseInitial.get();
    }

    public void setCourseInitial(String courseInitial) {
        this.courseInitial.set(courseInitial);
    }

    public StringProperty courseInitialProperty() {
        return courseInitial;
    }

    public String getCourseName() {
        return courseName.get();
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public int getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(int credit) {
        this.courseCredit = credit;
    }

    public int getTotalSeat() {
        return totalSeat;
    }

    public void setTotalSeat(int totalSeat) {
        this.totalSeat = totalSeat;
    }

    public StringProperty syllabusFilePathProperty() {
        return syllabusFilePath;
    }

    public StringProperty getSyllabusFilePathProperty() {
        String path = syllabusFilePath.get();
        return new SimpleStringProperty((path != null && !path.isEmpty()) ? "Available" : "N/A");
    }

    public String getCourseFaculty() {
        return courseFaculty.get();
    }

    public StringProperty facultyProperty() {
        return courseFaculty;
    }

    public void setFaculty(String faculty) {
        this.courseFaculty.set(faculty);
    }
}
