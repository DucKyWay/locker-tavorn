package ku.cs.services.datasources.provider;

import ku.cs.models.request.RequestList;
import ku.cs.services.datasources.RequestListFileDatasource;

public class RequestDatasourceProvider implements ZoneScopedDatasourceProvider<RequestList> {
    private static final String BASE_DIR = "data/requests";

    @Override
    public RequestList loadCollection(String zoneUid) {
        return new RequestListFileDatasource(BASE_DIR, "zone-" + zoneUid + ".json").readData();
    }

    @Override
    public void saveCollection(String zoneUid, RequestList collection) {
        new RequestListFileDatasource(BASE_DIR, "zone-" + zoneUid + ".json").writeData(collection);
    }
}
