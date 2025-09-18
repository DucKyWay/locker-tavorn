package ku.cs.models.account;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import javafx.scene.image.Image;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
@JsonbPropertyOrder({"username", "name", "email", "telphone","logintime","role","imagePath","password"})
public class Account {
    private String username;
    private String name;
    private String password;
    private String email;
    private String telphone;
    private String imagePath;
    private Role role;
    private LocalDateTime logintime;

    public Account() {}

    public Account(String username, String name, String password,
                   String email, String telphone, Role role, LocalDateTime logintime) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.telphone = telphone;
        this.imagePath = getClass().getResource("/ku/cs/images/default_profile.png").toExternalForm();
        this.role = role;
        this.logintime = logintime;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelphone() { return telphone; }
    public void setTelphone(String telphone) { this.telphone = telphone; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getLogintime() {
        return logintime;
    }

    public void setLogintime(LocalDateTime logintime) {
        this.logintime = logintime;
    }

    public boolean matchUsername(String username) {
        return this.username != null && this.username.equals(username);
    }

    public boolean matchPassword(String password) {
        return this.password != null && this.password.equals(password);
    }

    @Override
    public String toString() {
        return getRole() + "{username='" + username + "', name='" + name + "', email='" + email +
                "', telphone='" + telphone + "', imagePath='" + imagePath + "', logintime='"+logintime+ "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(username, account.username);
    }

    @Override
    public int hashCode() { return Objects.hash(username); }
}
