package ku.cs.controllers.user;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.models.account.Account;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;

import java.io.IOException;

public class UserHistoryController {

    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;

//    @FXML private TableView<> historyListTable;

    Account current = SessionManager.getCurrentAccount();

    @FXML
    public void initialize() {
        // Auth Guard
        SessionManager.requireUserLogin();
        current = SessionManager.getCurrentAccount();

        initialDatasourceZone();
        initUserInterface();
        initEvents();
    }
    private void initialDatasourceZone(){
    }

    private void initUserInterface() {
        LabelStyle.BODY_LARGE.applyTo(titleLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(descriptionLabel);
    }

    private void initEvents() {

    }
}
