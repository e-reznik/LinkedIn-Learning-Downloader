package app;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        final String COURSE = "https://www.linkedin.com/learning/java-14-ein-erster-blick/";

        Login l = new Login();
        l.loginToLI(COURSE);

    }

}
