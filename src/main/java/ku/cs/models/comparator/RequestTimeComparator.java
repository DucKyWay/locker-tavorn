package ku.cs.models.comparator;

import ku.cs.models.request.Request;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;

public class RequestTimeComparator implements Comparator<Request> {
    @Override
    public int compare(Request r1, Request r2) {
        LocalDateTime l1 = r1.getRequestTime();
        LocalDateTime l2 = r2.getRequestTime();
        // ถ้า null ให้ใช้เวลาที่ไกลมาก หรือเวลาปัจจุบันตามที่ต้องการ
        if (l1 == null && l2 == null) return 0;
        if (l1 == null) return 1;
        if (l2 == null) return -1;
        Duration duration1 = Duration.between(l1, LocalDateTime.now());
        Duration duration2 = Duration.between(l2, LocalDateTime.now());
        return Long.compare(duration1.getSeconds(), duration2.getSeconds());
    }
}
