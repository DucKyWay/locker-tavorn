package ku.cs.services.strategy;

public interface DatasourceProvider<C> {
    C loadCollection();

    void saveCollection(C collection);
}
