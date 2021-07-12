package si.irose.posttracking.reportutil;

import static java.lang.String.format;
import static java.util.Comparator.comparing;

import si.irose.posttracking.statsdata.AddressStats;
import si.irose.posttracking.statsdata.DailyStats;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ReportFormatter {

    private final static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm:ss");
    private final static String RECORD_FORMAT = "s \t | %s | records: %s \r\n";

    public String formatReport(List<DailyStats> dailyStatsList) {
        StringBuilder report = new StringBuilder();
        int addressMaxLength = getAddressMaxLength(dailyStatsList) + 1;
        String lineDelimiter = format("%" + (addressMaxLength + 42) + "s\r\n", " ").replaceAll(" ", "_");
        String duration;

        for (DailyStats dayStats : dailyStatsList) {
            report.append(lineDelimiter).append(dayStats.getDay().toString()).append("\r\n");
            dayStats.getRecords().sort(comparing(AddressStats::getArrival).reversed());
            for (AddressStats stats : dayStats.getRecords()) {
                if (stats.getRecordCount() == 1) {
                    duration = format("%14s %4s", stats.getArrival().format(TIME_FORMAT), " ");
                } else duration =
                        stats.getArrival().format(TIME_FORMAT) + " - " + stats.getDeparture().format(TIME_FORMAT);

                report.append(format("%-" + addressMaxLength + RECORD_FORMAT,
                        stats.getFullAddress(), duration, stats.getRecordCount()));
            }
        }
        report.append(lineDelimiter);

        return report.toString();
    }

    private int getAddressMaxLength(List<DailyStats> dailyStatsList) {
        Optional<Optional<String>> maxAddressLength = dailyStatsList.stream()
                .map(DailyStats::getRecords)
                .map(x -> x.stream()
                        .map(AddressStats::getFullAddress)
                        .max(comparing(String::length)))
                .max(comparing(y -> y.get().length()));

        return maxAddressLength.get().get().length();
    }

}
