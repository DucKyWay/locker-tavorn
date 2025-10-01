package ku.cs.services.zone;

import ku.cs.models.locker.LockerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.LockerListFileDatasource;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;

import java.util.ArrayList;
import java.util.List;

public class ZoneService {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();

    // when locker in zone have changed
    ZoneList zoneList = zonesProvider.loadCollection();
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
        zonesProvider.saveCollection(zoneList);
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
        for(Zone zone : zoneList.getZones()){
            if(zoneName.equals(zone.getZoneName())){
                return zone;
            }
        }
        return null;
    }
}
