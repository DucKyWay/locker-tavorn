package ku.cs.models.locker;

import javafx.util.Pair;
import ku.cs.models.account.Officer;

import java.time.LocalDate;
import java.util.ArrayList;

public class DateList {
   ArrayList<Date> dateList = new ArrayList<>();
   public DateList(){}
    public DateList(ArrayList<Date> dateList){
        this.dateList = dateList;
    }
    public void addDateList(Date date){
       this.dateList.add(date);
    }
    public ArrayList<Date> getDateList(){
       return dateList;
    }
    public Date findDatebyId(String id){
       for(Date date : dateList){
           if(date.getUuidLocker().equals(id)){
               return date;
           }
       }
       return null;
    }
}
