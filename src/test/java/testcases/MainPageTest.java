package testcases;

import com.opencsv.exceptions.CsvValidationException;
import org.example.base.BaseClass;
import org.example.dataprovider.DataProviders;
import org.example.pageobjects.MainPage;
import org.example.utility.ExtentManager;
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
        Log.startTestCase("Validation des données");
       // ExtentManager.test = ExtentManager.extent.createTest("Validation des données");
        //System.out.println(height + ", " + weight + ", " + front_img + ", " + side_img + ", " + gender);
        Log.info("Début de l'insertion des données...");
        ExtentManager.test.info("Début de l'insertion des données...");
       mainPage.completeForm(height,weight,front_img,side_img,gender);
       mainPage.saveResult();
        Log.info("Résultat enregistré avec succès.");
        ExtentManager.test.info("Résultat enregistré avec succès.");
       int correctCount = mainPage.compareResultat();
        Log.info("Comparaison terminée. Nombre de champs corrects : " + correctCount);
        ExtentManager.test.info("Comparaison terminée. Nombre de champs corrects : " + correctCount);
        if (correctCount >= 3) {
            Log.info("Validation réussie. Au moins 3 champs correspondent.");
            ExtentManager.test.info("Validation réussie. Au moins 3 champs correspondent.");
        } else {
            Log.info("La validation a échoué : Au moins 3 champs ne correspondent pas.");
            ExtentManager.test.info("La validation a échoué : Au moins 3 champs ne correspondent pas.");
        }
       assertTrue(correctCount>=3);
        Log.endTestCase("Fin de la validation des données");
        ExtentManager.test.info("Fin de la validation des données");

    }














}
