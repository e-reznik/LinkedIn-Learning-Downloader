package app;

import static helper.Constants.JAVASCRIPTEXECUTOR;
import static helper.Constants.MAXBITRATE;
import static helper.Constants.PASSWORD;
import static helper.Constants.URLLOGIN;
import static helper.Constants.USERNAME;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;
import static helper.Constants.WEBDRIVER;

public class Authenticator {

    public void login(String course) throws IOException {
        WEBDRIVER.get(URLLOGIN);
        WEBDRIVER.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement elemUsername = WEBDRIVER.findElement(By.id("auth-id-input"));
        elemUsername.sendKeys(USERNAME);

        WebElement button = WEBDRIVER.findElement(By.id("auth-id-button"));

        JAVASCRIPTEXECUTOR.executeScript("arguments[0].removeAttribute('disabled','disabled')", button);

        button.click();

        // Next page
        WebElement elemPassword = WEBDRIVER.findElement(By.id("password"));
        elemPassword.sendKeys(PASSWORD);

        WebElement button2 = WEBDRIVER.findElement(By.tagName("button"));
        button2.click();

        WEBDRIVER.get(course);

        // Hard code the maximum vbr (720) into the local storage of the browser
        JAVASCRIPTEXECUTOR.executeScript("window.localStorage.setItem('learning:media-player-prefs','{\"quality-prog\":" + MAXBITRATE + "}')");

        Downloader downloader = new Downloader();
        downloader.download();
    }
}
