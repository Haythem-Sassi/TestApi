package org.example.pageobjects;

import org.example.actiondriver.Action;
import org.example.base.BaseClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class MainPage extends BaseClass{
    Action action= new Action();

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

    public MainPage() {
        PageFactory.initElements(getDriver(), this);
    }

    public void enterMeasurements(String height, String weight) {
        action.type(heightInput,height);
        action.type(weightInput,weight);
    }

    public void uploadImages(String frontImagePath, String sideImagePath) {
        action.type(frontImage,frontImagePath);
        action.type(sideImage,sideImagePath);

    }
    public void selectGender(boolean isMale) {
        if (isMale) {
            action.click(getDriver(),maleMesure);
        } else {
            action.click(getDriver(),femaleMesure);
        }
    }
    public void completeForm(String height, String weight, String frontImagePath, String sideImagePath, boolean isMale) {
        // Enter measurements
        enterMeasurements(height, weight);

        // Upload images
        uploadImages(frontImagePath, sideImagePath);

        // Select gender and measure
        selectGender(isMale);
    }



}
