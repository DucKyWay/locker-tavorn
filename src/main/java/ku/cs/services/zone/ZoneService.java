package ku.cs.services.zone;

import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.locker.LockerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.models.zone.ZoneStatus;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ZoneService {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();

    private final ZoneList zones;

    public ZoneService() {
        zones = zonesProvider.loadCollection();
    }

    public void updateLockersToZone(ZoneList zones){
        for(Zone zone : zones.getZones()){
            LockerList lockers = lockersProvider.loadCollection(zone.getZoneUid());

            zone.setTotalLocker(lockers.getLockers().size());
            zone.setTotalAvailable(lockers.getAllAvailable());
            zone.setTotalAvailableNow(lockers.getAllAvailableNow());
            zone.setStatus(lockers.getStatus());

            System.out.println(lockers.getLockers().size());
        }
        zonesProvider.saveCollection(zones);
    }

    public void deleteZoneAndFiles(Zone zone, ZoneList zones, OfficerList officers) {
        // delete zone
        zones.removeZone(zone);
        zonesProvider.saveCollection(zones);

        removeZoneFromOfficers(zone.getZoneUid(), officers);    // ลบหน้าที่ officer ใน zone
        deleteLockerFile(zone.getZoneUid());                    // ลบไฟล์ locker ใน zone
    }

    private void removeZoneFromOfficers(String zoneUid, OfficerList officers) {
        for (Officer officer : officers.getAccounts()) {
            if (officer.getZoneUids().contains(zoneUid)) {
                officer.removeZoneUid(zoneUid);
            }
        }
        officersProvider.saveCollection(officers);
        System.out.println("Removed zoneUid=" + zoneUid + " from all related officers");
    }

    private void deleteLockerFile(String zoneUid) {
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

    public void update(Zone zone) {
        for (int i = 0; i < zones.getZones().size(); i++) {
            if (zones.getZones().get(i).getZoneUid().equals(zone.getZoneUid())) {
                zones.getZones().set(i, zone);
                zonesProvider.saveCollection(zones);
                return;
            }
        }
    }

    /**
     * รีโหลดสถานะทุกจุดให้บริการ
     */
    public void reloadZoneStatus() {
        for (Zone zone : zones.getZones()) {
            updateZoneStatus(zone);
        }
        zonesProvider.saveCollection(zones);
    }

    /**
     *
     * @param zone
     */
    private void updateZoneStatus(Zone zone) {
        int total = zone.getTotalLocker();
        int available = zone.getTotalAvailableNow();
        int unavailable = zone.getTotalUnavailable();

        if (zone.getStatus() == ZoneStatus.INACTIVE) {
            zone.setStatus(ZoneStatus.INACTIVE);
            return;
        }

        if (available == 0 || unavailable == total) {
            zone.setStatus(ZoneStatus.FULL);
        } else {
            zone.setStatus(ZoneStatus.ACTIVE);
        }
    }


    public List<Zone> getZonesByUids(List<String> uids) {
        List<Zone> result = new ArrayList<>();
        for (String uid : uids) {
            Zone zone = zones.findZoneByUid(uid);
            if (zone != null) result.add(zone);
        }
        return result;
    }

    public boolean isFindZoneByUid(String zoneUid) {
        for(Zone zone : zones.getZones()){
            if(zoneUid.equals(zone.getZoneUid())){
                return true;
            }
        }
        return false;
    }

    public Zone findZoneByUid(String zoneUid) {
        for(Zone zone : zones.getZones()){
            if(zoneUid.equals(zone.getZoneUid())){
                return zone;
            }
        }
        return null;
    }

    public Zone findZoneByName(String zoneName) {
        for(Zone zone : zones.getZones()){
            if(zoneName.equals(zone.getZoneName())){
                return zone;
            }
        }
        return null;
    }

    public ZoneList getZones() {
        return zones;
    }
}
