package si.irose.posttracking.datauitl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import si.irose.posttracking.basedata.Address;
import si.irose.posttracking.basedata.TrackingLog;
import si.irose.posttracking.datautil.DataParser;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DataParserTest {

    private DataParser dataParser;

    @BeforeEach
    void init() {
        dataParser = new DataParser();
    }


    @Test
    void parseDataShouldParsePostDataToMap() {
        Map<Integer, String> expected = new HashMap<>();
        expected.put(1000, "Ljubljana");
        expected.put(3000, "Celje");

        List<String> testSource = new LinkedList<>();
        testSource.add("1000,Ljubljana");
        testSource.add("3000,Celje");

        dataParser.parseData(testSource);
        assertEquals(expected, dataParser.getIdToPostMap());
    }

    @Test
    void parseDataShouldParseAddressDataToMap() {
        Map<Integer, Address> expected = new HashMap<>();
        expected.put(1, new Address(
                1, "Ljubljana", "Slovenčeva ulica", 19,'a',1000));
        expected.put(2, new Address(2, "Celje", "Gosposka" ,1,' ', 3000));

        List<String> testSource = new LinkedList<>();
        testSource.add("1,Ljubljana,Slovenčeva ulica,19,a,1000");
        testSource.add("2,Celje,Gosposka,1,,3000");

        dataParser.parseData(testSource);
        assertEquals(expected, dataParser.getIdToAddressMap());
    }

    @Test
    void parseDataShouldParseTrackingLogDataToList() {
        List<TrackingLog> expected = new LinkedList<>();
        expected.add(new TrackingLog(1,1, Timestamp.valueOf("2010-06-01 10:00:13")));
        expected.add(new TrackingLog(2,1, Timestamp.valueOf("2010-06-01 10:00:18")));

        List<String> testSource = new LinkedList<>();
        testSource.add("1,1,2010-06-01 10:00:13");
        testSource.add("2,1,2010-06-01 10:00:18");

        dataParser.parseData(testSource);
        assertEquals(expected, dataParser.getTrackingLogs());
    }

}
