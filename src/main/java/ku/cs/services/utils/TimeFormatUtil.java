package ku.cs.services.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeFormatUtil {
    public TimeFormatUtil() {}

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

        if (seconds < 60) return    "เมื่อ " + seconds + " วิที่แล้ว";
        if (seconds < 3600) return  "เมื่อ " + (seconds / 60) + " นาทีที่แล้ว";
        if (seconds < 86400) return "เมื่อ " + (seconds / 3600) + " ชม.ที่แล้ว";
        return                      "เมื่อ " + (seconds / 86400) + " วันที่แล้ว";
    }
}