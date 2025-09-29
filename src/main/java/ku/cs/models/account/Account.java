package ku.cs.models.account;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonbPropertyOrder({"username", "firstname", "surname", "email", "phone","loginTime","role","imagePath","password"})
public class Account  {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private String phone;
    private String imagePath;
    private Role role;
    private LocalDateTime loginTime;

    public Account() {}

    public Account(String username, String firstname, String lastname, String password,
                   String email, String phone, Role role) {
        this.username = username.trim();
        this.firstname = StringUtils.capitalize(firstname.trim());
        this.lastname = StringUtils.capitalize(lastname.trim());
        this.password = password.trim();
        this.email = email.trim();
        this.phone = phone.trim();
        this.imagePath = getClass().getResource("/ku/cs/images/default_profile.png").toExternalForm();
        this.role = role;
        this.loginTime = null;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = StringUtils.capitalize(firstname); }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = StringUtils.capitalize(lastname); }

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

    public boolean isSuspend() {
        return false; // admin default
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return getRole() + "{username='" + username + "', firstname='" + firstname + "', lastname='" + lastname +
                "', email='" + email + "', phone='" + phone + "', imagePath='" + imagePath + "', loginTime='"+loginTime+ "'}";
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
