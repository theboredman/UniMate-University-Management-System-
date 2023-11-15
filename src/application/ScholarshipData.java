package application;

public class ScholarshipData {
    private String id;
    private String name;
    private String cgpa;
    private String passedCredits;
    private String percentage;

    // Constructor
    public ScholarshipData(String id, String name, String cgpa, String passedCredits, String percentage) {
        this.id = id;
        this.name = name;
        this.cgpa = cgpa;
        this.passedCredits = passedCredits;
        this.percentage = percentage;
    }

    // Getters and Setters (You can generate these using your IDE)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCgpa() {
        return cgpa;
    }

    public void setCgpa(String cgpa) {
        this.cgpa = cgpa;
    }

    public String getPassedCredits() {
        return passedCredits;
    }

    public void setPassedCredits(String passedCredits) {
        this.passedCredits = passedCredits;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
