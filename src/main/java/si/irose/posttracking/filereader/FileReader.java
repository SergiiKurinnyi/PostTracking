package si.irose.posttracking.filereader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReader {

    public List<Path> getFileList(String sourcePath) {
        if (sourcePath == null || sourcePath.length() < 1) {
            throw new IllegalArgumentException("No path to file(s). Please specify path to folder.");
        }
        List<Path> files;
        try (Stream<Path> walk = Files.walk(Paths.get(sourcePath))) {
            files = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("File(s) not found. Please check path and folder contents.");
        }
        if (files.isEmpty()) {
            throw new IllegalStateException("File(s) not found. Please check path and folder contents.");
        }

        return files;
    }

}
