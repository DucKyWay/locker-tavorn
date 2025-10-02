package ku.cs.services.datasources.provider;

import ku.cs.models.locker.LockerList;
import ku.cs.services.datasources.LockerListFileDatasource;

public class LockerDatasourceProvider implements ZoneScopedDatasourceProvider<LockerList> {
    private static final String BASE_DIR = "data/lockers";

    @Override
    public LockerList loadCollection(String zoneUid) {
        return new LockerListFileDatasource(BASE_DIR, "zone-" + zoneUid + ".json").readData();
    }

    @Override
    public void saveCollection(String zoneUid, LockerList collection) {
        new LockerListFileDatasource(BASE_DIR, "zone-" + zoneUid + ".json").writeData(collection);
    }
}
