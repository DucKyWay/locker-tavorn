package ku.cs.services;

import ku.cs.models.*;

import java.io.IOException;
import java.util.Objects;

public class AccountService {
    private Account account;

    private Datasource<Admin> adminDatasource;
    private Datasource<OfficerList> officersDatasource;
    private Datasource<UserList> usersDatasource;
    private OfficerList officers;
    private UserList users;

    public AccountService(Account account) {
        this.account = Objects.requireNonNull(account);
    }

    /**
     * change password
     *
     * @param currentPassword
     * @param newPassword
     */
    public void changePassword(String currentPassword, String newPassword) {
        Objects.requireNonNull(currentPassword, "currentPassword");
        Objects.requireNonNull(newPassword, "newPassword");

        if (!PasswordUtil.matches(currentPassword, account.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        switch (account.getRole()) {
            case ADMIN:
                adminDatasource = new AdminFileDatasource("data","test-admin-data.json");
                try {
                    Admin admin = adminDatasource.readData();
                    admin.setPassword(PasswordUtil.hashPassword(newPassword));
                    adminDatasource.writeData(admin);
                    System.out.println("Password changed for " + account.getRole() + " username=" + account.getUsername());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case OFFICER:
                officersDatasource = new OfficerListFileDatasource("data","test-officer-data.json");
                try {
                    officers = officersDatasource.readData();
                    Officer officer = officers.findOfficerByUsername(account.getUsername());
                    officer.setPassword(PasswordUtil.hashPassword(newPassword));
                    officersDatasource.writeData(officers);
                    System.out.println("Password changed for " + account.getRole() + " username=" + account.getUsername());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case USER:
                usersDatasource = new UserListFileDatasource("data","test-user-data.json");
                try {
                    users = usersDatasource.readData();
                    User user = users.findUserByUsername(account.getUsername());
                    user.setPassword(PasswordUtil.hashPassword(newPassword));
                    usersDatasource.writeData(users);
                    System.out.println("Password changed for " + account.getRole() + " username=" + account.getUsername());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                throw new IllegalArgumentException("Role mismatch for username=" + account.getUsername());
        }
    }
}
