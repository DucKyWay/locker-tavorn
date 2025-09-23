package ku.cs.models.account;

import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.UuidUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class OfficerList {
    private ArrayList<Officer> officers;
    public OfficerList() {
        officers = new ArrayList<>();
    }

    public void addOfficer(Officer officer) {
        if (officer != null) {
            officers.add(officer);
        }
    }

    public void addOfficer(String username, String firstname, String lastname, String hashedPassword, String password,int zoneId,
                             String serviceZone, String email, String phone) {
        username = username.trim();
        firstname = firstname.trim();
        lastname = lastname.trim();
        email = email.trim();
        phone = phone.trim();
        serviceZone = serviceZone != null ? serviceZone.trim() : null;

        if (!username.isEmpty() && !firstname.isEmpty() && !lastname.isEmpty() && !hashedPassword.isEmpty()
                && !email.isEmpty() && !phone.isEmpty()) {
            Officer officer = new Officer(zoneId, username, firstname, lastname, hashedPassword, password, email, phone, Role.OFFICER, null);
            officer.setServiceZone(serviceZone);
            officers.add(officer);
        }
    }

    public void addOfficer(int idZone, String username, String firstname, String lastname, String hashedPassword, String password,
                           String email, String phone, String serviceZone, String imagePath, LocalDateTime logintime) {
        username = username.trim();
        username = Officer.createUsername(idZone, username);
        firstname = firstname.trim();
        lastname = lastname.trim();
        email = email.trim();
        phone = phone.trim();
        serviceZone = serviceZone != null ? serviceZone.trim() : null;

        if (!username.isEmpty() && !firstname.isEmpty() && !lastname.isEmpty() && !hashedPassword.isEmpty()
                && !email.isEmpty() && !phone.isEmpty()) {
            Officer officer = new Officer(idZone,username, firstname, lastname, hashedPassword, password, email, phone, Role.OFFICER,logintime);
            officer.setServiceZone(serviceZone);
            officers.add(officer);
        }
    }

    public void removeOfficer(Officer officer) {
        officers.remove(officer);
    }

    public boolean removeOfficerByUsername(String username) {
        for (Officer officer : officers) {
            if (officer.getUsername().equals(username)) {
                officers.remove(officer);
                return true;
            }
        }
        return false;
    }

    public Officer findOfficerByUsername(String username) {
        for (Officer officer : officers) {
            if (officer.getUsername().equals(username)) {
                return officer;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "OfficerList{" +
                "officers=" + officers +
                '}';
    }

    public ArrayList<Officer> getOfficers() {
        return officers;
    }
}
