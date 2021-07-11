package si.irose.posttracking.filereader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileReader {

    public List<File> getFileList(String path) throws IOException {
        List<File> files = Files.walk(Paths.get(path))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

        if (files.isEmpty()) {
            throw new FileNotFoundException("No files found at the specified folder.");
        }

        return files;
    }

}
