package si.irose.posttracking.report;

import si.irose.posttracking.basedata.Address;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class AddressStats {

    private Address address;
    private String postName;
    private LocalDate day;
    private LocalTime arrival;
    private LocalTime departure;
    private int recordCount;

    public AddressStats(Address address, String postName, LocalDate day, LocalTime arrival, LocalTime departure, int recordCount) {
        this.address = address;
        this.postName = postName;
        this.day = day;
        this.arrival = arrival;
        this.departure = departure;
        this.recordCount = recordCount;
    }

    public LocalDate getDay() {
        return day;
    }

    @Override
    public String toString() {
        return address + " " + postName +" | " + arrival + " | " + departure + " | records: " + recordCount + "\r\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressStats that = (AddressStats) o;
        return recordCount == that.recordCount && Objects.equals(address, that.address) && Objects.equals(day, that.day) && Objects.equals(arrival, that.arrival) && Objects.equals(departure, that.departure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, day, arrival, departure, recordCount);
    }

}
