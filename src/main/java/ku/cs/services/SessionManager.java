package ku.cs.services;

import ku.cs.models.User;

public class SessionManager {
    private static User currentUser;

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static boolean isAuthenticated() {
        return currentUser != null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void requireLogin() {
        if (!isAuthenticated()) {
            try {
                FXRouter.goTo("user-login");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}