package ku.cs.models.account;

import java.util.ArrayList;
import java.util.List;

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

    public void addOfficer(String username, String firstname, String lastname,
                           String hashedPassword, String password, String email,
                           String phone, ArrayList<String> zoneUids) {

        if (!username.isEmpty() && !firstname.isEmpty() && !lastname.isEmpty() && !hashedPassword.isEmpty() && !password.isEmpty()
                && !email.isEmpty() && !phone.isEmpty()) {
            Officer officer = new Officer(username, firstname, lastname,
                    hashedPassword, password, email, phone, zoneUids);

            officers.add(officer);
        }
    }

    public void removeOfficer(Officer officer) {
        officers.remove(officer);
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

    public int getCount() {
        return officers.size();
    }
}
