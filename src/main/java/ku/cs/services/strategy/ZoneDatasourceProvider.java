package ku.cs.services.strategy;

import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.ZoneListFileDatasource;

public class ZoneDatasourceProvider implements DatasourceProvider<ZoneList> {
    private final ZoneListFileDatasource datasource;

    public ZoneDatasourceProvider() {
        this.datasource = new ZoneListFileDatasource("data", "test-zone-data.json");
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
