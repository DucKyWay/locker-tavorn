package ku.cs.services.strategy.account;

import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.services.datasources.OfficerListFileDatasource;

import java.util.List;

public class OfficerAccountProvider implements AccountProvider<Officer> {
    private final OfficerListFileDatasource datasource;

    public OfficerAccountProvider() {
        this.datasource = new OfficerListFileDatasource("data", "test-officer-data.json");
    }

    @Override
    public List<Officer> loadAccounts() {
        OfficerList list = datasource.readData();
        return list.getOfficers();
    }

    @Override
    public void saveAccounts(List<Officer> officers) {
        OfficerList list = new OfficerList();
        officers.forEach(list::addOfficer);
        datasource.writeData(list);
    }
}
