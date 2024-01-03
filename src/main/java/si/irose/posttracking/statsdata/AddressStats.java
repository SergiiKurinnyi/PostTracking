package si.irose.posttracking.statsdata;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class AddressStats {

    private String fullAddress;
    private LocalDate day;
    private LocalTime arrival;
    private LocalTime departure;
    private int recordCount;

    public AddressStats(String fullAddress, LocalDate day, LocalTime arrival, LocalTime departure, int recordCount) {
        this.fullAddress = fullAddress;
        this.day = day;
        this.arrival = arrival;
        this.departure = departure;
        this.recordCount = recordCount;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public LocalDate getDay() {
        return day;
    }

    public LocalTime getArrival() {
        return arrival;
    }

    public LocalTime getDeparture() {
        return departure;
    }

    public int getRecordCount() {
        return recordCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressStats stats = (AddressStats) o;
        return getRecordCount() == stats.getRecordCount() && Objects.equals(getFullAddress(), stats.getFullAddress()) && Objects.equals(getDay(), stats.getDay()) && Objects.equals(getArrival(), stats.getArrival()) && Objects.equals(getDeparture(), stats.getDeparture());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFullAddress(), getDay(), getArrival(), getDeparture(), getRecordCount());
    }

}
