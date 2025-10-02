package ku.cs.services.accounts.strategy;

import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.services.datasources.OfficerListFileDatasource;

import java.util.List;

public class OfficerAccountProvider implements AccountProvider<Officer, OfficerList> {
    private final OfficerListFileDatasource datasource;

    public OfficerAccountProvider() {
        this.datasource = new OfficerListFileDatasource("data", "officer-data.json");
    }

    @Override
    public List<Officer> loadAccounts() {
        return datasource.readData().getOfficers();
    }

    @Override
    public OfficerList loadCollection() {
        return datasource.readData();
    }

    @Override
    public void saveCollection(OfficerList officers) {
        datasource.writeData(officers);
    }
}