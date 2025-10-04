package ku.cs.services.accounts.strategy;

import ku.cs.models.account.Account;
import ku.cs.services.datasources.AdminFileDatasource;

import java.util.List;

public class AdminAccountProvider {
    private final AdminFileDatasource datasource;

    public AdminAccountProvider() {
        this.datasource = new AdminFileDatasource("data", "admin-data.json");
    }

    public List<Account> loadAccount() {
        return List.of(datasource.readData());
    }

    public void saveAccount(Account account) {
        datasource.writeData(account);
    }
}
