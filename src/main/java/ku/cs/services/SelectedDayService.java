package ku.cs.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ku.cs.models.request.date.LockerDate;
import ku.cs.models.request.date.LockerDateList;
import ku.cs.models.request.date.DateRange;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.LockerDateListFileDatasource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SelectedDayService {

    private static final LocalDate TODAY = LocalDate.now();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static Datasource<ZoneList> zoneListDatasource;
    private static ZoneList zoneList;
    private static Datasource<LockerDateList> dateListDatasource;
    private static LockerDateList dateList;

    private static void initializeDatasource(int zoneId) {

        dateListDatasource = new LockerDateListFileDatasource("data/dates", "zone-" +zoneId+ ".json");
        dateList = dateListDatasource.readData();
    }

    public static ObservableList<String> populateStartDateComboBox(int zoneId, String uuidLocker) {
        initializeDatasource(zoneId);
        return populateDates(uuidLocker, TODAY, TODAY.plusMonths(1), false);
    }

    public static ObservableList<String> populateEndDateComboBox(int zoneId, String uuidLocker, LocalDate startDate) {
        initializeDatasource(zoneId);
        return populateDates(uuidLocker, startDate, TODAY.plusMonths(1), true);
    }

    private static ObservableList<String> populateDates(String uuidLocker, LocalDate fromDate, LocalDate toDate, boolean stopOnBooked) {
        ObservableList<String> availableDates = FXCollections.observableArrayList();
        LockerDate lockerDates = dateList.findDatebyId(uuidLocker);

        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            if (!isBooked(lockerDates, date)) {
                availableDates.add(date.format(FORMATTER));
            } else if (stopOnBooked) { //for endDate
                break;
            }
        }
        return availableDates;
    }

    private static boolean isBooked(LockerDate lockerDates, LocalDate date) {
        if (lockerDates == null) return false;

        for (DateRange range : lockerDates.getDateList()) {
            LocalDate start = range.getStartDate();
            LocalDate end = range.getEndDate();

            if (!date.isBefore(start) && !date.isAfter(end)) {
                return true;
            }
        }
        return false;
    }
}
