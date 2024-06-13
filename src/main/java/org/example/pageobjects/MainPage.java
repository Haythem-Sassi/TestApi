package org.example.pageobjects;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import junit.framework.Assert;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.actiondriver.Action;
import org.example.base.BaseClass;
import org.example.utility.FileTransformation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;


public class MainPage extends BaseClass{

    private static int counter = 0;
    private static int counter1 = 0;

    private static int counter2 = 0;

    String duree;

    // Chemin du dossier de téléchargement
    String downloadFolderPath = "C:\\Users\\DELL G15\\Downloads";
    // Chemin du dossier de destination pour déplacer les fichiers
    String destinationFolderPath = "C:\\Users\\DELL G15\\Desktop\\Results\\"+counter;
    Action action= new Action();

    FileTransformation fileTransformation=new FileTransformation();
    @FindBy(xpath = "//*[@id=\"myPopin\"]/div/span")
    private WebElement closeButton;

    @FindBy(id = "myPopin")
    private WebElement popUp;



    @FindBy(xpath = "//*[@id=\"uploadForm\"]/div[1]/input")
    private WebElement heightInput;

    @FindBy(xpath = "//*[@id=\"uploadForm\"]/div[2]/input")
    private WebElement weightInput;

    @FindBy(xpath = "//*[@id=\"uploadForm\"]/div[3]/input")
    private WebElement frontImage;

    @FindBy(xpath = "//*[@id=\"uploadForm\"]/div[4]/input")
    private WebElement sideImage;

    @FindBy(xpath = "//*[@id=\"uploadForm\"]/div[4]/button[1]")
    private WebElement maleMesure;

    @FindBy(xpath = "//*[@id=\"uploadForm\"]/div[4]/button[2]")
    private WebElement femaleMesure;


    @FindBy(xpath = "//*[@id=\"downloadCSV\"]")
    private WebElement result;

    @FindBy(xpath  = "/html/body/div[3]/div/p")
    private WebElement version;

    @FindBy(xpath  = "/html/body/div[3]/div/div[2]/div[2]/h3")
    private WebElement duration;

    public MainPage() {
        PageFactory.initElements(getDriver(), this);
    }

    public void enterMeasurements(String height, String weight) {
        action.type(heightInput,height);
        action.type(weightInput,weight);
    }

    public void uploadImages(String frontImagePath, String sideImagePath) {
        frontImage.sendKeys(frontImagePath+".jpg");
        sideImage.sendKeys(sideImagePath+".jpg");

    }

    public void uploadImagesRegression(String frontImagePath, String sideImagePath) {
        frontImage.sendKeys(frontImagePath);
        sideImage.sendKeys(sideImagePath);

    }
    public void selectGender(String gender) {
        if (gender.equals("male")) {
            action.click(getDriver(),maleMesure);
        } else {
            action.click(getDriver(),femaleMesure);
        }
    }

    public void closePopup() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(visibilityOfElementLocated(By.id("myPopin")));
        action.click(getDriver(),closeButton);
    }


    private static String getDownloadedFilePath(String downloadFolderPath) {
        File[] files = new File(downloadFolderPath).listFiles();
        // Parcourir tous les fichiers dans le dossier de téléchargement et trouver le fichier le plus récent
        File latestFile = null;
        for (File file : files) {
            if (file.isFile()) {
                if (latestFile == null || file.lastModified() > latestFile.lastModified()) {
                    latestFile = file;
                }

    }}
        return latestFile != null ? latestFile.getAbsolutePath() : null;
    }

    // Méthode pour déplacer un fichier vers un autre dossier
    private static void moveFile(String sourceFilePath, String destinationFolderPath) {
        try {
            // Obtenir le nom du fichier
            String fileName = new File(sourceFilePath).getName();
            // Nouveau nom du fichier avec un compteur
            String newFileName = String.format("%d_%s", counter++, fileName);
            // Chemin du fichier de destination
            Path destinationPath = Paths.get(destinationFolderPath, newFileName);
            // Déplacer le fichier vers le dossier de destination
            Files.move(Paths.get(sourceFilePath), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Fichier déplacé avec succès vers : " + destinationPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void downloadResult()  {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(40));
        wait.until(visibilityOfElementLocated(By.xpath("//*[@id=\"downloadCSV\"]")));
        action.click(getDriver(),result);
    }


    public void completeForm(String height, String weight, String frontImagePath, String sideImagePath, String gender) throws InterruptedException, CsvValidationException, IOException {
        closePopup();
        // Enter measurements
        enterMeasurements(height, weight);

        // Upload images
       uploadImages(frontImagePath, sideImagePath);

        // Select gender and measure
        selectGender(gender);
        //Thread.sleep(20000);
        closePopup();
        downloadResult();

        Thread.sleep(3000);


    }

    public void completeFormRegression(String height, String weight, String frontImagePath, String sideImagePath, String gender) throws InterruptedException, CsvValidationException, IOException {
        closePopup();
        // Enter measurements
        enterMeasurements(height, weight);

        // Upload images
        uploadImagesRegression(frontImagePath, sideImagePath);

        // Select gender and measure
        selectGender(gender);
        //Thread.sleep(20000);
        closePopup();
        downloadResult();
        duree=getDuration();
        Thread.sleep(3000);


    }

    public void saveResult() throws CsvValidationException, IOException {
        String downloadedFilePath = getDownloadedFilePath(downloadFolderPath);

        // Vérifier si un fichier a été téléchargé
        if (downloadedFilePath != null) {
            // Déplacer le fichier téléchargé vers le dossier de destination
            moveFile(downloadedFilePath, destinationFolderPath);
        } else {
            System.out.println("Aucun fichier téléchargé trouvé.");
        }

        fileTransformation.convertCSVToXLSX("C:\\Users\\DELL G15\\Desktop\\Results\\"+counter2+"\\"+counter2+"_mesures.csv",
                "C:\\Users\\DELL G15\\Desktop\\Results\\"+counter2+"\\"+counter2+"_mesures.xlsx");

        fileTransformation.deleteFile("C:\\Users\\DELL G15\\Desktop\\Results\\"+counter2+"\\"+counter2+"_mesures.csv");
        counter2++;

    }


    public int compareResultat() throws IOException {
        Map<String, Double> excelValues = new HashMap<>();
        Map<String, Double> jsonMap = new HashMap<>();
        excelValues=FileTransformation.readExcelFile("C:\\Users\\DELL G15\\Desktop\\Results\\"+counter1+"\\"+counter1+"_mesures.xlsx");
        jsonMap=FileTransformation.readJsonFile("C:\\Users\\DELL G15\\Desktop\\Results\\"+counter1+"\\measurements.json");
        System.out.println(counter1);
        int correctCount = 0;
        for (Map.Entry<String, Double> entry : excelValues.entrySet()) {
            String key = entry.getKey();
            Double excelValue = entry.getValue();
            Double jsonValue = jsonMap.get(key);

            if (jsonValue != null) { // Vérifier si la clé existe dans les deux HashMaps
                // Comparaison des valeurs avec une marge de 20%
                double lowerLimit = excelValue * 0.8;
                double upperLimit = excelValue * 1.2;

                if (jsonValue >= lowerLimit && jsonValue <= upperLimit) {
                    correctCount++;
                }
            }
        }
        counter1++;
        return correctCount;

    }

    public String getVersion(){

        return version.getText();
    }

    public void fusion() throws IOException, InterruptedException, CsvValidationException {
        FileTransformation.addVersion(getVersion());
        String downloadedFilePath = getDownloadedFilePath(downloadFolderPath);

        fileTransformation.convertCSVToXLSX(downloadedFilePath,
                "C:\\Users\\DELL G15\\Desktop\\mesures.xlsx");
        FileTransformation.concatenateExcelFiles(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx",
                "C:\\Users\\DELL G15\\Desktop\\mesures.xlsx");

        fileTransformation.deleteFile(downloadedFilePath);
        fileTransformation.deleteFile("C:\\Users\\DELL G15\\Desktop\\mesures.xlsx");

        FileTransformation.addColumn(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx");

        FileTransformation.addColumn2(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx",duree);




    }


    public String getDuration(){

        String str = duration.getText();
        return(str.substring(str.length() - 6).replace(" ", ""));
    }








}
