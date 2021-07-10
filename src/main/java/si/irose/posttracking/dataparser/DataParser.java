package si.irose.posttracking.dataparser;

import static java.lang.Integer.parseInt;

import si.irose.posttracking.basedata.Address;
import si.irose.posttracking.basedata.Post;
import si.irose.posttracking.basedata.TrackingLog;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class DataParser {

    private final List<Post> postList = new LinkedList<>();
    private final List<Address> addressList = new LinkedList<>();
    private final List<TrackingLog> trackingLogs = new LinkedList<>();

    public List<Post> getPostList() {
        return postList;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public List<TrackingLog> getTrackingLogs() {
        return trackingLogs;
    }

    public void processData(List<String> lines) {
        for (String string : lines) {
            String[] linesArray = string.split(",");
            allocateData(linesArray);
        }
    }

    private void allocateData(String[] linesArray) {
        try {
            if (linesArray.length == 2) {
                postList.add(new Post(parseInt(linesArray[0]), linesArray[1]));
            } else if (linesArray.length == 3) {
                trackingLogs.add(new TrackingLog(parseInt(linesArray[0]), parseInt(linesArray[1]), Timestamp.valueOf(linesArray[2])));
            } else if (linesArray.length == 6) {
                char houseDetails = linesArray[4].length() > 0 ? linesArray[4].charAt(0) : ' ';
                addressList.add(new Address(
                        parseInt(linesArray[0]), linesArray[1], linesArray[2], parseInt(linesArray[3]), houseDetails,
                        parseInt(linesArray[5])));
            }
        } catch (NumberFormatException e) {
            System.out.println("Error parsing numbers from file");
            e.printStackTrace();
        }
    }

}
