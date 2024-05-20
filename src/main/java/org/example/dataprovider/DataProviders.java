package org.example.dataprovider;

import org.example.utility.ExcelReader;
import org.testng.annotations.DataProvider;

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

        Object[][] newData = new Object[data1.getRowCount("regression")-1][data1.getColumnCount("regression")-1];
        for (int i=2;i<= data1.getRowCount("regression");i++) {
            for (int j = 1; j < data1.getColumnCount("regression"); j++) {
                newData[i - 2][j - 1] = data1.getCellData("regression", j, i);

            }
        }
        return newData;
    }



}