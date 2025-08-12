package ku.cs.models;

import java.util.Objects;

public class Account {
    private String username;
    private String name;
    private String password;
    private String email;
    private String telphone;
    private String imagePath;

    public Account() {}

    public Account(String username, String name, String password,
                   String email, String telphone, String imagePath) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.telphone = telphone;
        this.imagePath = imagePath;
    }

    public Account(String username, String name, String password,
                   String email, String telphone, int requestCount, String imagePath) {
        this(username, name, password, email, telphone, imagePath);
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

    public boolean matchUsername(String username) {
        return this.username != null && this.username.equals(username);
    }

    public boolean matchPassword(String password) {
        return this.password != null && this.password.equals(password);
    }

    public String getRole() { return "ACCOUNT"; }

    @Override
    public String toString() {
        return getRole() + "{username='" + username + "', name='" + name + "', email='" + email +
                "', telphone='" + telphone + "', imagePath='" + imagePath + "'}";
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
