package si.irose.posttracking.reportutil;

import si.irose.posttracking.statsdata.AddressStats;
import si.irose.posttracking.statsdata.DailyStats;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ReportFormatter {

    public String formatReport(List<DailyStats> dailyStatsList) {
        StringBuilder sb = new StringBuilder();
        int addressMaxLength = getAddressMaxLength(dailyStatsList);
        String lineDelimiter = String.format("%" + (addressMaxLength + 39) + "s\n", " ").replaceAll(" ", "_");
        String recordFormat = "s \t | %s | records: %s \n";
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm:ss");
        String duration;

        for (DailyStats stat : dailyStatsList) {
            sb.append(lineDelimiter).append(stat.getDay().toString()).append("\r\n");
            for (AddressStats stats : stat.getRecords()) {
                if (stats.getRecordCount() == 1) {
                    duration = String.format("%14s %4s", stats.getArrival().format(timeFormat), " ");
                } else duration =
                        stats.getArrival().format(timeFormat) + " - " + stats.getDeparture().format(timeFormat);

                sb.append(String.format("%-" + addressMaxLength + recordFormat, stats.getAddress() + " "
                        + stats.getPostName(), duration, stats.getRecordCount()));
            }
        }
        sb.append(lineDelimiter);

        return sb.toString();
    }

    //Probably there are options to optimize - without intermediate Optional<String>
    private int getAddressMaxLength(List<DailyStats> dailyStatsList) {
        Optional<Optional<String>> maxAddressLength = dailyStatsList.stream()
                .map(DailyStats::getRecords)
                .map(y -> y.stream()
                        .map(x -> x.getAddress().toString() + x.getPostName())
                        .max(Comparator.comparing(String::length)))
                .max(Comparator.comparing(z -> z.get().length()));

        return maxAddressLength.get().get().length();
    }

}
