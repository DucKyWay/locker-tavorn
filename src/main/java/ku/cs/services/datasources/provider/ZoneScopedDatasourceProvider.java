package ku.cs.services.datasources.provider;

public interface ZoneScopedDatasourceProvider<C> {
    C loadCollection(String zoneUid);
    void saveCollection(String zoneUid, C collection);
}
