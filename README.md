![Java CI with Maven](https://github.com/e-reznik/LinkedIn-Learning-Downloader/workflows/Java%20CI%20with%20Maven/badge.svg)

This software automatically downloads and saves all videos of a specific lecture from [LinkedIn Learning](https://www.linkedin.com/learning/me). A subscription is necessary, to login and parse the structure.

## Example Usage

1. Subscribe to LinkedIn Learning
2. Find a course of your interest: https://www.linkedin.com/learning/craft-a-great-github-profile/
3. Download this program
4. Find the method `main` and edit the URL of your course:
    ```java
    public static void main(String[] args) throws IOException {
        final String COURSE = "https://www.linkedin.com/learning/craft-a-great-github-profile/";

        Authenticator authenticator = new Authenticator();
        authenticator.login(COURSE);

    }
    ```
5. Specify your credentials and your directory in the class `Constants`:
    ```java
      public class Constants {

          public static final String USERNAME = "yourUsername"; // TODO: your LinkedIn username
          public static final String PASSWORD = "yourPassword"; // TODO: your LinkedIn password
          public static final String BASEDIR = "/home/user/videos/"; // TODO: your videos directory

          // ...
      }
    ```
6. Run your program

## Result

As you can see, even the proper folder structure will be maintained:

![linkedIn](https://user-images.githubusercontent.com/55981254/106777065-f0482000-6644-11eb-928e-c57d21560f49.png)


*Note: I created this program for educational purposes only! Use at your own risk!*
