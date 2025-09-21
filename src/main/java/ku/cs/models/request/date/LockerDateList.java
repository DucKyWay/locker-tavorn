package ku.cs.models.request.date;

import java.time.LocalDate;
import java.util.ArrayList;

public class LockerDateList {
   ArrayList<LockerDate> dateList = new ArrayList<>();
   public LockerDateList(){}
    public LockerDateList(ArrayList<LockerDate> dateList){
        this.dateList = dateList;
    }
    public void addDateList(LockerDate date){
       this.dateList.add(date);
    }
    public ArrayList<LockerDate> getDateList(){
       return dateList;
    }
    public LockerDate findDatebyId(String uuid){
       for(LockerDate date : dateList){
           if(date.getUuidLocker().equals(uuid)){
               return date;
           }
       }
       return null;
    }
}
