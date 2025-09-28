package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ku.cs.components.LabelStyle;
import ku.cs.models.account.Account;
import ku.cs.services.AppContext;
import ku.cs.services.SessionManager;

public class AdminHomeController {
    private final SessionManager sessionManager = AppContext.getSessionManager();

    @FXML private Label titleHome;
    @FXML private Label descriptionHome;

    private Account current;

    @FXML
    public void initialize() {
        // Auth Guard
        sessionManager.requireAdminLogin();
        current = sessionManager.getCurrentAccount();

        initUserInterface();
        initEvents();
    }

    private void initUserInterface() {

        titleHome.setText("Welcome Admin!");
        descriptionHome.setText("Have a nice day! " + current.getUsername() + ".");

        LabelStyle.TITLE_LARGE.applyTo(titleHome);
        LabelStyle.TITLE_MEDIUM.applyTo(descriptionHome);
    }

    private void initEvents() {

    }
}
