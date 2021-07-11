package si.irose.posttracking.filereader;

import java.io.*;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class FileDataReader {

    public List<String> readFile(List<File> fileList) {
        List<String> allFilesLines = new LinkedList<>();

        for (File file : fileList) {
            String line;
            try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
                while ((line = bf.readLine()) != null) {
                    allFilesLines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (allFilesLines.isEmpty()) {
            throw new IllegalStateException("Nothing to read. File(s) might be empty.");
        }

        return allFilesLines;
    }

}
