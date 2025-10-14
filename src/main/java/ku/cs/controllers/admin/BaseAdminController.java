package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.controllers.components.navbar.AdminNavbarController;
import ku.cs.models.account.Account;
import ku.cs.services.session.SessionManager;
import ku.cs.services.ui.FXRouter;

public abstract class BaseAdminController {
    protected final SessionManager sessionManager = (SessionManager) FXRouter.getService("session");

    protected Account current;

    @FXML protected AdminNavbarController adminNavbarController;
    protected Button footerNavBarButton;

    @FXML
    protected void initialize() {
        sessionManager.requireAdminLogin();
        current = sessionManager.getCurrentAccount();

        initDatasource();
        initUserInterfaces();
        initEvents();
    }

    protected abstract void initDatasource();
    protected abstract void initUserInterfaces();
    protected abstract void initEvents();
}