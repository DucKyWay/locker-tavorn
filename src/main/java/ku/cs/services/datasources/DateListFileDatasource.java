package ku.cs.services.datasources;

import ku.cs.models.locker.Date;
import ku.cs.models.locker.DateList;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;

public class DateListFileDatasource implements Datasource<DateList> {
    private  final JsonListFileDatasource<Date,DateList> delegate;

    public DateListFileDatasource(String directoryName, String fileName) {
        this.delegate = new JsonListFileDatasource<>(
                directoryName,
                fileName,
                DateList::new,
                DateList::getDateList,
                DateList::addDateList,
                Date.class
        );
    }

    @Override
    public DateList readData() {
        return delegate.readData();
    }

    @Override
    public void writeData(DateList data) {
        delegate.writeData(data);
    }
}
