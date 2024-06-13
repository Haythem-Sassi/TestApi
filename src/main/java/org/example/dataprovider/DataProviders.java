package org.example.dataprovider;

import org.example.utility.ExcelReader;
import org.example.utility.FileTransformation;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataProviders {

    ExcelReader data = new ExcelReader();

    ExcelReader data1 = new ExcelReader(System.getProperty("user.dir") +"\\src\\test\\resources\\TestData\\try.xlsx");




    @DataProvider(name = "credentials")
    public Object[][] getData(){
        Object[][] newData = new Object[data.getRowCount("Test")-1][data.getColumnCount("Test")-1];
        for (int i=2;i<= data.getRowCount("Test");i++) {
            for (int j = 1; j < data.getColumnCount("Test"); j++) {
                newData[i - 2][j - 1] = data.getCellData("Test", j, i);

            }
        }
        return newData;
    }

    @DataProvider(name = "regressionn")
    public Object[][] getDataRegression(){

        Object[][] newData = new Object[data1.getRowCount("Test")-1][data1.getColumnCount("Test")-1];
        for (int i=2;i<= data1.getRowCount("Test");i++) {
            for (int j = 1; j < data1.getColumnCount("Test"); j++) {
                newData[i - 2][j - 1] = data1.getCellData("Test", j, i);

            }
        }
        return newData;
    }

    @DataProvider(name = "regressionData")
    public static Object[][] regressionData() throws IOException {
        List<Map<String, List<FileTransformation.Measure>>> dataByVersions = FileTransformation.getRegressionData(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\originalFinal.xlsx");
        List<Object[]> data = new ArrayList<>();

        for (Map<String, List<FileTransformation.Measure>> versionData : dataByVersions) {
            for (Map.Entry<String, List<FileTransformation.Measure>> entry : versionData.entrySet()) {
                String id = entry.getKey();
                List<FileTransformation.Measure> measures = entry.getValue();

                if (measures.size() >= 42) {
                    data.add(new Object[]{id, measures});
                }
            }
        }
        return data.toArray(new Object[0][0]);
    }



}