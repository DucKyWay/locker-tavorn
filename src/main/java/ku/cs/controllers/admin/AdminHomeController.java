package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import ku.cs.models.account.Account;
import ku.cs.services.SessionManager;

import java.io.IOException;

public class AdminHomeController {
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

//        adminHomeLabel = DefaultLabel.h2("Home | Super Admin | " + current.getUsername());
    }

    private void initEvents() {

    }
}
