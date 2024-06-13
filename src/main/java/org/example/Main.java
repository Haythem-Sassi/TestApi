package org.example;
import junit.framework.Assert;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.utility.FileTransformation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        FileTransformation.deleteRow(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx");



    }
    public static void performTestRegression() throws IOException {
        List<Map<String, List<FileTransformation.Measure>>> dataByVersions = FileTransformation.getRegressionData(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\originalFinal.xlsx");

        for (Map<String, List<FileTransformation.Measure>> versionData : dataByVersions) {
            for (Map.Entry<String, List<FileTransformation.Measure>> entry : versionData.entrySet()) {
                String id = entry.getKey();
                List<FileTransformation.Measure> measures = entry.getValue();

                // Vérifier si nous avons plusieurs versions pour cet ID
                if (measures.size() >= 42) {
                    int totalFields = 0;
                    int correctFields = 0;

                    for (int i = 21; i < measures.size(); i += 21) {
                        for (int j = 0; j < 21; j++) {
                            FileTransformation.Measure previousMeasure = measures.get(i - 21 + j);
                            FileTransformation.Measure currentMeasure = measures.get(i + j);

                            double previousValue = previousMeasure.value;
                            double currentValue = currentMeasure.value;
                            String previousTime = previousMeasure.responseTime;
                            String currentTime = currentMeasure.responseTime;

                            boolean isFieldRegressor = isFieldRegressor(previousValue, currentValue);
                            boolean isTimeRegressor = isTimeRegressor(previousTime, currentTime);

                            totalFields++;
                            if (!isFieldRegressor && !isTimeRegressor) {
                                correctFields++;
                            }
                        }
                    }

                    boolean isTestSuccessful = isTestSuccessful(totalFields, correctFields);
                    System.out.println("Test de régression pour l'ID " + id + " : " + (isTestSuccessful ? "Réussi" : "Échoué"));
                    Assert.assertTrue("Le test de régression pour l'ID " + id + " a échoué.", isTestSuccessful);
                }
            }
        }
    }

    public static boolean isTimeRegressor(String previousTimeStr, String currentTimeStr) {
        double previousTime = convertTimeToDouble(previousTimeStr);
        double currentTime = convertTimeToDouble(currentTimeStr);
        return currentTime > previousTime;
    }

    public static double convertTimeToDouble(String timeStr) {
        // Suppression du suffixe 's' et conversion en double
        if (timeStr != null && timeStr.endsWith("s")) {
            timeStr = timeStr.substring(0, timeStr.length() - 1);
        }
        return Double.parseDouble(timeStr);
    }

    public static boolean isFieldRegressor(double previousValue, double currentValue) {
        return currentValue > previousValue;
    }

    public static boolean isTestSuccessful(int totalFields, int correctFields) {
        return correctFields > (totalFields / 2);
    }





}