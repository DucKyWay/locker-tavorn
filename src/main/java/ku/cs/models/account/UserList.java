package ku.cs.models.account;

import java.io.Serializable;

public class UserList extends AccountList<User> implements Serializable {

    public void addUser(String username, String password, String firstname,
                        String lastname, String email, String phone) {
        if (!username.isEmpty() && !password.isEmpty() && !firstname.isEmpty()
                && !lastname.isEmpty() && !email.isEmpty() && !phone.isEmpty()) {

            User user = new User(username, firstname, lastname,
                    password, email, phone);
            addAccount(user);
        }
    }
}
