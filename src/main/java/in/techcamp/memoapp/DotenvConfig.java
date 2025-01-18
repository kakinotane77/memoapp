package in.techcamp.memoapp;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvConfig {
    private static final Dotenv dotenv = Dotenv.configure()
            .directory("./")
            .ignoreIfMissing()
            .load();

    public static String get(String key) {
        return dotenv.get(key);
    }
}