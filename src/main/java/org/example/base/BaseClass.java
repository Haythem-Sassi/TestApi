package org.example.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static java.sql.DriverManager.getDriver;

public class BaseClass {
    public static Properties prop;
    public static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();

    @BeforeSuite
    public void loadConfig() {

        try {
            prop = new Properties();
            FileInputStream ip = new FileInputStream(
                    System.getProperty("user.dir") + "\\Configuration\\config.properties");
            prop.load(ip);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

    }

}

    public static WebDriver getDriver() {
        // Get Driver from threadLocalmap
        return driver.get();
    }

    public void launchApp(String browserName) {
        // String browserName = prop.getProperty("browser");
        if (browserName.equalsIgnoreCase("Chrome")) {
            WebDriverManager.chromedriver().setup();
            // Set Browser to ThreadLocalMap
            driver.set(new ChromeDriver());
        } else if (browserName.equalsIgnoreCase("FireFox")) {
            WebDriverManager.firefoxdriver().setup();
            driver.set(new FirefoxDriver());
        } else if (browserName.equalsIgnoreCase("IE")) {
            WebDriverManager.iedriver().setup();
            driver.set(new InternetExplorerDriver());
        }
        //Maximize the screen
        getDriver().manage().window().maximize();
        //Delete all the cookies
        getDriver().manage().deleteAllCookies();
        //Implicit TimeOuts
        getDriver().manage().timeouts().implicitlyWait
                (Integer.parseInt(prop.getProperty("implicitWait")),TimeUnit.SECONDS);
        //PageLoad TimeOuts
        getDriver().manage().timeouts().pageLoadTimeout
                (Integer.parseInt(prop.getProperty("pageLoadTimeOut")),TimeUnit.SECONDS);
        //Launching the URL
        getDriver().get(prop.getProperty("url"));
    }



}
