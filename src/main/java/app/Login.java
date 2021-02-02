package app;

import helper.Constants;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class Login {

    public void loginToLI(String COURSE) throws IOException {
        final WebDriver driver = new FirefoxDriver();

        driver.get(Constants.URLLOGIN);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
        WebElement elemUsername = driver.findElement(By.id("auth-id-input"));
        elemUsername.sendKeys(Constants.USERNAME);

        WebElement button = driver.findElement(By.id("auth-id-button"));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].removeAttribute('disabled','disabled')", button);

        button.click();

        // Next page
        WebElement elemPassword = driver.findElement(By.id("password"));
        elemPassword.sendKeys(Constants.PASSWORD);

        WebElement button2 = driver.findElement(By.tagName("button"));
        button2.click();

        driver.get(COURSE);

        Downloader d = new Downloader(driver, COURSE);
    }
}
