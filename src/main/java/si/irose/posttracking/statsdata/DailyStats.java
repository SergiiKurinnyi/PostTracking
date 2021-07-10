package si.irose.posttracking.statsdata;

import java.time.LocalDate;
import java.util.List;

public class DailyStats {

    private LocalDate day;
    private List<AddressStats> records;

    public DailyStats(LocalDate day, List<AddressStats> records) {
        this.day = day;
        this.records = records;
    }

    public LocalDate getDay() {
        return day;
    }

    public List<AddressStats> getRecords() {
        return records;
    }

    @Override
    public String toString() {
        return day + "\r\n" + records;
    }

}
