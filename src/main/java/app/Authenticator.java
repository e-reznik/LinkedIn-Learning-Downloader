package app;

import helper.Constants;
import static helper.Constants.URLLOGIN;
import static helper.Constants.USERNAME;
import static helper.Constants.driver;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

public class Authenticator {
    
    

    public void login(String course) throws IOException {
        driver.get(URLLOGIN);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement elemUsername = driver.findElement(By.id("auth-id-input"));
        elemUsername.sendKeys(USERNAME);

        WebElement button = driver.findElement(By.id("auth-id-button"));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].removeAttribute('disabled','disabled')", button);

        button.click();

        // Next page
        WebElement elemPassword = driver.findElement(By.id("password"));
        elemPassword.sendKeys(Constants.PASSWORD);

        WebElement button2 = driver.findElement(By.tagName("button"));
        button2.click();

        driver.get(course);

        Downloader downloader = new Downloader();
        downloader.download();
    }
}
