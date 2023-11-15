package application;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CourseFileLoader {

    public static List<CourseData> loadCourseDataFromFile(String filePath) {
        List<CourseData> courseDataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] courseDataArray = line.split(",");

                if (courseDataArray.length == 5) { // Assuming there are 5 fields for a course
                    try {
                        String courseInitial = courseDataArray[0];
                        String courseName = courseDataArray[1];
                        String description = courseDataArray[2];
                        int credit = Integer.parseInt(courseDataArray[3]);
                        int totalSeat = Integer.parseInt(courseDataArray[4]);

                        CourseData courseData = new CourseData(courseInitial, courseName, description, credit, totalSeat);
                        courseDataList.add(courseData);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid integer format in line: " + line);
                    }
                } else {
                    System.err.println("Invalid data format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return courseDataList;
    }
}
