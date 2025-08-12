package ku.cs.models;

public class Admin extends Account {
    public Admin() { super(); }

    public Admin(String username, String name, String password,
                 String email, String telphone, String imagePath) {
        super(username, name, password, email, telphone, imagePath);
    }

    public Admin(String username, String name, String password,
                 String email, String telphone, int requestCount, String imagePath) {
        super(username, name, password, email, telphone, imagePath);
    }

    @Override
    public String getRole() { return "ADMIN"; }
}
