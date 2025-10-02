package ku.cs.models.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserList extends AccountList<User> implements Serializable {

    public void addUser(String username, String password, String firstname,
                        String lastname, String email, String phone) {
        if (!username.isEmpty() && !password.isEmpty() && !firstname.isEmpty()
                && !lastname.isEmpty() && !email.isEmpty() && !phone.isEmpty()) {

            addAccount(new User(username, firstname, lastname,
                    password, email, phone));
        }
    }
    public void addUser(User user) {
        addAccount(user);
    }

    public User findUserByUsername(String username) {
        return findByUsername(username);
    }

    public boolean canFindUserByUsername(String username) {
        return canFindByUsername(username);
    }

    public boolean updateImagePathToUser(String username, String newPath) {
        return updateImagePath(username, newPath);
    }

    public boolean removeUserByUsername(String username) {
        return removeAccountByUsername(username);
    }

    public void removeUser(User user) {
        removeAccount(user);
    }

    public List<User> getUsers() {
        return getAccounts();
    }

    public int getCount() {
        return super.getCount();
    }
}
