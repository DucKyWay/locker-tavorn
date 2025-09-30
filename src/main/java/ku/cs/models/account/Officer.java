package ku.cs.models.account;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import ku.cs.models.zone.Zone;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonbPropertyOrder({
        "username", "firstname", "lastname", "email", "status", "defaultPassword",
        "zoneUids", "phone", "loginTime", "role", "password", "imagePath"
})
public class Officer extends Account {
    @JsonbProperty("zoneUids")
    private List<String> zoneUids = new ArrayList<>();
    private boolean status;
    private String defaultPassword;

    public Officer() {
        super();
    }

    public Officer(String username, String firstname, String lastname,
                   String hashedPassword, String password,
                   String email, String phone, Role role) {
        super(username, firstname, lastname, hashedPassword, email, phone, role);
        this.status = false;
        this.defaultPassword = password;
    }

    public Officer(String username, String firstname, String lastname,
                   String hashedPassword, String password,
                   String email, String phone, ArrayList<String> zoneUids) {
        super(username, firstname, lastname,
                hashedPassword, email, phone, Role.OFFICER);
        this.zoneUids = zoneUids;
        this.status = false;
        this.defaultPassword = password;
    }

    public List<String> getZoneUids() {
        return zoneUids;
    }

    public void setZoneUids(List<String> zoneUids) {
        this.zoneUids = new ArrayList<>(zoneUids != null ? zoneUids : new ArrayList<>());
    }

    public void addZoneUid(String zoneUid) {
        if (zoneUid != null && !zoneUids.contains(zoneUid)) {
            zoneUids.add(zoneUid);
        }
    }

    public void removeZoneUid(String zoneUid) {
        if (zoneUid != null) {
            zoneUids.remove(zoneUid);
        }
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

    @Override
    public boolean isStatus() {
        return status;
    }

    @Override
    public void toggleStatus() {
        status = !status;
    }

    @Override
    public String toString() {
        return super.toString().replace(
                "}",
                ", zoneUids=" + zoneUids + ", status=" + status + "}"
        );
    }
}
