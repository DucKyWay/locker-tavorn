package ku.cs.services.datasources.provider;

import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.ZoneListFileDatasource;

public class ZoneDatasourceProvider implements DatasourceProvider<ZoneList> {
    private final ZoneListFileDatasource datasource;

    public ZoneDatasourceProvider() {
        this.datasource = new ZoneListFileDatasource("data", "zone-data.json");
    }

    @Override
    public ZoneList loadCollection() {
        return datasource.readData();
    }

    @Override
    public void saveCollection(ZoneList collection) {
        datasource.writeData(collection);
    }
}
