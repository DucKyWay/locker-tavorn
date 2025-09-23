package ku.cs.models.account;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonbPropertyOrder({"username", "name", "surname", "email", "phone","logintime","role","imagePath","password"})
public class Account {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private String phone;
    private String imagePath;
    private Role role;
    private LocalDateTime logintime;

    public Account() {}

    public Account(String username, String firstname, String lastname, String password,
                   String email, String phone, Role role, LocalDateTime logintime) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.imagePath = getClass().getResource("/ku/cs/images/default_profile.png").toExternalForm();
        this.role = role;
        this.logintime = logintime;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getFullName() {
        if(firstname == null) return lastname;
        else if(lastname == null) return firstname;
        return firstname + " " + lastname;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isSuspended() {
        return false; // admin default
    }

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
        return getRole() + "{username='" + username + "', firstname='" + firstname + "', lastname='" + lastname +
                "', email='" + email + "', phone='" + phone + "', imagePath='" + imagePath + "', logintime='"+logintime+ "'}";
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
