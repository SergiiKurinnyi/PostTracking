package si.irose.posttracking.datautil;

import si.irose.posttracking.basedata.Address;
import si.irose.posttracking.basedata.TrackingLog;
import si.irose.posttracking.filereader.FileDataReader;
import si.irose.posttracking.filereader.FileReader;
import si.irose.posttracking.statsdata.AddressStats;
import si.irose.posttracking.statsdata.DailyStats;

import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class DataProcessor {

    final FileReader fileReader = new FileReader();
    final FileDataReader fileDataReader = new FileDataReader();
    final DataParser dataParser = new DataParser();

    public List<DailyStats> processData(String sourcePath) {
        List<Path> fileList = fileReader.getFileList(sourcePath);
        List<String> allFilesLines = fileDataReader.readFile(fileList);
        dataParser.parseData(allFilesLines);
        List<TrackingLog> trackingLogs = dataParser.getTrackingLogs();
        Map<Integer, Address> idToAddressMap = dataParser.getIdToAddressMap();
        Map<Integer, String> idToPostMap = dataParser.getIdToPostMap();
        Set<AddressStats> addressStats = initAddressStats(trackingLogs, idToAddressMap, idToPostMap);

        return getDailyStats(addressStats);
    }

    private Set<AddressStats> initAddressStats(
            List<TrackingLog> trackingLogs, Map<Integer, Address> addressMap, Map<Integer, String> posts) {
        Set<AddressStats> addressStats = new HashSet<>();

        for (TrackingLog log : trackingLogs) {
            addressStats.add(new AddressStats(
                    getFullAddress(log, addressMap, posts),
                    log.getDetectedDate().toLocalDateTime().toLocalDate(),
                    getArrivalTime(trackingLogs, log.getDetectedDate(), log.getAddressId()),
                    getDepartureTime(trackingLogs, log.getDetectedDate(), log.getAddressId()),
                    countAddressRecords(trackingLogs, log.getDetectedDate(), log.getAddressId())));
        }

        return addressStats;
    }

    private String getFullAddress(TrackingLog log, Map<Integer, Address> addressMap, Map<Integer, String> posts) {
        StringBuilder fullAddress = new StringBuilder();
        Address currentAddress = addressMap.get(log.getAddressId());

        fullAddress.append(currentAddress.getCity()).append(", ");
        fullAddress.append(currentAddress.getStreet()).append(", ");
        fullAddress.append(currentAddress.getHouseNumber());
        if (currentAddress.getHouseDetails() != ' ') {
            fullAddress.append(currentAddress.getHouseDetails()).append(", ");
        } else fullAddress.append(", ");
        fullAddress.append(currentAddress.getPostId()).append(" ");
        fullAddress.append(posts.get(currentAddress.getPostId()));

        return fullAddress.toString();
    }

    private List<DailyStats> getDailyStats(Set<AddressStats> addressStats) {
        Map<LocalDate, List<AddressStats>> result = addressStats.stream()
                .collect(Collectors.groupingBy(AddressStats::getDay));

        List<DailyStats> dailyStats = new LinkedList<>();
        result.forEach((k, v) -> dailyStats.add(new DailyStats(k, v)));
        dailyStats.sort(Comparator.comparing(DailyStats::getDay).reversed());

        return dailyStats;
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
