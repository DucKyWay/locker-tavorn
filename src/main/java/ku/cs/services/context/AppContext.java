package ku.cs.services.context;

import ku.cs.services.session.SessionManager;

public class AppContext {
    private static final SessionManager sessionManager = new SessionManager();
    public static SessionManager getSessionManager() {
        return sessionManager;
    }
}