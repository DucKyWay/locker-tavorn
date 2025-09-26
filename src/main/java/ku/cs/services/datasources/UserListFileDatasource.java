package ku.cs.services.datasources;
import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerList;
import ku.cs.models.account.User;
import ku.cs.models.account.UserList;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserListFileDatasource implements Datasource<UserList> {
    private final JsonListFileDatasource<User, UserList> delegate;
    public UserListFileDatasource(String directoryName, String fileName) {
        this.delegate = new JsonListFileDatasource<>(
                directoryName,
                fileName,
                UserList::new,
                UserList::getUsers,
                UserList::addUser,
                User.class
        );
    }
    @Override
    public UserList readData() {
        return delegate.readData();
    }

    @Override
    public void writeData(UserList data) {
        delegate.writeData(data);
    }


}
