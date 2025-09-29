package ku.cs.services;

import ku.cs.models.account.*;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.datasources.UserListFileDatasource;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.PasswordUtil;

import java.io.IOException;
import java.time.LocalDateTime;

public class SessionManager {
    private final AlertUtil alertUtil = new AlertUtil();
    private Account currentAccount;

    public SessionManager() {}

    public void authenticate(Account account, String rawPassword) {
        if (account == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if(account.getRole().equals(Role.USER)){
            if (account.isSuspend()) {
                throw new IllegalStateException(account.getUsername() + " is suspended account.\nPlease contact administrator.");
            }
        }

        String inputHashed = PasswordUtil.hashPassword(rawPassword);
        if (!inputHashed.equalsIgnoreCase(account.getPassword())) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        account.setLoginTime(LocalDateTime.now());
        currentAccount = account;
    }

    public void login(Account account) {
        this.currentAccount = account;
        String role = account.getRole().toString().toLowerCase();
        try {
            alertUtil.info("Welcome", "Login successful!");
            if(role.equals("officer")){
                FXRouter.goTo("officer-zone-list");
            }else {
                FXRouter.goTo(role + "-home");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout() {
        if (currentAccount == null) {
            try {
                FXRouter.goTo("home");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        String role = getCurrentAccount().getRole().toString();
        currentAccount = null;
        try {
            FXRouter.goTo(role.toLowerCase() + "-login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAuthenticated() {
        return currentAccount != null;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public Officer getOfficer() {
        if (!hasRole(Role.OFFICER)) return null;
        Datasource<OfficerList> officerListDatasource = new OfficerListFileDatasource("data", "test-officer-data.json");
        OfficerList officerList = officerListDatasource.readData();
        return officerList.findOfficerByUsername(currentAccount.getUsername());
    }

    public User getUser() {
        if(!hasRole(Role.USER)) { return null; }
        Datasource<UserList> userListDatasource = new UserListFileDatasource("data", "test-user-data.json");
        UserList userList = userListDatasource.readData();
        return userList.findUserByUsername(currentAccount.getUsername());
    }

    public boolean hasRole(Role role) {
        return currentAccount != null && currentAccount.getRole() == role;
    }

    public void requireRole(Role role, String loginRoute) {
        if (!hasRole(role)) {
            try {
                FXRouter.goTo(loginRoute);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void requireAdminLogin()   { requireRole(Role.ADMIN, "admin-login"); }
    public void requireOfficerLogin() { requireRole(Role.OFFICER, "officer-login"); }
    public void requireUserLogin()    { requireRole(Role.USER, "user-login"); }

    public void requireAdminOrOfficerLogin() {
        if(!(hasRole(Role.ADMIN) || hasRole(Role.OFFICER))) {
            requireRole(Role.OFFICER, "officer-login");
        }
    }

    public void logoutTestHelper() {
        currentAccount = null;
    }
}