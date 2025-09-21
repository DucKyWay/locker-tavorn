package ku.cs.models.request.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class LockerDate {
    private String uuidLocker;
    ArrayList<DateRange> dateList = new ArrayList();
    public LockerDate(){}
    public LockerDate(String uuidLocker, ArrayList<DateRange> DateList){
        this.uuidLocker = uuidLocker;
        this.dateList = DateList;
    }
    public LockerDate(String uuidLocker){
        this.uuidLocker = uuidLocker;
    }
    public String getUuidLocker() {
        return uuidLocker;
    }
    public void addDateList(DateRange range) {
        dateList.add(range);
    }
    public void addDateList(LocalDate startDate, LocalDate endDate) {
        dateList.add(new DateRange(startDate, endDate));
    }

    public void setUuidLocker(String uuidLocker) {
        this.uuidLocker = uuidLocker;
    }

    public ArrayList<DateRange> getDateList() {
        return dateList;
    }

    public void setDateList(ArrayList<DateRange> dateList) {
        this.dateList = dateList;
    }
}
