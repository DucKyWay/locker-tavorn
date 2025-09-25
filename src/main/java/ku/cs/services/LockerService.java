package ku.cs.services;

import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.request.date.DateRange;
import ku.cs.models.request.date.LockerDate;
import ku.cs.models.request.date.LockerDateList;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.LockerDateListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LockerService {
    static Datasource<ZoneList> datasourceZoneList = new ZoneListFileDatasource("data", "test-zone-data.json");
    static ZoneList zoneList = datasourceZoneList.readData();
    public static LockerList setAvailableLockerList(LockerList lockerList){
        Datasource<LockerDateList> lockerDateListDatasource =
                new LockerDateListFileDatasource(
                        "data/dates",
                        "zone-" + zoneList.findZoneByName(lockerList.getZone()).getZoneUid() + ".json");

        LockerDateList lockerDateList = lockerDateListDatasource.readData();
        for(Locker locker : lockerList.getLockers()){
            LockerDate lockerDate = lockerDateList.findDatebyId(locker.getUuid());
                if (SelectedDayService.isBooked(lockerDate, LocalDate.now())) {
                    locker.setAvailable(false);
                }
                else{
                    //เดี่ยวต้องมาเช็คว่าไม่มีของในล็อกเกอร์แล้วค่อยให้เป็น true ตอนนี้ default ไปก่อน
                    //เดี่ยวต้องไปเซ็ท key ที่ติดกับ lockerด้วย ผ่าน request
                    locker.setAvailable(true);
                }
        }
        return lockerList;
    }
}
