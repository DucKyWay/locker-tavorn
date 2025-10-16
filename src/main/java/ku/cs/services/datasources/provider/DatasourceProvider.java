package ku.cs.services.datasources.provider;

public interface DatasourceProvider<C> {
    C loadCollection();

    void saveCollection(C collection);
}
