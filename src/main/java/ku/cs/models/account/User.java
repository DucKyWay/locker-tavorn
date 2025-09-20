package ku.cs.models.account;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import ku.cs.services.FXRouter;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
@JsonbPropertyOrder({"username", "name", "email", "telphone","logintime","suspend","role","imagePath","password"})
public class User extends Account  implements Serializable {
    private boolean suspend;

    public User() { super(); }
    public User(String username, String name, String password,
                String email, String telphone,boolean suspend, Role role, LocalDateTime logintime) {
        super(username, name, password, email, telphone, role, logintime);
        this.suspend = suspend;
    }
    public User(String username, String name, String password,
                String email, String telphone, Role role, LocalDateTime logintime) {
        this(username, name, password, email, telphone,false,role, logintime);
        this.suspend = false;
    }

    public boolean isSuspend() { return suspend; }
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
