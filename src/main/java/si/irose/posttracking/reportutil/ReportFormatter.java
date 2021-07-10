package si.irose.posttracking.reportutil;

import si.irose.posttracking.statsdata.AddressStats;
import si.irose.posttracking.statsdata.DailyStats;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ReportFormatter {

    public String formatReport(List<DailyStats> dailyStatsList) {
        StringBuilder sb = new StringBuilder();
        int addressMaxLength = getAddressMaxLength(dailyStatsList) + 1;
        String lineDelimiter = String.format("%" + (addressMaxLength + 36) + "s\n", " ").replaceAll(" ", "_");
        String recordFormat = "s | %s | %s | records: %s \n";

        for (DailyStats stat : dailyStatsList) {
            sb.append(lineDelimiter).append(stat.getDay().toString()).append("\r\n");
            for (AddressStats stats : stat.getRecords()) {
                sb.append(String.format("%-" + addressMaxLength + recordFormat, stats.getAddress() + " "
                        + stats.getPostName(), stats.getArrival(), stats.getDeparture(), stats.getRecordCount()));
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
