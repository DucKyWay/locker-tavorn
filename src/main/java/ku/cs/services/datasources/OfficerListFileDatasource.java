package ku.cs.services.datasources;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;


public class OfficerListFileDatasource implements Datasource<OfficerList> {
    private final JsonListFileDatasource<Officer, OfficerList> delegate;
    public OfficerListFileDatasource(String directoryName, String fileName) {
        this.delegate = new JsonListFileDatasource<>(
                directoryName,
                fileName,
                OfficerList::new,
                OfficerList::getOfficers,
                OfficerList::addAccount,
                Officer.class
        );
    }
    @Override
    public OfficerList readData() {
        return delegate.readData();
    }

    @Override
    public void writeData(OfficerList data) {
        delegate.writeData(data);
    }
}
