package ku.cs.models.request.date;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateRange {
    private LocalDate startDate;
    private LocalDate endDate;

    public DateRange() {} // 👈 no-args constructor

    public DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "DateRange{" + startDate + " - " + endDate + '}';
    }
}