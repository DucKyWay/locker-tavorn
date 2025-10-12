package ku.cs.models.comparator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;

public class TimestampComparator<T extends TimeTrackable> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        LocalDateTime l1 = o1.getTimestamp();
        LocalDateTime l2 = o2.getTimestamp();

        if (l1 == null && l2 == null) return 0;
        if (l1 == null) return 1;
        if (l2 == null) return -1;

        Duration duration1 = Duration.between(l1, LocalDateTime.now());
        Duration duration2 = Duration.between(l2, LocalDateTime.now());

        return Long.compare(duration1.getSeconds(), duration2.getSeconds());
    }
}