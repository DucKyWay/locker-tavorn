package ku.cs.models.account;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import ku.cs.services.FXRouter;

import java.io.IOException;
import java.time.LocalDateTime;
@JsonbPropertyOrder({"username", "name", "email", "status", "defaultPassword", "zone", "telphone", "logintime", "role", "password", "imagePath"})
public class Officer extends Account {

    private String serviceZone;
    private boolean status; // not change password
    private String defaultPassword;

    public Officer() { super(); }

    public Officer(String username, String name, String hashedPassword, String password,
                   String email, String telphone, Role role) {
        super(username, name, hashedPassword, email, telphone, role, null);
        status = false;
        defaultPassword = password;
    }

    public Officer(String username, String name, String hashedPassword, String password,
                   int zoneId, String email, String phone) {
        super(createUsername(zoneId, username), name, hashedPassword, email, phone, Role.OFFICER, null);
        status = false;
        defaultPassword = password;
    }

    public Officer(int zone, String username, String name, String hashedPassword, String password,
                   String email, String telphone, Role role, LocalDateTime logintime) {
        super(createUsername(zone, username), name, hashedPassword, email, telphone, role, logintime);
        status = false;
        defaultPassword = password;
    }

    public static String createUsername(int zone,String username){
        String result = 'z'+ Integer.toString(zone);
        result +="-"+username;
        return result;
    }
    public String getServiceZone() { return serviceZone; }
    public void setServiceZone(String serviceZone) { this.serviceZone = serviceZone; }
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
