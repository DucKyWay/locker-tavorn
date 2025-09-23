package ku.cs.models.account;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import java.time.LocalDateTime;
import java.util.ArrayList;

@JsonbPropertyOrder({"username", "firstname", "lastname", "email", "status", "defaultPassword", "zoneId", "serviceZone", "phone", "loginTime", "role", "password", "imagePath"})
public class Officer extends Account {
    private ArrayList<String> serviceZoneArray;
    private String serviceZone;
    private boolean status; // not change password
    private String defaultPassword;

    public Officer() { super(); serviceZoneArray = new ArrayList<>(); }

    public Officer(String username, String firstname, String lastname, String hashedPassword, String password,
                   String email, String phone, Role role) {
        super(username, firstname, lastname, hashedPassword, email, phone, role, null);
        status = false;
        defaultPassword = password;
    }

    public Officer(String username, String firstname, String lastname, String hashedPassword, String password,
                   int zoneId, String email, String phone) {
        super(createUsername(zoneId, username), firstname, lastname, hashedPassword, email, phone, Role.OFFICER, null);
        status = false;
        defaultPassword = password;
    }

    public Officer(int zoneId, String username, String firstname, String lastname, String hashedPassword, String password,
                   String email, String phone, Role role, LocalDateTime loginTime) {
        super(createUsername(zoneId, username), firstname, lastname, hashedPassword, email, phone, role, loginTime);
        status = false;
        defaultPassword = password;
    }

    public static String createUsername(int zone,String username){
        String result = 'z'+ Integer.toString(zone);
        result +="-"+username;
        return result;
    }
    public String getServiceZone() { return serviceZone; }
    public void setServiceZone(String serviceZone) {
        this.serviceZone = serviceZone;
        if(!serviceZoneArray.contains(serviceZone))serviceZoneArray.add(serviceZone);
    }
    public void addServiceZone(String serviceZone) {
        serviceZoneArray.add(serviceZone);
    }
    public void removeServiceZone(String serviceZone) {
        serviceZoneArray.remove(serviceZone);
    }
    public ArrayList<String> getServiceZoneArray() { return serviceZoneArray; }
    public int getIdZone(){
        String s = this.getUsername().split("-")[0];
        s = s.substring(1);
        return Integer.parseInt(s);
    }
    public boolean isInServiceZone(String zone) {
        return serviceZone != null && zone != null && serviceZone.equalsIgnoreCase(zone);
    }

    public boolean canServe(String zone) {
        return isInServiceZone(zone);
    }


    public String getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    public void changePassword() {
        status = true;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return super.toString().replace("}", ", serviceZone='" + serviceZone + ", status=" + status + "'}");
    }
}
