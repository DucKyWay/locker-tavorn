package ku.cs.services.datasources.provider;

import ku.cs.models.key.KeyList;
import ku.cs.services.datasources.KeyListFileDatasource;

public class KeyDatasourceProvider implements ZoneScopedDatasourceProvider<KeyList> {
    private static final String BASE_DIR = "data/keys";

    @Override
    public KeyList loadCollection(String zoneUid) {
        return new KeyListFileDatasource(BASE_DIR, "zone-" + zoneUid + ".json").readData();
    }

    @Override
    public void saveCollection(String zoneUid, KeyList collection) {
        new KeyListFileDatasource(BASE_DIR, "zone-" + zoneUid + ".json").writeData(collection);
    }
}
