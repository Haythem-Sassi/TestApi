package testcases;

import com.opencsv.exceptions.CsvValidationException;
import org.example.base.BaseClass;
import org.example.dataprovider.DataProviders;
import org.example.pageobjects.MainPage;
import org.example.utility.FileTransformation;
import org.example.utility.Log;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class RegressionTest extends BaseClass {

    private MainPage mainPage;

    public static int i=0;



    @BeforeMethod
    public void setup(){
        launchApp("Chrome");
        mainPage = new MainPage();
    }

    @AfterMethod()
    public void tearDown() {
        getDriver().quit();
    }

    @Test(dataProvider = "regressionn", dataProviderClass = DataProviders.class)
    public void testingRegression(String height, String weight, String front_img, String side_img, String gender) throws CsvValidationException, IOException, InterruptedException {

        Log.startTestCase("Regression");
        if (FileTransformation.isVersionAndIdExist(mainPage.getVersion(), FileTransformation.id)) {
            System.out.println("La version et l'ID existent déjà. Passage au prochain jeu de données.");
            ++FileTransformation.id;
            return; // Sortir de la méthode et passer au jeu de données suivant
        }

        //System.out.println(height + ", " + weight + ", " + front_img + ", " + side_img + ", " + gender);
        mainPage.completeFormRegression(height,weight,front_img,side_img,gender);
        mainPage.fusion();

        ++i;
        if(i==4){
            FileTransformation.deleteRow(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx");

        }


    }
}
