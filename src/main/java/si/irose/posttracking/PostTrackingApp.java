package si.irose.posttracking;

import si.irose.posttracking.basedata.Address;
import si.irose.posttracking.basedata.Post;
import si.irose.posttracking.filereader.FileDataReader;
import si.irose.posttracking.dataparser.DataParser;
import si.irose.posttracking.filereader.FileReader;
import si.irose.posttracking.basedata.TrackingLog;
import si.irose.posttracking.report.AddressStats;
import si.irose.posttracking.report.DailyStats;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class PostTrackingApp {

    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args[0] == null) {
            throw new IllegalArgumentException("Please, specify folder that contains data files. " +
                    "Check ReadMe file for details");
        }

        final FileReader fileReader = new FileReader();
        final FileDataReader fileDataReader = new FileDataReader();
        final DataParser dataParser = new DataParser();

//        Map<LocalDate, Set<String>> finalMap = new HashMap<>();

        List<File> result = fileReader.getFileList(args[0]);
        List<String> stringList = fileDataReader.parseData(result);
        dataParser.processData(stringList);
        List<Address> addressList = dataParser.getAddressList();
        List<TrackingLog> trackingLogs = dataParser.getTrackingLogs();
        List<Post> postList = dataParser.getPostList();

        Set<AddressStats> addressStats = new HashSet<>();

        for (TrackingLog log : trackingLogs) {
            addressStats.add(new AddressStats(
                    findAddressById(addressList, log.getAddressId()),
                    findPostById(log, postList),
                    log.getDetectedDate().toLocalDateTime().toLocalDate(),
                    getArrivalTime(trackingLogs, log.getDetectedDate(), log.getAddressId()),
                    getDepartureTime(trackingLogs, log.getDetectedDate(), log.getAddressId()),
                    countAddressRecords(trackingLogs, log.getDetectedDate(), log.getAddressId())));

//            String arrivalTime = getArrivalTime(trackingLogs, log.getDetected_date(), log.getAddress_id()).toString();
//            String departureTime = getDepartureTime(trackingLogs, log.getDetected_date(), log.getAddress_id()).toString();
//
//            Set<String> lineSet = new HashSet<>();
//            StringBuilder sb = new StringBuilder();
//            sb.append("\n \r");
//            sb.append(findAddressById(addressList, log.getAddress_id()));
//            sb.append(" | " + arrivalTime);
//            sb.append(" | " + departureTime);
//            sb.append(" | " + countAddressRecords(trackingLogs, log.getDetected_date(), log.getAddress_id()) + " records");
//            lineSet.add(sb.toString());
//
//            if (finalMap.isEmpty() || !finalMap.containsKey(log.getDetected_date().toLocalDateTime().toLocalDate())) {
//                finalMap.put(log.getDetected_date().toLocalDateTime().toLocalDate(), lineSet);
//            }
//            finalMap.get(log.getDetected_date().toLocalDateTime().toLocalDate()).add(sb.toString());
        }

//        for (Map.Entry<LocalDate, Set<String>> entry : finalMap.entrySet()) {
//            System.out.print(entry.getKey());
//            System.out.println(entry.getValue());
//        }
//        System.out.println(addressStats);

        List<DailyStats> dailyStatsList = getDailyStats(new LinkedList<>(addressStats));
//        System.out.println(dailyStatsList);

        StringBuilder sb = new StringBuilder();
        for (DailyStats stat : dailyStatsList) {
            System.out.println(stat.getDay());
            List<AddressStats> addressStatsList = stat.getRecords();
            for (AddressStats addressStats1 : addressStatsList) {
                System.out.print(addressStats1);
            }
        }
    }

    private static List<DailyStats> getDailyStats(List<AddressStats> addressStats) {
        Map<LocalDate, List<AddressStats>> result = addressStats.stream()
                .collect(Collectors.groupingBy(AddressStats::getDay));

        List<DailyStats> dailyStatsList = new LinkedList<>();
        result.forEach((k,v) -> dailyStatsList.add(new DailyStats(k,v)));

        return dailyStatsList;
    }

    private static String findPostById(TrackingLog log, List<Post> postList) {
        return postList.get(log.getAddressId()).getPost();
    }

    private static Address findAddressById(List<Address> addressList, int addressId) {
        Optional<Address> result = addressList.stream()
                .filter(x -> x.getId() == addressId)
                .findFirst();

        return result.get();
    }

    private static int countAddressRecords(List<TrackingLog> trackingLogs, Timestamp timestamp, int addressId) {
        LocalDate day = timestamp.toLocalDateTime().toLocalDate();

        return (int) trackingLogs.stream()
                .filter(x -> x.getDetectedDate().toLocalDateTime().toLocalDate().equals(day))
                .filter(y -> y.getAddressId() == addressId)
                .count();
    }

    private static LocalTime getArrivalTime(List<TrackingLog> trackingLogs, Timestamp timestamp, int addressId) {
        LocalDate day = timestamp.toLocalDateTime().toLocalDate();
        Optional<TrackingLog> min = trackingLogs.stream()
                .filter(x -> x.getDetectedDate().toLocalDateTime().toLocalDate().equals(day))
                .filter(y -> y.getAddressId() == addressId)
                .min(Comparator.comparing(z -> z.getDetectedDate().toLocalDateTime().toLocalTime()));

        return min.get().getDetectedDate().toLocalDateTime().toLocalTime();
    }

    private static LocalTime getDepartureTime(List<TrackingLog> trackingLogs, Timestamp timestamp, int address_id) {
        LocalDate day = timestamp.toLocalDateTime().toLocalDate();
        Optional<TrackingLog> max = trackingLogs.stream()
                .filter(x -> x.getDetectedDate().toLocalDateTime().toLocalDate().equals(day))
                .filter(y -> y.getAddressId() == address_id)
                .max(Comparator.comparing(z -> z.getDetectedDate().toLocalDateTime().toLocalTime()));

        return max.get().getDetectedDate().toLocalDateTime().toLocalTime();
    }

}
