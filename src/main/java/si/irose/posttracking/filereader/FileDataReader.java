package si.irose.posttracking.filereader;

import java.io.*;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class FileDataReader {

    public List<String> readFile(List<Path> fileList) {
        if (fileList == null || fileList.isEmpty()) {
            throw new IllegalArgumentException("FileList is empty.");
        }
        List<String> allFilesLines = new LinkedList<>();

        for (Path file : fileList) {
            String line;
            try (BufferedReader bf = new BufferedReader(new FileReader(file.toFile()))) {
                while ((line = bf.readLine()) != null) {
                    allFilesLines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (allFilesLines.isEmpty() || (allFilesLines.size() < 2 && allFilesLines.get(0).length() < 1)) {
            throw new IllegalStateException("Nothing to read. File(s) might be empty.");
        }

        return allFilesLines;
    }

}
