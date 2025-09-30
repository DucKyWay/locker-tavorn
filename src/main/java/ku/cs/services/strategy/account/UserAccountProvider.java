package ku.cs.services.strategy.account;

import ku.cs.models.account.Account;
import ku.cs.models.account.UserList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.UserListFileDatasource;

import java.util.List;

public class UserAccountProvider implements AccountProvider {
    private final Datasource<UserList> datasource;
    private UserList users;

    public UserAccountProvider() {
        this.datasource = new UserListFileDatasource("data", "test-user-data.json");
    }

    @Override
    public List<Account> loadAccounts() {
        return List.copyOf(datasource.readData().getUsers());
    }

    @Override
    public void save(List<Account> accounts) {
        users = new UserList();
        accounts.stream()
                .filter(a -> a.getRole().toString().equals("USER"))
                .forEach(a -> users.addUser((ku.cs.models.account.User) a));
        datasource.writeData(users);
    }
}
