package application;

public class StudentGrade {
    private String id;
    private String name;
    private String quiz1;
    private String quiz2;
    private String quiz3;
    private String mid;
    private String finalGrade;
    private String grade;

    public StudentGrade(String id, String name, String quiz1, String quiz2, String quiz3, String mid, String finalGrade, String grade) {
        this.id = id;
        this.name = name;
        this.quiz1 = quiz1;
        this.quiz2 = quiz2;
        this.quiz3 = quiz3;
        this.mid = mid;
        this.finalGrade = finalGrade;
        this.grade = grade;
    }

    // Add getters and setters as needed

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

    public String getQuiz1() {
        return quiz1;
    }

    public void setQuiz1(String quiz1) {
        this.quiz1 = quiz1;
    }

    public String getQuiz2() {
        return quiz2;
    }

    public void setQuiz2(String quiz2) {
        this.quiz2 = quiz2;
    }

    public String getQuiz3() {
        return quiz3;
    }

    public void setQuiz3(String quiz3) {
        this.quiz3 = quiz3;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
