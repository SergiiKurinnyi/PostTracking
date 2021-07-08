package si.irose.posttracking.dataparser;

import static java.lang.Integer.parseInt;

import si.irose.posttracking.address.Address;
import si.irose.posttracking.post.Post;
import si.irose.posttracking.trackinglog.TrackingLog;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class DataParser {

    private List<Post> postList = new LinkedList<>();
    private List<Address> addressList = new LinkedList<>();
    private List<TrackingLog> trackingLogs = new LinkedList<>();

    public List<Post> getPostList() {
        return postList;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public List<TrackingLog> getTrackingLogs() {
        return trackingLogs;
    }

    public void processData(List<String> stringList) {
        for (String string : stringList) {
            String[] lines = string.split(",");
            allocateData(lines);
        }
    }

    private void allocateData(String[] lines) {
        try {
            if (lines.length == 2) {
                postList.add(new Post(parseInt(lines[0]), lines[1]));
            } else if (lines.length == 3) {
                trackingLogs.add(new TrackingLog(parseInt(lines[0]), parseInt(lines[1]), Timestamp.valueOf(lines[2])));
            } else if (lines.length == 6) {
                char houseDetails = lines[4].length() > 0 ? lines[4].charAt(0) : ' ';
                addressList.add(new Address(
                        parseInt(lines[0]), lines[1], lines[2], parseInt(lines[3]), houseDetails,
                        parseInt(lines[5])));
            }
        } catch (NumberFormatException e) {
            System.out.println("Error parsing numbers from file");
            e.printStackTrace();
        }
    }

}
