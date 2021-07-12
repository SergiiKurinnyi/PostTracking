package si.irose.posttracking.filereader;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class FileDataReaderTest {

    private final FileDataReader fileDataReader = new FileDataReader();

    @Test
    void readFile_ShouldThrowIllegalArgumentException_IfArgumentIsEmpty() {
        List<Path> testArgument = new LinkedList<>();
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> fileDataReader.readFile(testArgument));
        String expected = "FileList is empty.";
        String actual = thrown.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void readFile_ShouldThrowIllegalArgumentException_IfArgumentIsNull() {
        List<Path> testArgument = null;
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> fileDataReader.readFile(testArgument));
        String expected = "FileList is empty.";
        String actual = thrown.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void readFile_ShouldThrowIllegalStateException_IfFileIsEmpty() throws URISyntaxException {
        List<Path> testArgument = new LinkedList<>();
        Path testPath = Paths.get(getClass().getClassLoader().getResource("empty.csv").toURI());
        testArgument.add(testPath);

        Exception thrown = assertThrows(IllegalStateException.class, () -> fileDataReader.readFile(testArgument));
        String expected = "Nothing to read. File(s) might be empty.";
        String actual = thrown.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void readFile_ShouldReturnStringWithDataIfFileContainsProperData() throws URISyntaxException {
        List<String> expected = new LinkedList<>();
        expected.add("1,Ljubljana,Slovenčeva ulica,19,a,1000");
        expected.add("1000,Ljubljana");
        expected.add("1,1,2010-06-01 10:00:13");
        expected.add("2,1,2010-06-01 10:00:18");

        List<Path> testArgument = new LinkedList<>();
        Path testPath = Paths.get(getClass().getClassLoader().getResource("FullLog.csv").toURI());
        testArgument.add(testPath);
        List<String> actual = fileDataReader.readFile(testArgument);

        assertEquals(expected, actual);
    }

}
