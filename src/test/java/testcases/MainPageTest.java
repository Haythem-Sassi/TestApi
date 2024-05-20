package testcases;

import com.opencsv.exceptions.CsvValidationException;
import org.example.base.BaseClass;
import org.example.dataprovider.DataProviders;
import org.example.pageobjects.MainPage;
import org.example.utility.Log;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.AssertJUnit.assertTrue;

public class MainPageTest extends BaseClass {


    private MainPage mainPage;



    @BeforeMethod
    public void setup(){
        launchApp("Chrome");
        mainPage = new MainPage();
    }

    @AfterMethod()
    public void tearDown() {
        getDriver().quit();
    }


    @Test(dataProvider = "credentials", dataProviderClass = DataProviders.class)
    public void insertData(String height, String weight, String front_img, String side_img, String gender) throws InterruptedException, CsvValidationException, IOException {
        Log.startTestCase("Data validation");
        //System.out.println(height + ", " + weight + ", " + front_img + ", " + side_img + ", " + gender);
       mainPage.completeForm(height,weight,front_img,side_img,gender);
       mainPage.saveResult();
       Log.info("Result saving");
       int correctCount = mainPage.compareResultat();
        Log.info("Compare result");
        System.out.println(correctCount);
       assertTrue(correctCount>=3);
        Log.endTestCase("End data validation");

    }














}
