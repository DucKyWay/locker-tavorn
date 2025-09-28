package ku.cs.services;

public class AppContext {
    private static final SessionManager sessionManager = new SessionManager();

    public static SessionManager getSessionManager() {
        return sessionManager;
    }
}