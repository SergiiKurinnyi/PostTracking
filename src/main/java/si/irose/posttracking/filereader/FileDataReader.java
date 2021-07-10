package si.irose.posttracking.filereader;

import java.io.*;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class FileDataReader {

    public List<String> readFile(List<File> fileList) {
        List<String> fileLinesList = new LinkedList<>();

        for (File file : fileList) {
            String line;
            try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
                while ((line = bf.readLine()) != null) {
                    fileLinesList.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileLinesList;
    }

}
