package ku.cs.services.strategy.account;

import ku.cs.models.account.User;
import ku.cs.models.account.UserList;
import ku.cs.services.datasources.UserListFileDatasource;

import java.util.List;

public class UserAccountProvider implements AccountProvider<User> {
    private final UserListFileDatasource datasource;

    public UserAccountProvider() {
        this.datasource = new UserListFileDatasource("data", "test-user-data.json");
    }

    @Override
    public List<User> loadAccounts() {
        UserList list = datasource.readData();
        return list.getUsers();
    }

    @Override
    public void saveAccounts(List<User> users) {
        UserList list = new UserList();
        users.forEach(list::addUser);
        datasource.writeData(list);
    }
}
