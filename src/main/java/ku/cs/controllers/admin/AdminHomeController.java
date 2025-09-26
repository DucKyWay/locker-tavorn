package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ku.cs.components.LabelStyle;
import ku.cs.models.account.Account;
import ku.cs.services.SessionManager;

public class AdminHomeController {

    @FXML private Label titleHome;
    @FXML private Label descriptionHome;

    private Account current;

    @FXML
    public void initialize() {
        // Auth Guard
        SessionManager.requireAdminLogin();
        current = SessionManager.getCurrentAccount();

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
