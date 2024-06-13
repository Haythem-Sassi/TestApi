package testcases;

import com.opencsv.exceptions.CsvValidationException;
import junit.framework.Assert;
import org.example.base.BaseClass;
import org.example.dataprovider.DataProviders;
import org.example.pageobjects.MainPage;
import org.example.utility.ExtentManager;
import org.example.utility.FileTransformation;
import org.example.utility.Log;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

        Log.startTestCase("RegressionData");
       // ExtentManager.test = ExtentManager.extent.createTest("RegressionData");
        if (FileTransformation.isVersionAndIdExist(mainPage.getVersion(), FileTransformation.id)) {
            System.out.println("La version " + mainPage.getVersion()+"et l'ID" +FileTransformation.id+" existent déjà. Passage au prochain jeu de données.");
            ExtentManager.test.info("La version " + mainPage.getVersion()+"et l'ID" +FileTransformation.id+" existent déjà. Passage au prochain jeu de données.");
            ++FileTransformation.id;
            return; // Sortir de la méthode et passer au jeu de données suivant
        }

        //System.out.println(height + ", " + weight + ", " + front_img + ", " + side_img + ", " + gender);
        mainPage.completeFormRegression(height,weight,front_img,side_img,gender);
        mainPage.fusion();

        ++i;
        if(i==2){
            FileTransformation.deleteRow(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx");

        }
        Log.endTestCase("RegressionData");
        ExtentManager.test.info("RegressionData");



    }

    @Test(dataProvider = "regressionData", dataProviderClass = DataProviders.class,dependsOnMethods = {"testingRegression"})
    public void performTestRegression(String id, List<FileTransformation.Measure> measures) {
        Log.startTestCase("PerformRegressionData");
       // ExtentManager.test = ExtentManager.extent.createTest("PerformRegressionData");
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
        ExtentManager.test.info("Test de régression pour l'ID " + id + " : " + (isTestSuccessful ? "Réussi" : "Échoué"));
        Log.info("Test de régression pour l'ID " + id + " : " + (isTestSuccessful ? "Réussi" : "Échoué"));
        Assert.assertTrue("Le test de régression pour l'ID " + id + " a échoué.", isTestSuccessful);
        Log.endTestCase("PerformRegressionData");
        ExtentManager.test.info("PerformRegressionData");
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
