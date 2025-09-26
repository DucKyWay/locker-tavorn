package ku.cs.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SelectedDayService {

    private final LocalDate TODAY = LocalDate.now();
    public final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");




    public  ObservableList<String> populateEndDateComboBox() {
        return populateDates(TODAY, TODAY.plusMonths(1));
    }

    private  ObservableList<String> populateDates(LocalDate fromDate, LocalDate toDate) {
        ObservableList<String> availableDates = FXCollections.observableArrayList();

        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            availableDates.add(date.format(FORMATTER));
        }
        return availableDates;
    }

    public boolean isBooked(LocalDate start,LocalDate end) {
        if (!TODAY.isBefore(start) && !TODAY.isAfter(end))return true;
        return false;
    }
}
