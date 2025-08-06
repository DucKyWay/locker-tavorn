package ku.cs.models;

import java.util.ArrayList;

public class Officer {
    private String username;
    private String name;
    private String password;
    private String imagePath;
    private ArrayList<ServiceZone> serviceZone;

    public Officer(String username, String password, String name, String imagePath) {
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

    public ArrayList<ServiceZone> getServiceZone() {
        if (serviceZone == null) {
            serviceZone = new ArrayList<>();
        } else {
            serviceZone.clear();
        }
        return serviceZone;
    }

    public void setServiceZone(ArrayList<ServiceZone> serviceZone) {
        this.serviceZone = serviceZone;
    }

    @Override
    public String toString() {
        return "Officer{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", serviceZone=" + serviceZone +
                '}';
    }
}
