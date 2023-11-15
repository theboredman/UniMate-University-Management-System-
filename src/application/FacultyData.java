package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FacultyData {
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty initial = new SimpleStringProperty();

    private final StringProperty userName = new SimpleStringProperty();
    private final StringProperty department = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private String profilePicturePath;

    public FacultyData(String id, String initial, String userName, String department, String email, String phoneNumber, String password, String profilePicturePath) {
        this.id.set(id);
        this.initial.set(initial);
        this.userName.set(userName);
        this.department.set(department);
        this.email.set(email);
        this.phoneNumber.set(phoneNumber);
        this.password.set(password);
        this.profilePicturePath = profilePicturePath;
    }

    public FacultyData(String id, String initial, String userName, String department, String email, String phoneNumber, String password) {
        this.id.set(id);
        this.initial.set(initial);
        this.userName.set(userName);
        this.department.set(department);
        this.email.set(email);
        this.phoneNumber.set(phoneNumber);
        this.password.set(password);
    }

    public FacultyData() {

    }

    @Override
    public String toString() {
        return getId() + "," +
                getInitial() + "," +
                getUserName() + "," +
                getDepartment() + "," +
                getEmail() + "," +
                getPhoneNumber() + "," +
                getPassword();
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getInitial() {
        return initial.get();
    }

    public void setInitial(String initial) {
        this.initial.set(initial);
    }

    public StringProperty initialProperty() {
        return initial;
    }

    public String getUserName() {
        return userName.get();
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public String getDepartment() {
        return department.get();
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }

    public StringProperty departmentProperty() {
        return department;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }
}
