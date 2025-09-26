package ku.cs.services.datasources;

import ku.cs.models.account.User;
import ku.cs.models.account.UserList;
import ku.cs.models.locker.*;
import ku.cs.services.utils.UuidUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public LockerList readData() {
      return delegate.readData();
    }



    @Override
    public void writeData(LockerList data) {
        data.genId();
        delegate.writeData(data);
    }
}
