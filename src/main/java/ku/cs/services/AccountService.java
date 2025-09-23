package ku.cs.services;

import ku.cs.models.account.*;
import ku.cs.services.datasources.AdminFileDatasource;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.datasources.UserListFileDatasource;
import ku.cs.services.utils.PasswordUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class AccountService {
    private Account account;

    private Datasource<Account> adminDatasource;
    private Datasource<OfficerList> officersDatasource;
    private Datasource<UserList> usersDatasource;
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
                adminDatasource = new AdminFileDatasource("data","test-admin-data.json");

                Account admin = adminDatasource.readData();
                admin.setPassword(PasswordUtil.hashPassword(newPassword));
                adminDatasource.writeData(admin);
                System.out.println("Password changed for " + account.getRole() + " username=" + account.getUsername());

                break;
            case OFFICER:
                officersDatasource = new OfficerListFileDatasource("data","test-officer-data.json");

                System.out.println(officersDatasource);
                officers = officersDatasource.readData();
                Officer officer = officers.findOfficerByUsername(account.getUsername());
                officer.setPassword(PasswordUtil.hashPassword(newPassword));
                officersDatasource.writeData(officers);
                System.out.println(officersDatasource);
                System.out.println("Password changed for " + account.getRole() + " username=" + account.getUsername());

                break;
            case USER:
                usersDatasource = new UserListFileDatasource("data","test-user-data.json");

                users = usersDatasource.readData();
                User user = users.findUserByUsername(account.getUsername());
                user.setPassword(PasswordUtil.hashPassword(newPassword));
                usersDatasource.writeData(users);
                System.out.println("Password changed for " + account.getRole() + " username=" + account.getUsername());

                break;
            default:
                throw new IllegalArgumentException("Role mismatch for username=" + account.getUsername());
        }
    }

    public void changePasswordFirstOfficer(String newPassword) {
        Objects.requireNonNull(newPassword, "newPassword");

        officersDatasource = new OfficerListFileDatasource("data","test-officer-data.json");

        System.out.println(officersDatasource);
        officers = officersDatasource.readData();
        Officer officer = officers.findOfficerByUsername(account.getUsername());

        if(!officer.isStatus()) {
            officer.setPassword(PasswordUtil.hashPassword(newPassword));
            officer.changePassword();
            officersDatasource.writeData(officers);
            System.out.println(officersDatasource);
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
                    adminDatasource = new AdminFileDatasource("data","test-admin-data.json");
                    Account admin = adminDatasource.readData();
                    if (admin == null) throw new IllegalStateException("Admin not found");
                    admin.setImagePath(RELATIVE_PATH);
                    adminDatasource.writeData(admin);
                    System.out.println("Profile image changed for ADMIN username=" + account.getUsername());
                }
                case OFFICER -> {
                    officersDatasource = new OfficerListFileDatasource("data", "test-officer-data.json");
                    officers = officersDatasource.readData();
                    Officer officer = officers.findOfficerByUsername(account.getUsername());
                    if (officer == null) throw new IllegalStateException("Officer not found: " + account.getUsername());
                    officer.setImagePath(RELATIVE_PATH);
                    officersDatasource.writeData(officers);
                }
                case USER -> {
                    usersDatasource = new UserListFileDatasource("data", "test-user-data.json");
                    users = usersDatasource.readData();
                    User user = users.findUserByUsername(account.getUsername());
                    if (user == null) throw new IllegalStateException("User not found: " + account.getUsername());
                    user.setImagePath(RELATIVE_PATH);
                    usersDatasource.writeData(users);
                }
                default -> throw new IllegalArgumentException("Role mismatch for username=" + account.getUsername());
            }

    }
}
