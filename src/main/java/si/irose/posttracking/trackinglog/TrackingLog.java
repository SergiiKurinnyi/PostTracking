package si.irose.posttracking.trackinglog;

import java.sql.Timestamp;

public class TrackingLog {

    private int id;
    private int address_id;
    private Timestamp detected_date;

    public TrackingLog(int id, int address_id, Timestamp detected_date) {
        this.id = id;
        this.address_id = address_id;
        this.detected_date = detected_date;
    }

    public int getId() {
        return id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public Timestamp getDetected_date() {
        return detected_date;
    }

    @Override
    public String toString() {
        return "TrackingLog{" +
                "id=" + id +
                ", address_id=" + address_id +
                ", detected_date=" + detected_date +
                '}';
    }

}
