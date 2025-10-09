package ku.cs.services.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeFormatUtil {

    private final DateTimeFormatter fullNumericFormatter;
    private final DateTimeFormatter fullFormatter;
    private final DateTimeFormatter fullDateFormatter;
    private final DateTimeFormatter shortFormFormatter;
    private final DateTimeFormatter shortDateFormatter;

    public TimeFormatUtil() {
        this.fullNumericFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

        this.fullFormatter = DateTimeFormatter.ofPattern(
                "dd MMMM yyyy HH:mm",
                new Locale("th", "TH")
        );

        this.fullDateFormatter = DateTimeFormatter.ofPattern(
                "dd MMMM yyyy",
                new Locale("th", "TH")
        );

        this.shortFormFormatter = DateTimeFormatter.ofPattern(
                "d MMM yyyy HH:mm",
                new Locale("th", "TH")
        );

        this.shortDateFormatter = DateTimeFormatter.ofPattern(
                "d MMM yyyy",
                new Locale("th", "TH")
        );
    }

    public String localDateTimeToString(LocalDateTime time) {
        Duration duration = Duration.between(time, LocalDateTime.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) return    seconds + " วิที่แล้ว";
        if (seconds < 3600) return  (seconds / 60) + " นาทีที่แล้ว";
        if (seconds < 86400) return (seconds / 3600) + " ชม.ที่แล้ว";
        return                      (seconds / 86400) + " วันที่แล้ว";
    }

    public String localDateTimeToLongString(LocalDateTime time) {
        Duration duration = Duration.between(time, LocalDateTime.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) return    "เมื่อ " + seconds + " วินาทีที่แล้ว";
        if (seconds < 3600) return  "เมื่อ " + (seconds / 60) + " นาทีที่แล้ว";
        if (seconds < 86400) return "เมื่อ " + (seconds / 3600) + " ชั่วโมงที่แล้ว";
        return                      "เมื่อ " + (seconds / 86400) + " วันที่แล้ว";
    }

    public String formatNumeric(LocalDateTime time) {
        if (time == null) return "-";
        return time.format(fullNumericFormatter);
    }

    public String formatFull(LocalDateTime time) {
        if (time == null) return "-";
        return time.format(fullFormatter);
    }

    public String formatFull(LocalDate date) {
        if (date == null) return "-";
        return date.format(fullDateFormatter);
    }

    public String formatShort(LocalDateTime time) {
        if (time == null) return "-";
        return time.format(shortFormFormatter);
    }

    public String formatShort(LocalDate date) {
        if (date == null) return "-";
        return date.format(shortDateFormatter);
    }
}