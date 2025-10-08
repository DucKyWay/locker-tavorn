package ku.cs.services.session;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SelectedDayService {

    private final LocalDate TODAY = LocalDate.now();
    public final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public ObservableList<String> populateEndDateComboBox() {
        return populateDates(TODAY, TODAY.plusMonths(1));
    }

    private ObservableList<String> populateDates(LocalDate fromDate, LocalDate toDate) {
        ObservableList<String> availableDates = FXCollections.observableArrayList();

        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            availableDates.add(date.format(FORMATTER));
        }
        return availableDates;
    }

    public boolean isBooked(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        // ช่วงเวลา overlap ถ้า start1 <= end2 && start2 <= end1
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }

    public boolean isBooked(LocalDate start, LocalDate end) {
        if (start == null || end == null) {

            return false;
        }

        return !TODAY.isBefore(start) && !TODAY.isAfter(end);
    }

    public int getDaysBetween(LocalDate start, LocalDate end) {
        return (int) Math.abs(ChronoUnit.DAYS.between(start, end));
    }
}

