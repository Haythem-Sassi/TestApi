package testcases;

import org.example.base.BaseClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
public class MainPageTest extends BaseClass {
    @BeforeMethod
    public void setup(String browserName){
        launchApp(browserName);
    }

    @AfterMethod()
    public void tearDown() {
        getDriver().quit();
    }




}
