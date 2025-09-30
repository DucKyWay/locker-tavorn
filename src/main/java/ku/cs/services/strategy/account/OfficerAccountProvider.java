package ku.cs.services.strategy.account;

import ku.cs.models.account.Account;
import ku.cs.models.account.OfficerList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;

import java.util.List;

public class OfficerAccountProvider implements AccountProvider {
    private final Datasource<OfficerList> datasource;
    private OfficerList officers;

    public OfficerAccountProvider() {
        this.datasource = new OfficerListFileDatasource("data", "test-officer-data.json");
    }

    @Override
    public List<Account> loadAccounts() {
        return List.copyOf(datasource.readData().getOfficers());
    }

    @Override
    public void save(List<Account> accounts) {
        officers = new OfficerList();
        accounts.stream()
                .filter(a -> a.getRole().toString().equals("OFFICER"))
                .forEach(a -> officers.addOfficer((ku.cs.models.account.Officer) a));
        datasource.writeData(officers);
    }
}
