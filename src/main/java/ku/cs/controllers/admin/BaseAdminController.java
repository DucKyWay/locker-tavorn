package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.controllers.components.navbar.AdminNavbarController;
import ku.cs.models.account.Account;
import ku.cs.services.context.AppContext;
import ku.cs.services.session.SessionManager;

public abstract class BaseAdminController {
    protected final SessionManager sessionManager = AppContext.getSessionManager();

    protected Account current;

    @FXML protected AdminNavbarController adminNavbarController;
    protected Button footerNavBarButton;

    @FXML
    protected void initialize() {
        sessionManager.requireAdminLogin();
        current = sessionManager.getCurrentAccount();

//        if (adminNavbarController != null) {
//            footerNavBarButton = adminNavbarController.getFooterNavButton();
//        }

        initDatasource();
        initUserInterfaces();
        initEvents();
    }

    protected abstract void initDatasource();
    protected abstract void initUserInterfaces();
    protected abstract void initEvents();
}