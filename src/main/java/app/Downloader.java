package app;

import static helper.Constants.AUDIOFAIL;
import static helper.Constants.AUDIOSUCCESS;
import static helper.Constants.BASEDIR;
import static helper.Constants.JAVASCRIPTEXECUTOR;
import static helper.Constants.MAXBITRATE;
import static helper.Constants.REGEXFILENAME;
import static helper.Constants.SLEEPTIME;
import static helper.Constants.WEBDRIVER;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Downloader {

    private static final Logger LOGGER = Logger.getLogger(Downloader.class.getName());
    private String courseTitle;
    private final List<String> errorVideos = new ArrayList<>();

    public void download() {
        courseTitle = WEBDRIVER.findElement(By.tagName("h1")).getText();
        JAVASCRIPTEXECUTOR.executeScript("window.localStorage.setItem('learning:media-player-prefs','{\"quality-prog\":" + MAXBITRATE + "}')");

        final Map<String, List<String>> chapterLecturesMap = createVideoStructure();
        iterate(chapterLecturesMap);

        if (errorVideos.isEmpty()) {
            playSound(true);
            LOGGER.log(Level.INFO, "All videos have been downloaded successfully: {0} Errors.", errorVideos.size());
        } else {
            playSound(false);
            LOGGER.log(Level.INFO, "The following videos could not been downloaded: {0}\nYou can download them manually.", errorVideos.toArray());
        }
    }

    private Map<String, List<String>> createVideoStructure() {
        Map<String, List<String>> chapterLecturesMap = new HashMap<>();
        List<WebElement> allContents = WEBDRIVER.findElements(By.xpath("//section[contains(@class, 'classroom-toc-chapter')]"));

        for (WebElement e : allContents) {
            WebElement chapter = e.findElement(By.xpath(".//span[contains(@class, 'classroom-toc-chapter__toggle-title')]"));
            String chapterText = chapter.getText().replaceAll(REGEXFILENAME, "_");

            chapterLecturesMap.put(chapterText, new ArrayList<>());

            List<WebElement> videos = e.findElements(By.xpath(".//a[contains(@class, 'toc-item')]"));

            for (WebElement v : videos) {
                if (!v.getText().toLowerCase().contains("quiz")) {
                    chapterLecturesMap.get(chapterText).add(v.getAttribute("href"));
                }
            }
        }
        createDirectory(courseTitle);

        return chapterLecturesMap;
    }

    private void iterate(Map<String, List<String>> map) {
        for (String chapter : map.keySet()) {
            createDirectory(courseTitle + File.separator + chapter);
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
            LOGGER.log(Level.INFO, "Directory: {0}", dir);
        }
    }

    private void findVideoUrl(String chapter, String lectureUrl, int currentIndex) {
        WEBDRIVER.get(lectureUrl);

        JavascriptExecutor js = (JavascriptExecutor) WEBDRIVER;
        js.executeScript("return window.stop");

        // TODO: Find a more elegant solution
        try {
            Thread.sleep(SLEEPTIME);
        } catch (InterruptedException ex) {
            LOGGER.log(Level.WARNING, "Error while sleeping", ex);
        }

        WebElement linkToVideo = WEBDRIVER.findElement(By.tagName("video"));
        String videoTitle = WEBDRIVER.getTitle();
        String videoUrl = linkToVideo.getAttribute("src");

        if (!videoUrl.contains("vbr-" + MAXBITRATE)) {
            LOGGER.log(Level.SEVERE, "Video with less than {0} found. All downloads will be aborted! You can try it again.", MAXBITRATE);
            playSound(false);
            System.exit(0);
        }

        if (videoUrl.isBlank()) {
            errorVideos.add(videoTitle);
        } else {
            downloadVideo(chapter, currentIndex, videoTitle.replaceAll(REGEXFILENAME, "_"), videoUrl);
            LOGGER.log(Level.INFO, "URL: {0}", videoUrl);
        }
    }

    private void downloadVideo(String chapter, int currentIndex, String videoTitle, String videoUrl) {
        try {
            File file = new File(BASEDIR + courseTitle
                    + File.separator + chapter
                    + File.separator + currentIndex + ". " + videoTitle + ".mp4");

            FileUtils.copyURLToFile(new URL(videoUrl), file, 5000, 5000);

            LOGGER.log(Level.INFO, "File: {0}", file);

        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, videoUrl, ex);
        }
    }

    public String replaceIllegaleCharacters(String file) {

        return null;
    }

    private void playSound(boolean success) {
        AudioInputStream audioInputStream = null;
        String audioName = null;

        if (success) {
            audioName = AUDIOSUCCESS;
        } else {
            audioName = AUDIOFAIL;
        }

        try {
            audioInputStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResourceAsStream(audioName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            Thread.sleep(clip.getMicrosecondLength() / 1000);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } finally {
            try {
                audioInputStream.close();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

}
