package ku.cs.services.datasources.provider;

import ku.cs.models.locker.LockerList;
import ku.cs.models.request.RequestList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.LockerListFileDatasource;
import ku.cs.services.datasources.RequestListFileDatasource;

import java.util.ArrayList;
import java.util.List;

public class RequestDatasourceProvider implements ZoneScopedDatasourceProvider<RequestList> {
    private static final String BASE_DIR = "data/requests";
    private final ZoneList zones = new ZoneDatasourceProvider().loadCollection();

    @Override
    public RequestList loadCollection(String zoneUid) {
        return new RequestListFileDatasource(BASE_DIR, "zone-" + zoneUid + ".json").readData();
    }

    @Override
    public void saveCollection(String zoneUid, RequestList collection) {
        new RequestListFileDatasource(BASE_DIR, "zone-" + zoneUid + ".json").writeData(collection);
    }

    public List<RequestList> loadAllCollectionLists() {
        List<RequestList> list = new ArrayList<>();
        for(Zone zone : zones.getZones()) {
            list.add(new RequestListFileDatasource(BASE_DIR, "zone-" + zone.getZoneUid() + ".json").readData());
        }
        return list;
    }

    public RequestList loadAllCollections() {
        RequestList allRequests = new RequestList();

        for (Zone zone : zones.getZones()) {
            RequestList zoneRequests = new RequestListFileDatasource(BASE_DIR, "zone-" + zone.getZoneUid() + ".json").readData();

            if (zoneRequests != null && zoneRequests.getRequests() != null) {
                allRequests.addRequest(zoneRequests.getRequests());
            }
        }

        return allRequests;
    }
}
