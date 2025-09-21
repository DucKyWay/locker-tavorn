package ku.cs.services.datasources;

import ku.cs.models.request.date.LockerDate;
import ku.cs.models.request.date.LockerDateList;

public class DateListFileDatasource implements Datasource<LockerDateList> {
    private  final JsonListFileDatasource<LockerDate, LockerDateList> delegate;

    public DateListFileDatasource(String directoryName, String fileName) {
        this.delegate = new JsonListFileDatasource<>(
                directoryName,
                fileName,
                LockerDateList::new,
                LockerDateList::getDateList,
                LockerDateList::addDateList,
                LockerDate.class
        );
    }

    @Override
    public LockerDateList readData() {
        return delegate.readData();
    }

    @Override
    public void writeData(LockerDateList data) {
        delegate.writeData(data);
    }
}
