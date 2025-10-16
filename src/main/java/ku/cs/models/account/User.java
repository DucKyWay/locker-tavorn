package ku.cs.models.account;
import jakarta.json.bind.annotation.JsonbPropertyOrder;

import java.io.Serializable;

@JsonbPropertyOrder({"username", "firstname", "lastname", "email", "phone","loginTime","role","imagePath","password"})
public class User extends Account implements Serializable {
    public User() {
        super();
    }

    public User(String username, String firstname, String lastname, String password,
                String email, String phone) {
        super(username, firstname, lastname, password, email, phone, Role.USER);
    }
}
