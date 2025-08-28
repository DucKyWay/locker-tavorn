package ku.cs.services;

import ku.cs.models.Admin;
import ku.cs.models.Officer;
import ku.cs.models.User;

public class SessionManager {
    private static User currentUser;
    private static Officer currentOfficer;
    private static Admin currentAdmin;

    public static void login(User user) {
        currentUser = user;
    }
    public static void login(Officer officer) { currentOfficer = officer; }
    public static void login(Admin admin) { currentAdmin = admin; }

    public static void logout() {
        currentUser = null;
    }

    public static boolean isAuthenticated() {
        return currentUser != null;
    }

    public static Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public static Officer getCurrentOfficer() {
        return currentOfficer;
    }
    public static User getCurrentUser() {
        return currentUser;
    }

    public static void requireAdminLogin() {
        if (!isAuthenticated()) {
            try {
                FXRouter.goTo("admin-login");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void requireOfficerLogin() {
        if (!isAuthenticated()) {
            try {
                FXRouter.goTo("officer-login");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void requireUserLogin() {
        if (!isAuthenticated()) {
            try {
                FXRouter.goTo("user-login");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}