package ku.cs.services.datasources.provider;

import ku.cs.models.key.KeyList;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.KeyListFileDatasource;

public class KeyDatasourceProvider implements ZoneScopedDatasourceProvider<KeyList> {
    private static final String BASE_DIR = "data/keys";

    private final ZoneList zones = new ZoneDatasourceProvider().loadCollection();

    @Override
    public KeyList loadCollection(String zoneUid) {
        return new KeyListFileDatasource(BASE_DIR, "zone-" + zoneUid + ".json").readData();
    }

    @Override
    public void saveCollection(String zoneUid, KeyList collection) {
        new KeyListFileDatasource(BASE_DIR, "zone-" + zoneUid + ".json").writeData(collection);
    }

    public KeyList loadAllCollection() {
        KeyList allKeys = new KeyList();

        for(Zone zone : zones.getZones()) {
            KeyList zoneKeys = new KeyListFileDatasource(BASE_DIR, "zone-" + zone.getZoneUid() + ".json").readData();

            if(zoneKeys != null && zoneKeys.getKeys() != null) {
                allKeys.addKey(zoneKeys.getKeys());
            }
        }

        return allKeys;
    }
}
