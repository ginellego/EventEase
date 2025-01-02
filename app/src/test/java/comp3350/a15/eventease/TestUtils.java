package comp3350.a15.eventease;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import comp3350.a15.eventease.application.EventEaseApp;




public class TestUtils {
    private static final File DB_SRC = new File("src/main/assets/db/EventEaseDB.script");

    public static File copyDB() throws IOException {
        final File target = File.createTempFile("temp-db", ".script");
        Files.copy(DB_SRC.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        EventEaseApp.setDBPathName(target.getAbsolutePath().replace(".script", ""));
        return target;
    }
}
