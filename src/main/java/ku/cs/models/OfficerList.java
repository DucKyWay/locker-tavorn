package ku.cs.models;

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
    public void addOfficer(String username, String name, String password,
                           String email, String telphone, String serviceZone, String imagePath) {
        username = username.trim();
        name = name.trim();
        password = password.trim();
        email = email.trim();
        telphone = telphone.trim();
        serviceZone = serviceZone != null ? serviceZone.trim() : null;
        imagePath = imagePath != null ? imagePath.trim() : null;

        if (!username.isEmpty() && !name.isEmpty() && !password.isEmpty()
                && !email.isEmpty() && !telphone.isEmpty()) {
            Officer officer = new Officer(username, name, password, email, telphone, imagePath);
            officer.setServiceZone(serviceZone);
            officers.add(officer);
        }
    }

    public void addOfficer(int idZone, String username, String name, String password,
                           String email, String telphone, String serviceZone, String imagePath) {
        username = username.trim();
        username = Officer.createUsername(idZone, username);
        name = name.trim();
        password = password.trim();
        email = email.trim();
        telphone = telphone.trim();
        serviceZone = serviceZone != null ? serviceZone.trim() : null;
        imagePath = imagePath != null ? imagePath.trim() : null;

        if (!username.isEmpty() && !name.isEmpty() && !password.isEmpty()
                && !email.isEmpty() && !telphone.isEmpty()) {
            Officer officer = new Officer(username, name, password, email, telphone, imagePath);
            officer.setServiceZone(serviceZone);
            officers.add(officer);
        }
    }

    public Officer findOfficerByUsername(String username) {
        for (Officer officer : officers) {
            if (officer.getUsername().equals(username)) {
                return officer;
            }
        }
        return null;
    }

    public ArrayList<Officer> getOfficers() {
        return officers;
    }
}
