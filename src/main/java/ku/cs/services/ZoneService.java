package ku.cs.services;

import ku.cs.models.locker.LockerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.LockerListFileDatasource;

public class ZoneService {

    public static void setLockerToZone(ZoneList zoneList){
        for(Zone zone : zoneList.getZones()){
            Datasource<LockerList> lockerListDatasource =
                    new LockerListFileDatasource(
                            "data/lockers",
                            "zone-" + zone.getIdZone() + ".json"
                    );
            LockerList lockerList = lockerListDatasource.readData();
            System.out.println(lockerList.getLockers().size());
            zone.setTotalAvailable(lockerList.getAllAvalibleStatus());
            zone.setTotalLocker(lockerList.getLockers().size());
            zone.setStatus(lockerList.getStatusString());
        }
    }
}
