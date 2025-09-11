package ku.cs.models.account;
import java.io.Serializable;
import java.time.LocalDateTime;

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

    public boolean canRequest() { return true; }

    @Override
    public String toString() {
        return super.toString()
                .replace("}", ", suspend=" + suspend + "}");
    }

    public boolean getSuspend() {
        return this.suspend;
    }
}
