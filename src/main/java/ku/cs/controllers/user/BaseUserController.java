package ku.cs.controllers.user;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.controllers.components.navbar.UserNavbarController;
import ku.cs.models.account.User;
import ku.cs.models.zone.Zone;
import ku.cs.services.session.SessionManager;
import ku.cs.services.ui.FXRouter;

public abstract class BaseUserController {
    protected final SessionManager sessionManager = (SessionManager) FXRouter.getService("session");

    protected User current;
    protected Zone currentZone;

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