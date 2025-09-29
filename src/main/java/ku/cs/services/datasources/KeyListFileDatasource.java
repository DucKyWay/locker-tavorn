package ku.cs.services.datasources;

import ku.cs.models.key.Key;
import ku.cs.models.key.KeyList;

public class KeyListFileDatasource implements Datasource<KeyList>{
    private final JsonListFileDatasource<Key, KeyList> delegate;

    public KeyListFileDatasource(String directoryName, String fileName) {
        this.delegate = new JsonListFileDatasource<>(
                directoryName,
                fileName,
                KeyList::new,
                KeyList::getKeys,
                KeyList::addKey,
                Key.class
        );
    }
    @Override
    public KeyList readData() {
        return delegate.readData();
    }

    @Override
    public void writeData(KeyList data) {
        delegate.writeData(data);
    }
}
