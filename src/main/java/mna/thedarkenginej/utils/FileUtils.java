package mna.thedarkenginej.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class FileUtils {

    public static Properties getConfigProps(String fileName) throws IOException {
        // 1) check resources
        InputStream resourceStream = FileUtils.class.getClassLoader().getResourceAsStream(fileName);

        // 2) root dir file
        if (resourceStream == null) {
            resourceStream = Files.newInputStream(Path.of(fileName), StandardOpenOption.READ);
        }

        Properties properties = new Properties();
        properties.load(resourceStream);

        return properties;
    }

}
