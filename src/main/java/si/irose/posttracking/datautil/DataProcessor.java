package si.irose.posttracking.datautil;

import si.irose.posttracking.basedata.Address;
import si.irose.posttracking.basedata.Post;
import si.irose.posttracking.basedata.TrackingLog;
import si.irose.posttracking.filereader.FileDataReader;
import si.irose.posttracking.filereader.FileReader;
import si.irose.posttracking.statsdata.AddressStats;
import si.irose.posttracking.statsdata.DailyStats;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class DataProcessor {

    final FileReader fileReader = new FileReader();
    final FileDataReader fileDataReader = new FileDataReader();
    final DataParser dataParser = new DataParser();

    public List<DailyStats> processData(String folderName) throws IOException {
        List<File> fileList = fileReader.getFileList(folderName);
        List<String> allFilesLines = fileDataReader.readFile(fileList);
        dataParser.parseData(allFilesLines);
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
        }

        return getDailyStats(new LinkedList<>(addressStats));
    }

    //Probably there are options to optimize - without intermediate Map
    private List<DailyStats> getDailyStats(List<AddressStats> addressStats) {
        Map<LocalDate, List<AddressStats>> result = addressStats.stream()
                .collect(Collectors.groupingBy(AddressStats::getDay));

        List<DailyStats> dailyStatsList = new LinkedList<>();
        result.forEach((k,v) -> dailyStatsList.add(new DailyStats(k,v)));

        return dailyStatsList;
    }

    private Address findAddressById(List<Address> addressList, int addressId) {
        Optional<Address> result = addressList.stream()
                .filter(x -> x.getId() == addressId)
                .findFirst();

        return result.get();
    }

    private String findPostById(TrackingLog log, List<Post> postList) {
        return postList.get(log.getAddressId()).getPost();
    }

    private LocalTime getArrivalTime(List<TrackingLog> trackingLogs, Timestamp timestamp, int addressId) {
        LocalDate day = timestamp.toLocalDateTime().toLocalDate();
        Optional<TrackingLog> min = trackingLogs.stream()
                .filter(x -> x.getDetectedDate().toLocalDateTime().toLocalDate().equals(day))
                .filter(y -> y.getAddressId() == addressId)
                .min(Comparator.comparing(z -> z.getDetectedDate().toLocalDateTime().toLocalTime()));

        return min.get().getDetectedDate().toLocalDateTime().toLocalTime();
    }

    private LocalTime getDepartureTime(List<TrackingLog> trackingLogs, Timestamp timestamp, int address_id) {
        LocalDate day = timestamp.toLocalDateTime().toLocalDate();
        Optional<TrackingLog> max = trackingLogs.stream()
                .filter(x -> x.getDetectedDate().toLocalDateTime().toLocalDate().equals(day))
                .filter(y -> y.getAddressId() == address_id)
                .max(Comparator.comparing(z -> z.getDetectedDate().toLocalDateTime().toLocalTime()));

        return max.get().getDetectedDate().toLocalDateTime().toLocalTime();
    }

    private int countAddressRecords(List<TrackingLog> trackingLogs, Timestamp timestamp, int addressId) {
        LocalDate day = timestamp.toLocalDateTime().toLocalDate();

        return (int) trackingLogs.stream()
                .filter(x -> x.getDetectedDate().toLocalDateTime().toLocalDate().equals(day))
                .filter(y -> y.getAddressId() == addressId)
                .count();
    }

}
