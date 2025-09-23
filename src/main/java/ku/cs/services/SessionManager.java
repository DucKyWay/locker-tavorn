package ku.cs.services;

import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.account.Role;
import ku.cs.models.account.User;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.PasswordUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class SessionManager {
    private static Account currentAccount;

    public static void authenticate(Account account, String rawPassword) {
        if (account == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if(account.getRole().equals(Role.USER)){
            if (account.isSuspended()) {
                throw new IllegalStateException(account.getUsername() + " is suspended account.\nPlease contact administrator.");
            }
        }

        String inputHashed = PasswordUtil.hashPassword(rawPassword);
        if (!inputHashed.equalsIgnoreCase(account.getPassword())) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        account.setLogintime(LocalDateTime.now());
        currentAccount = account;
    }

    public static void login(Account account) {
        String role = account.getRole().toString().toLowerCase();
        try {
            AlertUtil.info("Welcome", "Login successful!");
            FXRouter.goTo(role + "-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void logout() {
        String role = getCurrentAccount().getRole().toString();
        currentAccount = null;
        try {
            FXRouter.goTo(role.toLowerCase() + "-login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isAuthenticated() {
        return currentAccount != null;
    }

    public static Account getCurrentAccount() {
        return currentAccount;
    }

    public static Officer getOfficer() {
        return hasRole(Role.OFFICER) ? (Officer) currentAccount : null;
    }

    public static User getUser() {
        return hasRole(Role.USER) ? (User) currentAccount : null;
    }

    public static boolean hasRole(Role role) {
        return currentAccount != null && currentAccount.getRole() == role;
    }

    public static void requireRole(Role role, String loginRoute) {
        if (!hasRole(role)) {
            try {
                FXRouter.goTo(loginRoute);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void requireAdminLogin()   { requireRole(Role.ADMIN, "admin-login"); }
    public static void requireOfficerLogin() { requireRole(Role.OFFICER, "officer-login"); }
    public static void requireUserLogin()    { requireRole(Role.USER, "user-login"); }

    public static void requireAdminOrOfficerLogin() {
        if(!(hasRole(Role.ADMIN) || hasRole(Role.OFFICER))) {
            requireRole(Role.OFFICER, "officer-login");
        }
    }

    public static void logoutTestHelper() {
        currentAccount = null;
    }
}
