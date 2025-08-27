package ku.cs.models;

public class Admin extends Account {
    public Admin() {
        super();
    }

    public Admin(String username, String name, String password,
                 String email, String telPhone, String imagePath) {
        super(username, name, password, email, telPhone, imagePath);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}