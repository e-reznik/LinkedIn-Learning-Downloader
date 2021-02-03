package app;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        final String COURSE = "https://www.linkedin.com/learning/craft-a-great-github-profile/";

        Authenticator authenticator = new Authenticator();
        authenticator.login(COURSE);

    }
}
