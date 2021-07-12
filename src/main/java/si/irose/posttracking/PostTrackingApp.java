package si.irose.posttracking;

import si.irose.posttracking.datautil.DataProcessor;
import si.irose.posttracking.reportutil.ReportFormatter;
import si.irose.posttracking.statsdata.DailyStats;

import java.util.List;

public class PostTrackingApp {

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Please, specify folder that contains data files. " +
                    "Check ReadMe file for details");
        }

        final DataProcessor dataProcessor = new DataProcessor();
        final ReportFormatter reportFormatter = new ReportFormatter();

        List<DailyStats> dailyStatsList = dataProcessor.processData(args[0]);
        System.out.println(reportFormatter.formatReport(dailyStatsList));
    }

}
