package ku.cs.models;

public class Admin {
    private String username;
    private String name;
    private String password;
    private String imagePath;

    public Admin(String username, String password, String name, String imagePath) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
