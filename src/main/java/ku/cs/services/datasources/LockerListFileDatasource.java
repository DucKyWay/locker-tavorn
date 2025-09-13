package ku.cs.services.datasources;

import ku.cs.models.account.User;
import ku.cs.models.account.UserList;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;

import java.io.FileNotFoundException;
import java.io.IOException;

public class LockerListFileDatasource implements Datasource<LockerList> {
    private final JsonListFileDatasource<Locker,LockerList> delegate;

    public LockerListFileDatasource(String directoryName, String fileName) {
        this.delegate = new JsonListFileDatasource<>(
                directoryName,
                fileName,
                LockerList::new,
                LockerList::getLockers,
                LockerList::addLocker,
                Locker.class
        );
    }

    @Override
    public LockerList readData(){
        return delegate.readData();
    }

    @Override
    public void writeData(LockerList data) {
        delegate.writeData(data);
    }
}
