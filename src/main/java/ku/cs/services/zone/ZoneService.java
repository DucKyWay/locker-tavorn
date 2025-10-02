package ku.cs.services.zone;

import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.locker.LockerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.LockerListFileDatasource;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ZoneService {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
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

    public void deleteZoneAndFiles(Zone zone, ZoneList zones, OfficerList officers) {
        // delete zone
        zones.removeZone(zone);
        zonesProvider.saveCollection(zones);

        // delete zone on officer
        for (Officer officer : officers.getOfficers()) {
            if (officer.getZoneUids().contains(zone.getZoneUid())) {
                officer.removeZoneUid(zone.getZoneUid());
            }
        }
        officersProvider.saveCollection(officers);

        // delete relate file
        deleteRelatedFiles(zone.getZoneUid());
    }

    private void deleteRelatedFiles(String zoneUid) {
        try {
            Path lockerPath = Paths.get("data", "lockers", "zone-" + zoneUid + ".json");
            if (Files.deleteIfExists(lockerPath)) {
                System.out.println("Deleted locker file: " + lockerPath);
            }
        } catch (IOException e) {
            System.out.println("Failed to delete locker JSON for zoneUid=" + zoneUid);
        }

        // Not Delete Request because it has history
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
