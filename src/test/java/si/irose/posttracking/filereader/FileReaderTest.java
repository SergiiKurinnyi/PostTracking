package si.irose.posttracking.filereader;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class FileReaderTest {

    private final FileReader fileReader = new FileReader();

    @Test
    void getFileList_ShouldThrowIllegalArgumentException_IfArgumentIsNullOrEmpty() {
        String testArgument = "";
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> fileReader.getFileList(testArgument));
        String expected = "No path to file(s). Please specify path to folder.";
        String actual = thrown.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void getFileList_ShouldThrowIllegalArgumentException_IfArgumentIsNull() {
        Exception thrown = assertThrows(IllegalArgumentException.class, () -> fileReader.getFileList(null));
        String expected = "No path to file(s). Please specify path to folder.";
        String actual = thrown.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void getFileList_ShouldThrowIllegalArgumentException_IfArgumentIsNotValid() {
        String testArgument = "abc123";
        Exception thrown = assertThrows(IllegalStateException.class, () -> fileReader.getFileList(testArgument));
        String expected = "File(s) not found. Please check path and folder contents.";
        String actual = thrown.getMessage();

        assertEquals(expected, actual);
    }

}
