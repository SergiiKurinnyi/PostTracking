package si.irose.posttracking.datautil;

import static java.lang.Integer.parseInt;

import si.irose.posttracking.basedata.Address;
import si.irose.posttracking.basedata.TrackingLog;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataParser {

    private final Map<Integer, String> idToPost = new HashMap<>();
    private final Map<Integer, Address> idToAddress = new HashMap<>();
    private final List<TrackingLog> trackingLogs = new LinkedList<>();

    public Map<Integer, String> getIdToPostMap() {
        return idToPost;
    }

    public Map<Integer, Address> getIdToAddressMap() {
        return idToAddress;
    }

    public List<TrackingLog> getTrackingLogs() {
        return trackingLogs;
    }

    public void parseData(List<String> lines) {
        for (String string : lines) {
            String[] linesArray = string.split(",");
            allocateData(linesArray);
        }

        if (idToPost.isEmpty() || idToAddress.isEmpty() || trackingLogs.isEmpty()) {
            throw new IllegalStateException("Not enough data for report. Please check file(s) content.");
        }
    }

    private void allocateData(String[] linesArray) {
        try {
            if (linesArray.length == 2) {
                idToPost.put(parseInt(linesArray[0]), linesArray[1]);
            } else if (linesArray.length == 3) {
                trackingLogs.add(new TrackingLog(parseInt(linesArray[0]), parseInt(linesArray[1]), Timestamp.valueOf(linesArray[2])));
            } else if (linesArray.length == 6) {
                char houseDetails = linesArray[4].length() > 0 ? linesArray[4].charAt(0) : ' ';
                idToAddress.put(parseInt(linesArray[0]), new Address(
                        parseInt(linesArray[0]), linesArray[1], linesArray[2], parseInt(linesArray[3]), houseDetails,
                        parseInt(linesArray[5])));
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Error parsing data. Please check file(s) contents.");
        }
    }

}
