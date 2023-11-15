package application;

import java.util.ArrayList;
import java.util.List;

public class RegistrationRequest {
    List<RegistrationRequest> registrationRequests = new ArrayList<>();
    private UserData userData;
    private RegistrationStatus status;

    public RegistrationRequest(UserData userData, RegistrationStatus status) {
        this.userData = userData;
        this.status = status;
    }

    public void addRegistrationRequest(RegistrationRequest request) {
        registrationRequests.add(request);
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }

    public enum RegistrationStatus {
        APPROVED,
        REJECTED,
        PENDING
    }
}
