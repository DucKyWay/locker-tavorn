package ku.cs.models.account;
import java.io.Serializable;
import java.time.LocalDateTime;

public class User extends Account  implements Serializable {
    private int requestCount;
    private boolean suspend;

    public User() { super(); }

    public User(String username, String name, String password,
                String email, String telphone, int requestCount, Role role, LocalDateTime logintime) {
        super(username, name, password, email, telphone, role, logintime);
        this.requestCount = requestCount;
        this.suspend = false;
    }

    public User(String username, String name, String password,
                String email, String telphone, Role role, LocalDateTime logintime) {
        this(username, name, password, email, telphone,0, role,logintime);
        this.requestCount = 0;
        this.suspend = false;
    }

    public User(String username, String password, String name, String email,
                String telphone, int request_id, boolean suspend, Role role,LocalDateTime logintime) {
        super(username, name, password, email, telphone, role,logintime);
        this.requestCount = request_id;
        this.suspend = suspend;
    }

    public int getRequestCount() { return requestCount; }
    public void setRequestCount(int requestCount) { this.requestCount = requestCount; }
    public void increaseRequestCount() { this.requestCount++; }

    public boolean isSuspend() { return suspend; }
    public void setSuspend(boolean suspend) { this.suspend = suspend; }

    public boolean canRequest() { return true; }

    @Override
    public String toString() {
        return super.toString()
                .replace("}", ", requestCount=" + requestCount + ", suspend=" + suspend + "}");
    }

    public boolean getSuspend() {
        return this.suspend;
    }

    public int getRequest_id() {
        return this.requestCount;
    }

    public void setRequest_id(int request_id) {
        this.requestCount = request_id;
    }
}
