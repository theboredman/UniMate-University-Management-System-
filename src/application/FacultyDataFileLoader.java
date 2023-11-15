package application;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FacultyDataFileLoader {

    public static List<FacultyData> loadFacultyDataFromFile(String filePath) {
        List<FacultyData> facultyDataList = new ArrayList<>();

        try {
            File file = new File(filePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] facultyDataArray = line.split(",");

                if (facultyDataArray.length == 7) {
                    FacultyData facultyData = new FacultyData(
                            facultyDataArray[0],
                            facultyDataArray[1],
                            facultyDataArray[2],
                            facultyDataArray[3],
                            facultyDataArray[4],
                            facultyDataArray[5],
                            facultyDataArray[6]
                    );
                    facultyDataList.add(facultyData);
                } else {
                    showAlert("Invalid Data Format", "The data format in the file is invalid: " + line);
                }
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return facultyDataList;
    }

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
