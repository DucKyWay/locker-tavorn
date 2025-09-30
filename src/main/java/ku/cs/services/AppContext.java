package ku.cs.services;

import ku.cs.services.utils.TableColumnFactory;

public class AppContext {
    private static final SessionManager sessionManager = new SessionManager();
    private static final TableColumnFactory columnFactory = new TableColumnFactory();
    private static final OfficerService officerService = new OfficerService();

    public static SessionManager getSessionManager() {
        return sessionManager;
    }
    public static OfficerService getOfficerService() {
        return officerService;
    }
    public static TableColumnFactory getTableColumnFactory() {
        return columnFactory;
    }
}