package ku.cs.services.datasources;

import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;


public class ZoneListFileDatasource implements Datasource<ZoneList> {
    private final JsonListFileDatasource<Zone, ZoneList> delegate;

    public ZoneListFileDatasource(String directoryName, String fileName) {
        this.delegate = new JsonListFileDatasource<>(
                directoryName,
                fileName,
                ZoneList::new,
                ZoneList::getZones,
                ZoneList::addZone,
                Zone.class
        );
    }

    @Override
    public ZoneList readData() {
        return delegate.readData();
    }

    @Override
    public void writeData(ZoneList data) {
        delegate.writeData(data);
    }
}
