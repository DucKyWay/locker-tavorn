package ku.cs.services.datasources.provider;

import ku.cs.models.locker.LockerList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.LockerListFileDatasource;

import java.util.ArrayList;
import java.util.List;

public class LockerDatasourceProvider implements ZoneScopedDatasourceProvider<LockerList> {
    private static final String BASE_DIR = "data/lockers";
    private final ZoneList zones = new ZoneDatasourceProvider().loadCollection();

    @Override
    public LockerList loadCollection(String zoneUid) {
        return new LockerListFileDatasource(BASE_DIR, "zone-" + zoneUid + ".json").readData();
    }

    @Override
    public void saveCollection(String zoneUid, LockerList collection) {
        new LockerListFileDatasource(BASE_DIR, "zone-" + zoneUid + ".json").writeData(collection);
    }

    public List<LockerList> loadAllCollections() {
        List<LockerList> list = new ArrayList<>();
        for(Zone zone : zones.getZones()) {
            list.add(new LockerListFileDatasource(BASE_DIR, "zone-" + zone.getZoneUid() + ".json").readData());
        }
        return list;
    }
}
