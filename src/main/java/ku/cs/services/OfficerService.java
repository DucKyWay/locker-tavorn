package ku.cs.services;

import ku.cs.models.account.Officer;
import ku.cs.models.account.OfficerForm;
import ku.cs.models.account.OfficerList;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.strategy.account.OfficerAccountProvider;
import ku.cs.services.utils.PasswordUtil;

import java.util.ArrayList;
import java.util.List;

public class OfficerService {
    private final OfficerAccountProvider provider = new OfficerAccountProvider();
    private OfficerList officers;

    public OfficerService() {
        officers = provider.loadCollection();
    }

    public Officer findByUsername(String username) {
        return officers.findOfficerByUsername(username);
    }

    public List<Officer> getAll() {
        return officers.getOfficers();
    }

    public void createOfficer(OfficerForm form) {
        String hashed = PasswordUtil.hashPassword(form.password());
        officers.addOfficer(
                form.username(),
                form.firstname(),
                form.lastname(),
                hashed,
                form.password(), // plain password
                form.email(),
                form.phone(),
                new ArrayList<>(form.zoneUids())
        );
        provider.saveCollection(officers);
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

    public void save(Officer officer) {
        saveAll();
    }

    public void deleteOfficer(Officer officer) {
        officers.removeOfficer(officer);
        saveAll();
    }

    private void saveAll() {
        provider.saveCollection(officers);
    }
}
