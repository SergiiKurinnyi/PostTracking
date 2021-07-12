package si.irose.posttracking.datauitl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import si.irose.posttracking.basedata.Address;
import si.irose.posttracking.basedata.TrackingLog;
import si.irose.posttracking.datautil.DataParser;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataParserTest {

    private final DataParser dataParser = new DataParser();

    @Test
    void parseData_ShouldParsePostDataToMap_IfFilesContainProperData() {
        Map<Integer, String> expected = new HashMap<>();
        expected.put(1000, "Ljubljana");
        expected.put(3000, "Celje");

        List<String> testArgument = new LinkedList<>();
        testArgument.add("1000,Ljubljana");
        testArgument.add("3000,Celje");
        testArgument.add("1,Ljubljana,Slovenčeva ulica,19,a,1000");
        testArgument.add("1,1,2010-06-01 10:00:13");
        dataParser.parseData(testArgument);

        assertEquals(expected, dataParser.getIdToPostMap());
    }

    @Test
    void parseData_ShouldParseAddressDataToMap_IfFilesContainProperData() {
        Map<Integer, Address> expected = new HashMap<>();
        expected.put(1, new Address(
                1, "Ljubljana", "Slovenčeva ulica", 19, 'a', 1000));
        expected.put(2, new Address(2, "Celje", "Gosposka", 1, ' ', 3000));

        List<String> testArgument = new LinkedList<>();
        testArgument.add("1,Ljubljana,Slovenčeva ulica,19,a,1000");
        testArgument.add("2,Celje,Gosposka,1,,3000");
        testArgument.add("1000,Ljubljana");
        testArgument.add("1,1,2010-06-01 10:00:13");
        dataParser.parseData(testArgument);

        assertEquals(expected, dataParser.getIdToAddressMap());
    }

    @Test
    void parseData_ShouldParseTrackingLogDataToList_IfFilesContainProperData() {
        List<TrackingLog> expected = new LinkedList<>();
        expected.add(new TrackingLog(1, 1, Timestamp.valueOf("2010-06-01 10:00:13")));
        expected.add(new TrackingLog(2, 1, Timestamp.valueOf("2010-06-01 10:00:18")));

        List<String> testArgument = new LinkedList<>();
        testArgument.add("1000,Ljubljana");
        testArgument.add("1,Ljubljana,Slovenčeva ulica,19,a,1000");
        testArgument.add("1,1,2010-06-01 10:00:13");
        testArgument.add("2,1,2010-06-01 10:00:18");
        dataParser.parseData(testArgument);

        assertEquals(expected, dataParser.getTrackingLogs());
    }

    @Test
    void parseData_ShoulThrowIllegalStateException_IfDataIsCorrectButNotEnoughForReport() {
        List<String> testArgument = new LinkedList<>();
        testArgument.add("123");
        testArgument.add("123,abc,efg,ijkl");
        testArgument.add("1000,Ljubljana");
        testArgument.add("3000,Celje");
        testArgument.add("1,Ljubljana,Slovenčeva ulica,19,a,1000");
        testArgument.add("2,Celje,Gosposka,1,,3000");

        Exception thrown = assertThrows(IllegalStateException.class, () -> dataParser.parseData(testArgument));
        String expected = "Not enough data for report. Please check file(s) content.";
        String actual = thrown.getMessage();

        assertTrue(actual.contains(expected));
    }

    @Test
    void parseData_ShouldThrowNumberFormatException_IfFileContentStructureIsWrong() {
        List<String> testArgument = new LinkedList<>();
        testArgument.add("aaa, 123");

        Exception thrown = assertThrows(NumberFormatException.class, () -> dataParser.parseData(testArgument));
        String expected = "Error parsing data. Please check file(s) contents.";
        String actual = thrown.getMessage();

        assertTrue(actual.contains(expected));
    }

}
