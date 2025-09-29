package ku.cs.models.account;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import ku.cs.services.FXRouter;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
@JsonbPropertyOrder({"username", "firstname", "lastname", "email", "phone","loginTime","suspend","role","imagePath","password"})
public class User extends Account  implements Serializable {
    private boolean suspend;

    public User() {
        super();
    }
    public User(String username, String firstname, String lastname, String password,
                String email, String phone) {
        super(username, firstname, lastname, password, email, phone, Role.USER);
        this.suspend = false;
    }

    @Override
    public boolean isSuspend() {
        return suspend;
    }

    public void setSuspend(boolean suspend) { this.suspend = suspend; }

    public void toggleSuspend() { this.suspend = !this.suspend; }

    @Override
    public String toString() {
        return super.toString()
                .replace("}", ", suspend=" + suspend + "}");
    }

    public boolean getSuspend() {
        return this.suspend;
    }
}
