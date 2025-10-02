package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import ku.cs.models.account.Officer;
import ku.cs.models.zone.Zone;
import ku.cs.services.context.AppContext;
import ku.cs.services.session.SessionManager;
import ku.cs.services.ui.FXRouter;

public abstract class BaseOfficerController {
    protected final SessionManager sessionManager = AppContext.getSessionManager();

    protected Officer current;
    protected Zone currentZone;

    @FXML protected void initialize() {
        sessionManager.requireOfficerLogin();
        current = sessionManager.getOfficer();
        currentZone = (Zone) FXRouter.getData();

        initDatasource();
        initUserInterfaces();
        initEvents();
    }

    protected abstract void initUserInterfaces();
    protected abstract void initDatasource();
    protected abstract void initEvents();
}