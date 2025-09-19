package ku.cs.models.request;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.util.Pair;
public class Date {
    private String uuidLocker;
    ArrayList<Pair<LocalDate,LocalDate>> DateList = new ArrayList();
    public Date(){}
    public Date(String uuidLocker,ArrayList<Pair<LocalDate,LocalDate>> DateList){
        this.uuidLocker = uuidLocker;
        this.DateList = DateList;
    }
    public Date(String uuidLocker){
        this.uuidLocker = uuidLocker;
    }
    public String getUuidLocker() {
        return uuidLocker;
    }
    public void addDateList(LocalDate startDate, LocalDate endDate){
        if(!DateList.isEmpty()){
            DateList.add(new Pair(startDate,endDate));
        }
    }

    public void setUuidLocker(String uuidLocker) {
        this.uuidLocker = uuidLocker;
    }

    public ArrayList<Pair<LocalDate, LocalDate>> getDateList() {
        return DateList;
    }

    public void setDateList(ArrayList<Pair<LocalDate, LocalDate>> dateList) {
        DateList = dateList;
    }
}
