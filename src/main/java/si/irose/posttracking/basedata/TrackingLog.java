package si.irose.posttracking.basedata;

import java.sql.Timestamp;
import java.util.Objects;

public class TrackingLog {

    private int id;
    private int addressId;
    private Timestamp detectedDate;

    public TrackingLog(int id, int addressId, Timestamp detectedDate) {
        this.id = id;
        this.addressId = addressId;
        this.detectedDate = detectedDate;
    }

    public int getAddressId() {
        return addressId;
    }

    public Timestamp getDetectedDate() {
        return detectedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackingLog that = (TrackingLog) o;
        return id == that.id && getAddressId() == that.getAddressId() && Objects.equals(getDetectedDate(), that.getDetectedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getAddressId(), getDetectedDate());
    }

}
