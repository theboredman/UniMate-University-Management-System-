package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserData {
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty userName = new SimpleStringProperty();
    private final StringProperty department = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final StringProperty passedCredit = new SimpleStringProperty();
    private final StringProperty cgpa = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private String profilePicturePath;

    public UserData(String id, String userName, String department, String email, String phoneNumber, String passedCredit, String cgpa, String password, String profilePicturePath) {
        this.id.set(id);
        this.userName.set(userName);
        this.department.set(department);
        this.email.set(email);
        this.phoneNumber.set(phoneNumber);
        this.passedCredit.set(passedCredit);
        this.cgpa.set(cgpa);
        this.password.set(password);
        this.profilePicturePath = profilePicturePath;
    }

    public UserData(String id, String userName, String department, String email, String phoneNumber, String passedCredit, String cgpa, String password) {
        this.id.set(id);
        this.userName.set(userName);
        this.department.set(department);
        this.email.set(email);
        this.phoneNumber.set(phoneNumber);
        this.passedCredit.set(passedCredit);
        this.cgpa.set(cgpa);
        this.password.set(password);
    }

    public UserData() {
    }

    @Override
    public String toString() {
        return getId() + "," +
                getUserName() + "," +
                getDepartment() + "," +
                getEmail() + "," +
                getPhoneNumber() + "," +
                getCgpa() + "," +
                getPassedCredit() + "," +
                getPassword();
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
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

    public String getPassedCredit() {
        return passedCredit.get();
    }

    public void setPassedCredit(String passedCredit) {
        this.passedCredit.set(passedCredit);
    }

    public StringProperty passedCreditProperty() {
        return passedCredit;
    }

    public String getCgpa() {
        return cgpa.get();
    }

    public void setCgpa(String cgpa) {
        this.cgpa.set(cgpa);
    }

    public StringProperty cgpaProperty() {
        return cgpa;
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

}
