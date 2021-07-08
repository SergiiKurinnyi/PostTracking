package si.irose.posttracking;

import si.irose.posttracking.address.Address;
import si.irose.posttracking.filedatareader.FileDataReader;
import si.irose.posttracking.dataparser.DataParser;
import si.irose.posttracking.filereader.FileReader;
import si.irose.posttracking.trackinglog.TrackingLog;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class PostTrackingApp {

    public static void main(String[] args) throws URISyntaxException, IOException {

        System.out.println(args.length);
        System.out.println(args[0]);

        final FileReader fileReader = new FileReader();
        final FileDataReader fileDataReader = new FileDataReader();
        final DataParser dataParser = new DataParser();

        Map<LocalDate, Set<String>> finalMap = new HashMap<>();

        List<File> result = fileReader.getFileList(args[0]);
        List<String> stringList = fileDataReader.parseData(result);
        dataParser.processData(stringList);
        List<Address> addressList = dataParser.getAddressList();
        List<TrackingLog> trackingLogs = dataParser.getTrackingLogs();

        for (TrackingLog log : trackingLogs) {
            String arrivalTime = getArrivalTime(trackingLogs, log.getDetected_date(), log.getAddress_id()).toString();
            String departureTime = getDepartureTime(trackingLogs, log.getDetected_date(), log.getAddress_id()).toString();

            Set<String> lineSet = new HashSet<>();
            StringBuilder sb = new StringBuilder();
            sb.append("\n \r");
            sb.append(findAddressById(addressList, log.getAddress_id()));
            sb.append(" | " + arrivalTime);
            sb.append(" | " + departureTime);
            sb.append(" | " + countAddressRecords(trackingLogs, log.getDetected_date(), log.getAddress_id()) + " records");
            lineSet.add(sb.toString());

            if (finalMap.isEmpty() || !finalMap.containsKey(log.getDetected_date().toLocalDateTime().toLocalDate())) {
                finalMap.put(log.getDetected_date().toLocalDateTime().toLocalDate(), lineSet);
            }
            finalMap.get(log.getDetected_date().toLocalDateTime().toLocalDate()).add(sb.toString());
        }

        for (Map.Entry<LocalDate, Set<String>> entry : finalMap.entrySet()) {
            System.out.print(entry.getKey());
            System.out.println(entry.getValue());
        }
    }

    private static Address findAddressById(List<Address> addressList, int id) {
        List<Address> result = addressList.stream()
                .filter(x -> x.getId() == id)
                .collect(Collectors.toList());

        return result.get(0);
    }

    private static int countAddressRecords(List<TrackingLog> trackingLogs, Timestamp timestamp, int address_id) {
        LocalDate day = timestamp.toLocalDateTime().toLocalDate();

        return (int) trackingLogs.stream()
                .filter(x -> x.getDetected_date().toLocalDateTime().toLocalDate().equals(day))
                .filter(y -> y.getAddress_id() == address_id)
                .count();
    }

    private static LocalTime getArrivalTime(List<TrackingLog> trackingLogs, Timestamp timestamp, int address_id) {
        LocalDate day = timestamp.toLocalDateTime().toLocalDate();

        Optional<TrackingLog> min = trackingLogs.stream()
                .filter(x -> x.getDetected_date().toLocalDateTime().toLocalDate().equals(day))
                .filter(y -> y.getAddress_id() == address_id)
                .min(Comparator.comparing(z -> z.getDetected_date().toLocalDateTime().toLocalTime()));

        LocalTime result = min.get().getDetected_date().toLocalDateTime().toLocalTime();

        return result;
    }

    private static LocalTime getDepartureTime(List<TrackingLog> trackingLogs, Timestamp timestamp, int address_id) {
        LocalDate day = timestamp.toLocalDateTime().toLocalDate();

        Optional<TrackingLog> max = trackingLogs.stream()
                .filter(x -> x.getDetected_date().toLocalDateTime().toLocalDate().equals(day))
                .filter(y -> y.getAddress_id() == address_id)
                .max(Comparator.comparing(z -> z.getDetected_date().toLocalDateTime().toLocalTime()));

        LocalTime result = max.get().getDetected_date().toLocalDateTime().toLocalTime();

        return result;
    }

}
