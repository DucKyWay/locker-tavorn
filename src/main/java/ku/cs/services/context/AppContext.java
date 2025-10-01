package ku.cs.services.context;

import ku.cs.services.accounts.OfficerService;
import ku.cs.services.session.SessionManager;
import ku.cs.services.utils.TableColumnFactory;

public class AppContext {
    private static final SessionManager sessionManager = new SessionManager();

    public static SessionManager getSessionManager() {
        return sessionManager;
    }
}