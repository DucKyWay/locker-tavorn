package ku.cs.services;

import ku.cs.models.locker.LockerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.LockerListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;

import java.util.ArrayList;
import java.util.List;

public class ZoneService {
    // when locker in zone have changed
    Datasource<ZoneList> datasourceZoneList = new ZoneListFileDatasource("data", "test-zone-data.json");
    ZoneList zoneList = datasourceZoneList.readData();
    public void setLockerToZone(ZoneList zoneList){
        for(Zone zone : zoneList.getZones()){
            Datasource<LockerList> lockerListDatasource =
                    new LockerListFileDatasource(
                            "data/lockers",
                            "zone-" + zone.getZoneUid() + ".json"
                    );
            LockerList lockerList = lockerListDatasource.readData();
            System.out.println(lockerList.getLockers().size());
            zone.setTotalAvailable(lockerList.getAllAvailable());
            zone.setTotalAvailableNow(lockerList.getAllAvailableNow());
            zone.setTotalLocker(lockerList.getLockers().size());
            zone.setStatus(lockerList.getStatus());
        }
        datasourceZoneList.writeData(zoneList);
    }

    public List<Zone> getZonesByUids(List<String> uids) {
        List<Zone> result = new ArrayList<>();
        for (String uid : uids) {
            Zone zone = zoneList.findZoneByUid(uid);
            if (zone != null) result.add(zone);
        }
        return result;
    }

    public Zone findZoneByName(String zoneName) {
        ZoneList zoneList = datasourceZoneList.readData();
        for(Zone zone : zoneList.getZones()){
            if(zoneName.equals(zone.getZoneName())){
                return zone;
            }
        }
        return null;
    }
}
