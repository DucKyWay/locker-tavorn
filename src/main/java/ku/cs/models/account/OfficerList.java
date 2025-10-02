package ku.cs.models.account;

import java.util.ArrayList;
import java.util.List;

public class OfficerList extends AccountList<Officer> {

    public void addOfficer(String username, String firstname, String lastname,
                           String hashedPassword, String password, String email,
                           String phone, ArrayList<String> zoneUids) {
        if (!username.isEmpty() && !firstname.isEmpty() && !lastname.isEmpty()
                && !hashedPassword.isEmpty() && !password.isEmpty()
                && !email.isEmpty() && !phone.isEmpty()) {

            Officer officer = new Officer(username, firstname, lastname,
                    hashedPassword, password, email, phone, zoneUids);
            addAccount(officer);
        }
    }

    public Officer findOfficerByUsername(String username) {
        return findByUsername(username);  // เรียก generic method
    }

    public boolean canFindOfficerByUsername(String username) {
        return canFindByUsername(username);
    }

    public boolean updateImagePathToOfficer(String username, String newPath) {
        return updateImagePath(username, newPath);
    }

    public void removeOfficer(Officer officer) {
        removeAccount(officer);
    }

    public List<Officer> getOfficers() {
        return getAccounts();
    }
}
