package ku.cs.services;

import ku.cs.models.account.*;
import ku.cs.services.datasources.AdminFileDatasource;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.datasources.UserListFileDatasource;
import ku.cs.services.strategy.account.OfficerAccountProvider;
import ku.cs.services.strategy.account.UserAccountProvider;
import ku.cs.services.utils.PasswordUtil;

import java.util.Objects;

public class AccountService {
    protected final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    protected final UserAccountProvider usersProvider = new UserAccountProvider();

    private final Account account;

    private Datasource<Account> adminDatasource;
    private OfficerList officers;
    private UserList users;

    public AccountService(Account account) {
        this.account = Objects.requireNonNull(account);
    }

    public void changePassword(String currentPassword, String newPassword) {
        Objects.requireNonNull(currentPassword, "currentPassword");
        Objects.requireNonNull(newPassword, "newPassword");

        if (!PasswordUtil.matches(currentPassword, account.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        switch (account.getRole()) {
            case ADMIN:
                adminDatasource = new AdminFileDatasource("data","admin-data.json");

                Account admin = adminDatasource.readData();
                admin.setPassword(PasswordUtil.hashPassword(newPassword));
                adminDatasource.writeData(admin);
                System.out.println("Password changed for " + account.getRole() + " username=" + account.getUsername());

                break;
            case OFFICER:
                officers = officersProvider.loadCollection();
                System.out.println(officers);
                Officer officer = officers.findOfficerByUsername(account.getUsername());
                officer.setPassword(PasswordUtil.hashPassword(newPassword));
                officersProvider.saveCollection(officers);
                System.out.println("Password changed for " + account.getRole() + " username=" + account.getUsername());

                break;
            case USER:
                users = usersProvider.loadCollection();
                User user = users.findUserByUsername(account.getUsername());
                user.setPassword(PasswordUtil.hashPassword(newPassword));
                usersProvider.saveCollection(users);
                System.out.println("Password changed for " + account.getRole() + " username=" + account.getUsername());

                break;
            default:
                throw new IllegalArgumentException("Role mismatch for username=" + account.getUsername());
        }
    }

    public void changePasswordFirstOfficer(String newPassword) {
        Objects.requireNonNull(newPassword, "newPassword");

        officers = officersProvider.loadCollection();

        System.out.println(officers);
        Officer officer = officers.findOfficerByUsername(account.getUsername());

        if(officer.isFirstTime()) {
            officer.setPassword(PasswordUtil.hashPassword(newPassword));
            officer.setFirstTime(false);
            officer.setDefaultPassword("");
            officersProvider.saveCollection(officers);
            System.out.println("Password changed for " + account.getRole() + " username=" + account.getUsername());
        } else {
            throw  new IllegalArgumentException(officer.getUsername() + " officer is already change password.");
        }
    }

    public void updateProfileImage(String filename) {
        Objects.requireNonNull(filename, "filename");

        String lower = filename.toLowerCase();
        if (!(lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg"))) {
            throw new IllegalArgumentException("filename is not an image");
        }

        final String RELATIVE_PATH = "images/profiles/" + filename;

        switch (account.getRole()) {
            case ADMIN -> {
                adminDatasource = new AdminFileDatasource("data","admin-data.json");
                Account admin = adminDatasource.readData();
                if (admin == null) throw new IllegalStateException("Admin not found");
                admin.setImagePath(RELATIVE_PATH);
                adminDatasource.writeData(admin);
                System.out.println("Profile image changed for ADMIN username=" + account.getUsername());
            }
            case OFFICER -> {
                officers = officersProvider.loadCollection();
                Officer officer = officers.findOfficerByUsername(account.getUsername());
                if (officer == null) throw new IllegalStateException("Officer not found: " + account.getUsername());
                officer.setImagePath(RELATIVE_PATH);
                officersProvider.saveCollection(officers);
            }
            case USER -> {
                users = usersProvider.loadCollection();
                User user = users.findUserByUsername(account.getUsername());
                if (user == null) throw new IllegalStateException("User not found: " + account.getUsername());
                user.setImagePath(RELATIVE_PATH);
                usersProvider.saveCollection(users);
            }
            default -> throw new IllegalArgumentException("Role mismatch for username=" + account.getUsername());
        }
    }
}
