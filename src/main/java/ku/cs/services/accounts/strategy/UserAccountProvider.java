package ku.cs.services.accounts.strategy;

import ku.cs.models.account.User;
import ku.cs.models.account.UserList;
import ku.cs.services.datasources.UserListFileDatasource;

import java.util.List;

public class UserAccountProvider implements AccountProvider<User, UserList> {
    private final UserListFileDatasource datasource;

    public UserAccountProvider() {
        this.datasource = new UserListFileDatasource("data", "user-data.json");
    }

    @Override
    public List<User> loadAccounts() {
        return datasource.readData().getAccounts();
    }

    @Override
    public UserList loadCollection() {
        return datasource.readData();
    }

    @Override
    public void saveCollection(UserList users) {
        datasource.writeData(users);
    }
}
