package ku.cs.models.account;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import ku.cs.services.FXRouter;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
@JsonbPropertyOrder({"username", "firstname", "lastname", "email", "phone","logintime","suspend","role","imagePath","password"})
public class User extends Account  implements Serializable {
    private boolean suspend;

    public User() {
        super();
    }

    public User(String username, String firstname, String lastname, String password,
                String email, String phone,boolean suspend, Role role, LocalDateTime logintime) {
        super(username, firstname, lastname, password, email, phone, role, logintime);
        this.suspend = suspend;
    }

    public User(String username, String firstname, String lastname, String password,
                String email, String phone, Role role, LocalDateTime logintime) {
        this(username, firstname, lastname, password, email, phone,false,role, logintime);
        this.suspend = false;
    }

    @Override
    public boolean isSuspended() {
        return suspend;
    }

    public void setSuspend(boolean suspend) { this.suspend = suspend; }

    public void toggleSuspend() { this.suspend = !this.suspend; }

    public boolean canRequest() { return true; }

    public void goHome() throws IOException {
        FXRouter.goTo("user-home");
    }
    
    @Override
    public String toString() {
        return super.toString()
                .replace("}", ", suspend=" + suspend + "}");
    }

    public boolean getSuspend() {
        return this.suspend;
    }
}
