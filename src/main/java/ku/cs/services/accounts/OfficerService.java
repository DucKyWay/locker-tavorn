package ku.cs.services.accounts;

import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerForm;
import ku.cs.models.account.OfficerList;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.utils.PasswordUtil;

import java.util.ArrayList;
import java.util.List;

public class OfficerService {
    private final PasswordUtil passwordUtil = new PasswordUtil();
    private final OfficerAccountProvider provider = new OfficerAccountProvider();
    private OfficerList officers;

    public OfficerService() {
        officers = provider.loadCollection();
    }

    public Officer findByUsername(String username) {
        return officers.findByUsername(username);
    }

    public List<Officer> getAll() {
        return officers.getAccounts();
    }

    public void updateOfficer(Officer officer, OfficerForm form) {
        officer.setUsername(form.username());
        officer.setFirstname(form.firstname());
        officer.setLastname(form.lastname());
        officer.setEmail(form.email());
        officer.setPhone(form.phone());
        officer.setZoneUids(new ArrayList<>(form.zoneUids()));

        saveAll();
    }
    private void saveAll() {
        provider.saveCollection(officers);
    }
}
