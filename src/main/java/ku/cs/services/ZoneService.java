package ku.cs.services;

import ku.cs.models.locker.LockerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.LockerListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;

public class ZoneService {
    // when locker in zone have changed
    static Datasource<ZoneList> datasourceZoneList = new ZoneListFileDatasource("data", "test-zone-data.json");
    public static void setLockerToZone(ZoneList zoneList){
        for(Zone zone : zoneList.getZones()){
            Datasource<LockerList> lockerListDatasource =
                    new LockerListFileDatasource(
                            "data/lockers",
                            "zone-" + zone.getIdZone() + ".json"
                    );
            LockerList lockerList = lockerListDatasource.readData();
            System.out.println(lockerList.getLockers().size());
            zone.setTotalAvailable(lockerList.getAllAvailable());
            zone.setTotalAvailableNow(lockerList.getAllAvalibleNow());
            zone.setTotalLocker(lockerList.getLockers().size());
            zone.setStatus(lockerList.getStatusString());
        }
        datasourceZoneList.writeData(zoneList);
    }
    public static Zone findZoneByName(String zoneName) {
        ZoneList zoneList = datasourceZoneList.readData();
        for(Zone zone : zoneList.getZones()){
            if(zoneName.equals(zone.getZone())){
                return zone;
            }
        }
        return null;
    }
}
