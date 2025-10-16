package ku.cs.models.account;

import java.util.ArrayList;

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
}
