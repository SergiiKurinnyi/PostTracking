package si.irose.posttracking.basedata;

import java.sql.Timestamp;

public class TrackingLog {

    private int id;
    private int addressId;
    private Timestamp detectedDate;

    public TrackingLog(int id, int addressId, Timestamp detectedDate) {
        this.id = id;
        this.addressId = addressId;
        this.detectedDate = detectedDate;
    }

    public int getId() {
        return id;
    }

    public int getAddressId() {
        return addressId;
    }

    public Timestamp getDetectedDate() {
        return detectedDate;
    }

}