package si.irose.posttracking.reportutil;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import si.irose.posttracking.statsdata.AddressStats;
import si.irose.posttracking.statsdata.DailyStats;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

public class ReportFormatterTest {

    private final ReportFormatter reportFormatter = new ReportFormatter();

    @Test
    void formatReport_ShouldFormatReport_IfMoreThanOneRecord() {
        StringBuilder expected = new StringBuilder();
        expected.append("___________________________________________________________________________________________");
        expected.append("\r\n");
        expected.append("2010-06-01\r\n");
        expected.append("Ljubljana, Slovenčeva ulica, 19a, 1000 Ljubljana  \t | 10:00:13 - 10:00:24 | records: 3 ");
        expected.append("\r\n");
        expected.append("___________________________________________________________________________________________");
        expected.append("\r\n");

        List<DailyStats> testSource = new LinkedList<>();
        List<AddressStats> addressStats = new LinkedList<>();
        String fullAddress = "Ljubljana, Slovenčeva ulica, 19a, 1000 Ljubljana";
        LocalDate day = LocalDate.parse("2010-06-01");
        LocalTime arrival = LocalTime.parse("10:00:13");
        LocalTime departure = LocalTime.parse("10:00:24");
        int recordCount = 3;
        addressStats.add(new AddressStats(fullAddress, day, arrival, departure, recordCount));
        testSource.add(new DailyStats(day, addressStats));
        String actual = reportFormatter.formatReport(testSource);

        assertEquals(expected.toString(), actual);
    }

    @Test
    void formatReport_ShouldFormatReport_IfNotMoreThanOneRecord() {
        StringBuilder expected = new StringBuilder();
        expected.append("___________________________________________________________________________________________");
        expected.append("\r\n");
        expected.append("2010-06-01\r\n");
        expected.append("Ljubljana, Slovenčeva ulica, 19a, 1000 Ljubljana  \t |       10:00:13      | records: 1 ");
        expected.append("\r\n");
        expected.append("___________________________________________________________________________________________");
        expected.append("\r\n");

        List<DailyStats> testSource = new LinkedList<>();
        List<AddressStats> addressStats = new LinkedList<>();
        String fullAddress = "Ljubljana, Slovenčeva ulica, 19a, 1000 Ljubljana";
        LocalDate day = LocalDate.parse("2010-06-01");
        LocalTime arrival = LocalTime.parse("10:00:13");
        LocalTime departure = LocalTime.parse("10:00:13");
        int recordCount = 1;
        addressStats.add(new AddressStats(fullAddress, day, arrival, departure, recordCount));
        testSource.add(new DailyStats(day, addressStats));
        String actual = reportFormatter.formatReport(testSource);

        assertEquals(expected.toString(), actual);
    }

}
