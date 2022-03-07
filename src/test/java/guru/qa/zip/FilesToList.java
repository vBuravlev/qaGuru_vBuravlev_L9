package guru.qa.zip;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FilesToList {

    public static List<String> filesToList(String resourcesDirName) {
        try {
            return Files.walk(Paths.get(resourcesDirName))
                    .filter(Files::isRegularFile)
                    .map(x -> x.getFileName())
                    .map(x -> x.toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
