package ku.cs.models.account;
import jakarta.json.bind.annotation.JsonbPropertyOrder;

import java.io.Serializable;

@JsonbPropertyOrder({"username", "firstname", "lastname", "email", "phone","loginTime","status","role","imagePath","password"})
public class User extends Account implements Serializable {
    private boolean status;

    public User() {
        super();
    }
    public User(String username, String firstname, String lastname, String password,
                String email, String phone) {
        super(username, firstname, lastname, password, email, phone, Role.USER);
        this.status = true;
    }

    @Override
    public boolean isStatus() {
        return status;
    }

    @Override
    public void toggleStatus() {
        status = !status;
    }

    @Override
    public String toString() {
        return super.toString()
                .replace("}", ", status=" + status + "}");
    }

    public boolean getStatus() {
        return this.status;
    }
}
