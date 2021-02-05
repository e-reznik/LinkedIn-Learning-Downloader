package helper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public final class Constants {

    private Constants() {

    }

    public static final String USERNAME = "..."; // TODO: your LinkedIn username
    public static final String PASSWORD = "..."; // TODO: your LinkedIn password
    public static final String BASEDIR = "..."; // TODO: your videos directory

    public static final String AUDIONAME = "beep2.wav";

    public static final String URLLOGIN = "https://www.linkedin.com/learning-login/";
    public static final WebDriver driver = new FirefoxDriver();
}
