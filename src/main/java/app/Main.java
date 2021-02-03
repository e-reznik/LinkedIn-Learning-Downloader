package app;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        final String COURSE = "https://www.linkedin.com/learning/secure-coding-in-java/";

        Login l = new Login();
        l.loginToLI(COURSE);

    }

}
