package ku.cs.controllers.user;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.controllers.components.UserNavbarController;
import ku.cs.models.account.User;
import ku.cs.services.AppContext;
import ku.cs.services.SessionManager;

public abstract class BaseUserController {
    protected final SessionManager sessionManager = AppContext.getSessionManager();

    protected User current;

    @FXML protected UserNavbarController userNavbarController;
    protected Button footerNavBarButton;

    @FXML
    protected void initialize() {
        sessionManager.requireUserLogin();
        current = sessionManager.getUser();

        if (userNavbarController != null) {
            footerNavBarButton = userNavbarController.getFooterNavButton();
        }

        initDatasource();
        initUserInterfaces();
        initEvents();
    }

    protected abstract void initDatasource();
    protected abstract void initUserInterfaces();
    protected abstract void initEvents();
}