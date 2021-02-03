package app;

import static helper.Constants.BASEDIR;
import static helper.Constants.driver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Downloader {

    private static final Logger LOGGER = Logger.getLogger(Downloader.class.getName());

    private final String COURSETITLE;
    private final Map<String, List<String>> chapterLecturesMap = new HashMap();

    public Downloader() throws IOException {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        COURSETITLE = driver.findElement(By.tagName("h1")).getText();
        createVideoStructure();
    }

    private void createVideoStructure() throws IOException {
        List<WebElement> allContents = driver.findElements(By.xpath("//section[contains(@class, 'classroom-toc-chapter')]"));

        for (WebElement e : allContents) {
            WebElement chapter = e.findElement(By.xpath(".//span[contains(@class, 'classroom-toc-chapter__toggle-title')]"));
            chapterLecturesMap.put(chapter.getText(), new ArrayList<>());

            List<WebElement> videos = e.findElements(By.xpath(".//a[contains(@class, 'toc-item')]"));

            for (WebElement v : videos) {
                if (!v.getText().toLowerCase().contains("quiz")) {
                    chapterLecturesMap.get(chapter.getText()).add(v.getAttribute("href"));
                }
            }
        }
        createDirectory(COURSETITLE);

        iterate(chapterLecturesMap);
    }

    private void iterate(Map<String, List<String>> map) throws IOException {
        for (String chapter : map.keySet()) {
            createDirectory(COURSETITLE + "/" + chapter);
            // Index is needed for the number of the current video (lecture)
            for (int i = 0; i < map.get(chapter).size(); i++) {
                findVideoUrl(chapter, map.get(chapter).get(i), i + 1);
            }
        }
    }

    private void createDirectory(String dir) {
        File directory = new File(BASEDIR + dir);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    private void findVideoUrl(String chapter, String lectureUrl, int currentIndex) throws IOException {
        List<String> errorVideos = new ArrayList<>();
        driver.get(lectureUrl);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("return window.stop");

        // TODO: Find a more elegant solution
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            LOGGER.log(Level.WARNING, "Error while sleeping", ex);
        }

        WebElement linkToVideo = driver.findElement(By.tagName("video"));
        String videoTitle = driver.getTitle();
        String videoUrl = linkToVideo.getAttribute("src");

        if (videoUrl.isBlank()) {
            errorVideos.add(videoTitle);
            LOGGER.log(Level.WARNING, "Video: {0} could not be downloaded: {1}", new String[]{videoTitle, videoUrl});
        } else {
            downloadVideo(chapter, currentIndex, videoTitle, videoUrl);
        }

        if (!errorVideos.isEmpty()) {
            iterateErrorVideos(errorVideos);
        }
    }

    private void downloadVideo(String chapter, int currentIndex, String videoTitle, String videoUrl) {
        try {
            FileUtils.copyURLToFile(new URL(videoUrl),
                    new File("/home/evgenij/learningVids/" + COURSETITLE + "/" + chapter + "/" + currentIndex + ". " + videoTitle + ".mp4"), 5000, 5000);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, videoUrl, ex);
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, videoUrl, ex);
        }
    }

    private void iterateErrorVideos(List<String> errorVideos) {
        LOGGER.log(Level.INFO, "The following videos could not been downloaded:{0}", errorVideos.toArray());
    }
}