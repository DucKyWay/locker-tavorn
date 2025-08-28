package ku.cs.services;

import ku.cs.models.Account;
import ku.cs.models.Role;

public class SessionManager {
    private static Account currentAccount;

    public static void login(Account account) {
        currentAccount = account;
    }

    public static void logout() {
        currentAccount = null;
    }

    public static boolean isAuthenticated() {
        return currentAccount != null;
    }

    public static Account getCurrentAccount() {
        return currentAccount;
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
}
